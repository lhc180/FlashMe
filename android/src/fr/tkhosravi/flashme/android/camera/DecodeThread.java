package fr.tkhosravi.flashme.android.camera;

import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.common.GlobalHistogramBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

public class DecodeThread extends Thread {
  
  final private String TAG = "DecodeThread"; 
  
  private Handler m_handler;
  private Camera m_camera;
  private Handler mainHandler;
  
  public DecodeThread(Handler handler, Camera camera) {
    m_camera = camera;
    mainHandler = handler;
  }

  public void run() {
    Looper.prepare();
    
    m_handler = new Handler(new DecodeCallback(m_camera));
    
    Looper.loop();
  }
  
  private class DecodeCallback implements Callback {
    
    private boolean decoded = false;
    private Camera m_camera;
    
    public DecodeCallback(Camera camera) {
      m_camera = camera;
    }

    public boolean handleMessage(Message msg) {
      if(decoded == false) {
        byte[] data = (byte[]) msg.obj;
        Size size = m_camera.getParameters().getPreviewSize();
        LuminanceSource src = new CameraLuminanceSource(data, size.width, size.height);
        Binarizer binarizer = new GlobalHistogramBinarizer(src);
        BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
        QRCodeReader qrCodeReader = new QRCodeReader();
        Result result = null;
        try {
          result = qrCodeReader.decode(binaryBitmap);
        } catch (NotFoundException e) {
          Log.e(TAG, "QRCode not found");
        } catch (ChecksumException e) {
          Log.e(TAG, "QRCode checksum exception");
        } catch (FormatException e) {
          Log.e(TAG, "QRCode format exception");
        }
        Message newMsg = Message.obtain();
        if(result != null) {
          decoded = true;
          newMsg.obj = "QRCode decoded";
        }
        else
          newMsg.obj = "QRCode not found";
        
        mainHandler.sendMessage(newMsg);
      }
      return false;
    }
    
  }

  public Handler getHandler() {
    return m_handler;
  }
  

}
