package android.fireworks.broadcast;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.zqgame.mbm.ui.activity.BaseActivity;
import com.zqgame.mbm.util.LogUtil;
import com.zqgame.mbm.util.LogUtil.LOG_ENUM;
import com.zqgame.mbm.util.Utils;
/**
 * 屏幕关闭开启监听
 * @author 唐晓泽
 *
 */
public class ScreenActionReceiver extends BroadcastReceiver {

	private static boolean isRegisterReceiver = false;
	private static ScreenActionReceiver receiver=new ScreenActionReceiver();
	public static ScreenActionReceiver getInstance(){
		if(receiver==null){
			receiver=new ScreenActionReceiver();
		}
		return receiver;
	}
	private ScreenActionReceiver(){}
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (action.equals(Intent.ACTION_SCREEN_ON)) {
			LogUtil.log("屏幕开启广播...");
			handlerScreenListener(true);
			if (Utils.isTopActivity()&&!BaseActivity.isActive) {
				BaseActivity.isActive = true;
				BaseActivity.cancelExitAppDelay(context);
				LogUtil.log(LOG_ENUM.INFO, "APP从后台唤醒,进入前台");
			}
		} else if (action.equals(Intent.ACTION_SCREEN_OFF)) {
			handlerScreenListener(false);
			LogUtil.log( "屏幕关闭广播...");
			if (Utils.isTopActivity()&&BaseActivity.isActive) {//在前台运行
				LogUtil.log(LOG_ENUM.INFO, "记录当前已经进入后台,10分钟后退出程序");
				BaseActivity.isActive = false;
				BaseActivity.exitAppDelayed(context,BaseActivity.DEFAULT_TIME_OUT);
			}
		}
	}

	public static void registerScreenActionReceiver(Context mContext) {
		if (!isRegisterReceiver) {
			isRegisterReceiver = true;
			try{
				IntentFilter filter = new IntentFilter();
				filter.addAction(Intent.ACTION_SCREEN_OFF);
				filter.addAction(Intent.ACTION_SCREEN_ON);
				LogUtil.log( "注册屏幕解锁、加锁广播接收者...");
				mContext.registerReceiver(getInstance(), filter);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	public static void unRegisterScreenActionReceiver(Context mContext) {
		if (isRegisterReceiver) {
			isRegisterReceiver = false;
			LogUtil.log( "注销屏幕解锁、加锁广播接收者...");
			try{
				mContext.unregisterReceiver(getInstance());
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	
	
	///////////////屏幕监听////////////////////////////////
	private static ArrayList<OnScreenListener> listenerArray=new ArrayList<OnScreenListener>();
	public static void addScreenListener(OnScreenListener listener){
		if(listener!=null)
			listenerArray.add(listener);
	}
	public static void removeScreenListener(OnScreenListener listener){
		if(listener!=null)
			listenerArray.remove(listener);
	}
	public static void clearScreenListenerList(){
		listenerArray.clear();
	}
	/**
	 * @param isOn true为开启，false为关闭
	 */
	public void handlerScreenListener(boolean isOn){
		for(OnScreenListener listener:listenerArray){
			if(listener!=null){
				listener.screenStateChanged(isOn);
			}
		}
	}
	public interface OnScreenListener{
		/**
		 * @param isOn true为开启，false为关闭
		 */
		public void screenStateChanged(boolean isOn);
	}
}