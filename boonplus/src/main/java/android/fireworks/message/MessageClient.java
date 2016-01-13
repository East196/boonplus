package android.fireworks.message;

public interface MessageClient {
	/*** 开启服务器 **/
	public void startServer();

	/** 停止服务器 **/
	public void stopServer();

	/*** 是否已连接 ***/
	public boolean isConnected();

	/*** 发送消息 */
	public void sendMessage(Object object);
}
