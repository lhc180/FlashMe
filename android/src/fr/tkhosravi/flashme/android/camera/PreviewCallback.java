package fr.tkhosravi.flashme.android.camera;

import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;
import fr.tkhosravi.flashme.android.FlashYouActivity;

public class PreviewCallback implements android.hardware.Camera.PreviewCallback {

  private DecodeThread m_decodeThread;

  int i = 0;

  public PreviewCallback(DecodeThread decodeThread) {
    m_decodeThread = decodeThread;
  }

  public void onPreviewFrame(byte[] data, Camera camera) {
    Message msg = Message.obtain();
    msg.obj = data;
    m_decodeThread.getHandler().sendMessage(msg);
  }

}
