package fr.tkhosravi.flashme.android.camera;

import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import fr.tkhosravi.flashme.R;

public class CameraPreviewCallback implements Camera.PreviewCallback {

  private Handler mHandler;
  private String TAG = "FlashMe";

  public void setHandler(Handler handler) {
    Log.i(TAG, "Set preview callback handler");
    mHandler = handler;
  }

  public void onPreviewFrame(byte[] data, Camera camera) {
    if(mHandler != null) {
      Message msg = Message.obtain(mHandler, R.integer.frame, data);
      msg.sendToTarget();
    }
    else
      Log.i("PreviewCallback", "Handler null");
  }

}
