package android.fireworks.message;

import java.lang.Thread.UncaughtExceptionHandler;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.Executors;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.json.JSONException;
import org.json.JSONObject;

import com.zqgame.mbm.data.Constant;
import com.zqgame.mbm.http.Url;
import com.zqgame.mbm.model.ChatMsg;
import com.zqgame.mbm.model.MbmApplication;
import com.zqgame.mbm.model.MessageInfo;
import com.zqgame.mbm.service.MessageInfoService;
import com.zqgame.mbm.socket.MessageManager;
import com.zqgame.mbm.util.LogUtil;
import com.zqgame.mbm.util.LogUtil.LOG_ENUM;
import com.zqgame.mbm.util.MinaInitChat;
import com.zqgame.mbm.util.StringUtil;
import com.zqgame.mbm.util.Utils;

import android.util.Log;

public class MinaMessageClient extends IoHandlerAdapter implements MessageClient {
	private MessageInfoService messageInfoService;
	/** 使用情况下检查网络的时间间隔 */
	private static final long NORMAL_CHECK_TIME = 10 * 1000;
	/** 重连时的时间间隔 */
	private static final long RECONNECT_CHECK_TIME = 3000;
	/** 连接器 */
	private IoConnector connector;
	/** 会话属性 */
	private ConnectFuture connectFuture;
	/** 连接会话 */
	public IoSession session;
	/** 检测网络状态的线程 */
	private Thread deamonThread;
	/** 是否已经登录 */
	public boolean isLogin = false;
	/** 是否将线程停止 */
	private boolean isStop = false;
	/** 判断socket是否已经连接 */
	public static boolean isSocketConn = false;

	@Override
	public void startServer() {
		init();
	}

	@Override
	public void stopServer() {
		Log.i("test", "stopServer");
		isStop = true;
		if (deamonThread != null) {
			deamonThread.interrupt();
		}
		closeConnect();
		if (connector != null) {
			connector.dispose(true);
		}
	}

	/*** 初始化mina **/
	private void init() {
		initConnector();
		startDaemonThread();
	}

