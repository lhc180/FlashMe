package fr.tkhosravi.flashme.android.camera;

import fr.tkhosravi.flashme.R;
import android.hardware.Camera;
import android.os.Message;

public class PreviewCallback implements android.hardware.Camera.PreviewCallback {

  private DecodeThread m_decodeThread;

  int i = 0;

  public PreviewCallback(DecodeThread decodeThread) {
    m_decodeThread = decodeThread;
  }

  public void onPreviewFrame(byte[] data, Camera camera) {
    Message msg = Message.obtain();
    msg.obj = data;
    msg.what = R.integer.frame;
    m_decodeThread.getHandler().sendMessage(msg);
  }

}
