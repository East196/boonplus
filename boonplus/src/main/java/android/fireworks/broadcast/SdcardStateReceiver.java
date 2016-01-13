package android.fireworks.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class SdcardStateReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (action.equals(Intent.ACTION_MEDIA_MOUNTED)) {// sd卡成功挂载

		} else if (action.equals(Intent.ACTION_MEDIA_EJECT)) {// sd卡被移除
			Toast.makeText(context, "内存卡已拔出，图片与语音等功能暂时无法使用", Toast.LENGTH_SHORT).show();
		}
	}
}