	/**
	 * 初始化connector对象
	 */
	private void initConnector() {
		// 编码工厂
		TextLineCodecFactory factory = new TextLineCodecFactory(Charset.forName("UTF-8"));
		factory.setDecoderMaxLineLength(Integer.MAX_VALUE);
		factory.setEncoderMaxLineLength(Integer.MAX_VALUE);
		// 链接属性
		connector = new NioSocketConnector(Runtime.getRuntime().availableProcessors());
		connector.setConnectTimeoutMillis(2 * 1000);
		connector.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 1000 * 60 * 10);
		connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(factory));
		connector.getFilterChain().addLast("threadPool", new ExecutorFilter(Executors.newCachedThreadPool()));
		connector.setHandler(this);
	}

	/***
	 * 执行连接
	 * 
	 * @throws InterruptedException
	 */
	public synchronized void connect() {
		if (!Utils.isNetworkAvailable(MbmApplication.getInstance())) {
			return;
		}
		closeConnect();
		LogUtil.log(LOG_ENUM.INFO, "Mina is connecting to Server.IP:" + Url.SERVER_IP + ",port:" + Url.SERVER_PORT);
		if (connector.isDisposed()) {
			initConnector();
		}
		connectFuture = connector.connect(new InetSocketAddress(Url.SERVER_IP, Url.SERVER_PORT));
		connectFuture.awaitUninterruptibly();
		connectFuture.addListener(new IoFutureListener<ConnectFuture>() {
			@Override
			public void operationComplete(ConnectFuture connectFuture) {
				connectFuture.removeListener(this);
				boolean isDone = connectFuture.isDone();
				Throwable ex = connectFuture.getException();
				boolean isConnect = connectFuture.isConnected();
				if ((isDone == false) || (ex != null) || (isConnect == false)) {
					LogUtil.log(LOG_ENUM.ERROR, "Can not connect to msg server!!!");
					return;
				}
				session = connectFuture.getSession();
				sendLogin();
				LogUtil.log(LOG_ENUM.INFO, "Connected on msg server!!!");
			}
		});
	}

	/*** 关闭连接 **/
	public void closeConnect() {
		try {
			if ((session != null) && session.isConnected()) {
				session.close(true);
			}
			if (connectFuture != null) {
				connectFuture.cancel();
			}
			LogUtil.log(LOG_ENUM.INFO, "成功关闭Mina Socket链接");
		} catch (Exception ex) {
			LogUtil.log(LOG_ENUM.ERROR, "关闭连接异常");
		}
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		super.sessionCreated(session);
	}

	/*** 启动守护线程 **/
	public synchronized void startDaemonThread() {
		Log.i("test", "MessageServer-startDaemonThread");
		try {
			if (!isStop && (deamonThread != null) && deamonThread.isAlive()) {
				if (deamonThread != null) {
					deamonThread.interrupt();
					deamonThread = null;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		isStop = false;
		deamonThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (!isStop) {
					LogUtil.log(LOG_ENUM.INFO, "正在检查socket 连接是否正常");
					try {
						if (isConnected()) {
							LogUtil.log(LOG_ENUM.INFO, "Socket 连接正常");
							isSocketConn = true;
							if (isLogin) {
								// 发送心跳包
								ChatMsg chatMsg = new ChatMsg();
								chatMsg.setJsonMsg(MinaInitChat.getLiveJsonStr());
								sendMessage(chatMsg);
							}
						} else if (!isStop) {
							LogUtil.log(LOG_ENUM.ERROR, "Socket 异常,执行重新连接");
							isSocketConn = false;
							connect();
						} else {
							break;
						}

						if (isSocketConn) {
							Thread.sleep(NORMAL_CHECK_TIME);
						} else {
							Thread.sleep(RECONNECT_CHECK_TIME);// socket异常时，重连检查时间缩短
						}
					} catch (InterruptedException e) {
						LogUtil.log(LOG_ENUM.ERROR, "InterruptedException" + isStop);
						// LogUtil.log(LOG_ENUM.ERROR, e.getMessage());
						// if(!isStop){
						// try {
						// Thread.sleep(RECONNECT_CHECK_TIME);// socket异常时，重连检查时间缩短
						// } catch (InterruptedException e1) {
						// e1.printStackTrace();
						// }
						// }
					} catch (Exception ex) {
						LogUtil.log(LOG_ENUM.ERROR, ex.getMessage());
						// if(!isStop){
						// try {
						// Thread.sleep(RECONNECT_CHECK_TIME);// socket异常时，重连检查时间缩短
						// } catch (InterruptedException e) {
						// e.printStackTrace();
						// }
						// }
					}
				}
			}
		});
		deamonThread.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread thread, Throwable ex) {
				LogUtil.log(LOG_ENUM.ERROR, ex.getMessage());
			}
		});
		deamonThread.start();
	}

	/**
	 * 判断线程
	 * 
	 * @return
	 */
	private boolean isThreadRunning() {
		return (deamonThread != null) && deamonThread.isAlive();
	}

	/***
	 * 判断连接是否正常
	 * 
	 * @return boolean
	 */
	@Override
	public boolean isConnected() {
		if (session == null) {
			return false;
		}
		if (session.isClosing()) {
			return false;
		}
		if (!session.isConnected()) {
			return false;
		}
		return true;
	}

	/*** 接受到消息 */
	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		if (message == null) {
			return;
		}
		receiveJsonAndType(message.toString());
	}

	/*** 发送登录协议 ***/
	private void sendLogin() {
		String login = MinaInitChat.getLoginJsonStr();
		if (!StringUtil.isEmptyString(login)) {
			try {
				JSONObject loginJson = new JSONObject(login);
				WriteFuture writeFuture = session.write(loginJson);
				LogUtil.log(LOG_ENUM.ERROR, "登陆指令发送成功:" + loginJson);
				writeFuture.awaitUninterruptibly();
				writeFuture.addListener(new IoFutureListener<WriteFuture>() {
					@Override
					public void operationComplete(WriteFuture future) {
						Throwable ex = future.getException();
						if ((ex != null) && isConnected()) {
							sendLogin();
						}
					}
				});
				isLogin = true;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			isLogin = false;
		}

	}

	/***
	 * 发送消息
	 * 
	 * @param jsonObject
	 */
	@Override
	public void sendMessage(Object object) {
		try {
			ChatMsg chatMsg = (ChatMsg) object;
			if (chatMsg != null) {
				WriteFuture writeFuture = null;
				if (session != null) {
					writeFuture = session.write(new JSONObject(chatMsg.getJsonMsg()));
					writeFuture.awaitUninterruptibly();
				} else {
					// //判断服务是否有开启
					// if(!Utils.isServiceRunning(context,
					// MessageServer.class.getName())){
					// MbmApplication.getInstance().startAppServer();
					// }
				}

				if ((chatMsg.getMsg() == null) || (chatMsg.getMsg().getMsgID() == Constant.MSG_ID.MSG_BACK_STATE)) { // 阅读回执
					return;
				}

				if ((writeFuture != null) && writeFuture.isWritten()) {
					LogUtil.log(LOG_ENUM.ERROR, "数据发送成功:" + chatMsg.getJsonMsg());
					long id = chatMsg.getMsg().getId();
					if (id > 0) {
						messageInfoService.updateSendState(id, Constant.MSG_TYPE.SEND_MSG_SUCCESS + "");
					}
					MessageInfo messageInfo = chatMsg.getMsg();
					messageInfo.setSendState(Constant.MSG_TYPE.SEND_MSG_SUCCESS);
					messageInfo.setSendFileState(Constant.MSG_TYPE.FILEMSG_SEND_OK);
					chatMsg.setMsg(messageInfo);
					MessageManager.getInstance().refreshSendResultMsg(chatMsg);
				} else { // 发送失败逻辑处理
					LogUtil.log(LOG_ENUM.ERROR, "数据发送失败:" + chatMsg.getJsonMsg());
					long id = chatMsg.getMsg().getId();
					if (id > 0) {
						messageInfoService.updateSendState(id, Constant.MSG_TYPE.SEND_MSG_ERROR + "");
					}
					MessageInfo messageInfo = chatMsg.getMsg();
					messageInfo.setSendState(Constant.MSG_TYPE.SEND_MSG_ERROR);
					chatMsg.setMsg(messageInfo);
					MessageManager.getInstance().refreshSendResultMsg(chatMsg);
					if (!isThreadRunning()) {// 如果线程停止，重启*-、
						MbmApplication.getInstance().startAppServer();
					}

				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	// /*** 启动守护线程 ***/
	// public void startDeamonThread() {
	// if (deamonThread == null)
	// startDaemonThread();
	// else {
	// synchronized (deamonThread) {
	// if (deamonThread.isAlive())
	// deamonThread.notify();
	// else if (deamonThread.isAlive() == false)
	// deamonThread.start();
	// }
	// }
	// }

	/**
	 * 会话关闭事件
	 * 
	 * @param session
	 */
	@Override
	public void sessionClosed(IoSession session) {
		LogUtil.log(LOG_ENUM.ERROR, "session has been closed");
		isLogin = false;
		if (!isStop) {// 不是正常退出，则重新唤起线程
			LogUtil.log(LOG_ENUM.ERROR, "重新唤起线程");
			deamonThread.interrupt();
		}
	}

	/**
	 * 会话进入闲置状态
	 * 
	 * @param session
	 * @param status
	 */
	@Override
	public void sessionIdle(IoSession session, IdleStatus status) {
		LogUtil.log(LOG_ENUM.INFO, "session idle state:" + status);
	}

	/**
	 * 异常事件
	 * 
	 * @param session
	 * @param cause
	 */
	@Override
	public void exceptionCaught(IoSession session, Throwable cause) {
		LogUtil.log(LOG_ENUM.INFO, "exception cause:" + cause);
	}

	/**
	 * 消息发出去事件
	 * 
	 * @param session
	 * @param message
	 */
	@Override
	public void messageSent(IoSession session, Object message) {
	}

	/**
	 * 根据不同类型数据填充到不同的集合 交给对应的逻辑处理线程处理业务逻辑
	 * 
	 * @param type
	 *            消息类型
	 * @param message
	 *            消息体
	 */
	public void receiveJsonAndType(String message) {
		try {
			// 处理服务器在线发送过来的消息信息
			LogUtil.log(LOG_ENUM.ERROR, "receive JSon is :[" + message + "]");
			MessageManager.getInstance().handleReceiveMsg(message);
		} catch (Exception e) {
			e.printStackTrace();
			// 异常情况是否需要重启服务
			LogUtil.log(LOG_ENUM.ERROR, "ReceiveJSonAndType JSon is error...");
		}
	}
}
