package fr.tkhosravi.flashme.android;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import fr.tkhosravi.flashme.R;
import fr.tkhosravi.flashme.android.util.ImageWriter;

public class FlashMeActivity extends Activity {
  
  private String FILE_NAME = "myQRCode.jpeg";
  private ImageView qrcodeImage;
  private SharedPreferences prefs ;

  public void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.flashme);
    
    qrcodeImage = (ImageView) findViewById(R.id.qrcode_imageView);
    // If no pictures, display blank image
    boolean exist = false;
    for (String file : fileList()) {
      if(file.equals(FILE_NAME)) {
        exist = true;
        break;
      }
    }
    if(exist) {
      try {    
        FileInputStream fis = openFileInput(FILE_NAME);
        BitmapDrawable image = new BitmapDrawable(getResources(), fis);
        qrcodeImage.setImageDrawable(image);
      } catch (FileNotFoundException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        System.err.println("Error");
      }
    }

  }
  
  final Handler handler = new Handler(){
    public void handleMessage(Message msg) {
      String id = msg.getData().getString("response");
      QRCodeWriter qrCodeWriter = new QRCodeWriter();
      BitMatrix result = null;
      try {
        result = qrCodeWriter.encode(id, BarcodeFormat.QR_CODE, 200, 200);
      } catch (WriterException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      if(result != null) {
        FileOutputStream fos = null;
        try {
          fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
          ImageWriter.writeToStream(result, Bitmap.CompressFormat.JPEG, 100, fos);
          fos.close();

          FileInputStream fis = openFileInput(FILE_NAME);
          BitmapDrawable image = new BitmapDrawable(getResources(), fis);
          qrcodeImage.setImageDrawable(image);
          
        } catch (FileNotFoundException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
        
    }
  };
  
  @Override
  public Dialog onCreateDialog(int id) {
    Dialog box = null;
    switch(id) {
      case 0:
        box = new Dialog(this);
        box.setTitle("Error");
        break;
    }
    return box;
    
  }
  
  private class IdRequestListener implements RequestListener {
    
    private Handler idHandler;
    
    public IdRequestListener(Handler handler) {
      idHandler = handler;
    }

    public void onComplete(String response, Object arg1) {
      Message msg = new Message();
      Bundle data = new Bundle();
      data.putString("response", response);
      msg.setData(data);
      idHandler.sendMessage(msg);
    }

    public void onFacebookError(FacebookError arg0, Object arg1) {
      // TODO Auto-generated method stub
      
    }

    public void onFileNotFoundException(FileNotFoundException arg0, Object arg1) {
      // TODO Auto-generated method stub
      
    }

    public void onIOException(IOException arg0, Object arg1) {
      // TODO Auto-generated method stub
      
    }

    public void onMalformedURLException(MalformedURLException arg0, Object arg1) {
      // TODO Auto-generated method stub
      
    }
    
  }
  
  public void loadQRCodeButtonClick(View view) {
    Facebook facebook = new Facebook("222242837882073");
    prefs = getSharedPreferences(MainActivity.PREFS_NAME, MODE_PRIVATE);
    String access_token = prefs.getString("access_token", null);
    facebook.setAccessToken(access_token);
    AsyncFacebookRunner asyncRunner = new AsyncFacebookRunner(facebook);
    Bundle param = new Bundle();
    param.putString("fields", "id");
    asyncRunner.request("me", param, new IdRequestListener(handler));
    
  }
}
