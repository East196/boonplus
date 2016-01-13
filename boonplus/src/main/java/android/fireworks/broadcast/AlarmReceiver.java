package android.fireworks.broadcast;

import com.zqgame.mbm.model.MbmApplication;
import com.zqgame.mbm.ui.activity.BaseActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals("exit")) {// 关闭程序
			if (!BaseActivity.isActive) {
				MbmApplication.getInstance().exit();
			}
		} else {
		}
	}
}