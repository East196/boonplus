/**Copyright(c)2012,中青宝SNS社交游戏事业部
 *Allrights reserved
 *
 *文件名称：NetWorkBroadcastReceiver.java
 *摘要：网络监测广播
 *
 *当前版本：1.0
 *作者：刘海军（2401）
 *完成日期：2012年11月1日
 */
package android.fireworks.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.os.Handler;

/**
 * 网络监测广播
 * 
 * @author 刘海军(2401)
 */
public class NetWorkBroadcastReceiver extends BroadcastReceiver {
	private static Handler handler;
	// 0表示在线，-1 不在线
	public static int isnet = 0;
	public static int networkType = 0;

	@Override
	public void onReceive(Context context, Intent intent) {
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		// 判断是否正在使用WIFI网络
		State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.getState();
		isnet = State.CONNECTED == state ? 0 : -1;
		if (isnet == 0) {
			networkType = 1; // wifi环境
		}
		// WIFI 连接不成功的情况下开启GPRS连接网络
		if (State.CONNECTED != state) {
			if(connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null){
				state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
				// 判断是否正在使用GPRS网络
				isnet = State.CONNECTED == state ? 0 : -1;
				if (isnet == 0) {
					networkType = 2;// GPRS环境
				}
			}
		}
	}

	/*** 设置handler刷新 */
	public static void setHandler(Handler handler) {
		NetWorkBroadcastReceiver.handler = handler;
	}

	public static Handler getHandler() {
		return handler;
	}
}
