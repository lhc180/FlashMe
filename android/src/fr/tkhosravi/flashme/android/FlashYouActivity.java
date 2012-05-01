package fr.tkhosravi.flashme.android;

import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.facebook.android.FbDialog;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import fr.tkhosravi.flashme.R;
import fr.tkhosravi.flashme.android.camera.CameraPreview;
import fr.tkhosravi.flashme.android.camera.DecodeThread;

public class FlashYouActivity extends Activity {

  SharedPreferences m_prefs;
  String m_friendID;

  private static final String TAG = "FlashYouActivity";

  Facebook facebook = new Facebook("222242837882073");

  private Camera m_camera;
  private CameraPreview m_preview;
  private DecodeThread m_decodeThread;
  private TextView result;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.flashyou);

    // Create an instance of Camera
    m_camera = getCameraInstance();

    m_decodeThread = new DecodeThread(m_handler, m_camera);
    m_decodeThread.start();

    // Create our Preview view and set it as the content of our activity.
    m_preview = new CameraPreview(this, m_camera, m_decodeThread);
    
    FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
    preview.addView(m_preview);
    result = new TextView(this);
    result.setBackgroundColor(Color.TRANSPARENT);
    result.setTextColor(Color.RED);
    preview.addView(result);
  }

  @Override
  protected void onPause() {
    super.onPause();     
    releaseCamera();     // release the camera immediately on pause even
  }
  
  @Override
  protected void onResume() {
    super.onResume();
    //m_camera = getCameraInstance();
    
  }

  public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
    if (scanResult != null) {
      Log.d(TAG, "get contents");
      String result = scanResult.getContents();
      JSONObject json;
      try {
        Log.d(TAG, "get json from contents");
        json = new JSONObject(result);
        m_friendID = json.getString("id");
        m_prefs = getSharedPreferences(MainActivity.PREFS_NAME, MODE_PRIVATE);
        String access_token = "AAADKIPFn5NkBAPIsAF8lhNfcCeQeoTyZCQCdckeo4zRanfvlyCWDoZBKLMlNZAvBX0pPU3zULoARHYZAdQQntxXlXh2GC2YfDzKVYeHo0Hapn8AZBSWTS";//prefs.getString("access_token", null);
        facebook.setAccessToken(access_token);
        /*
         * Add a progress dialog?
         */
        String fbResult = facebook.request("me/friends");
        /*
         * Split the field data
         */
        json = new JSONObject(fbResult);
        JSONArray data = json.getJSONArray("data");
        boolean isFriend = false;
        String friendName = null;
        for(int i = 0; i < data.length(); ++i) {
          JSONObject field = data.getJSONObject(i);
          if(field.get("id") != null && field.getString("id").equals(m_friendID)) {
            isFriend = true;
            friendName = field.getString("name");
            break;
          }
        }
        Context context = getApplicationContext();
        CharSequence text;
        if(isFriend) {
          text = "You are already friend with " + friendName;
          Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
          toast.show();
        }
        else {
          String url = "http://www.facebook.com/dialog/friends?id=1432296467" +
              "&app_id=222242837882073&redirect_uri=http://www.facebook.com";

          FbDialog dialog = new FbDialog(this, url, new DialogListener() {

            public void onFacebookError(FacebookError arg0) {
              // TODO Auto-generated method stub

            }

            public void onError(DialogError arg0) {
              // TODO Auto-generated method stub

            }

            public void onComplete(Bundle arg0) {
              // TODO Auto-generated method stub

            }

            public void onCancel() {
              // TODO Auto-generated method stub

            }
          });
          dialog.show();
        }

      } catch (JSONException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();

        Log.e(TAG, "JSON Exception");
      } catch (MalformedURLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        Log.e(TAG, "Malformed URL");
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        Log.e(TAG, "IOException");
      }
    }
  }
  


  final private Handler m_handler = new Handler() {
    
    public void handleMessage(Message msg) {
      String s = (String) msg.obj;
      result.setText(s);
    }
  };
  
  public Handler getHandler() {
    return m_handler;
  }
  
  private Camera getCameraInstance() {
    Camera c = null;
    try {
      c = Camera.open(); // attempt to get a Camera instance
    }
    catch (Exception e){
      // Camera is not available (in use or does not exist)
    }
    return c; // returns null if camera is unavailable

  }

  private void releaseCamera(){
    if (m_camera != null){
      m_camera.release();        // release the camera for other applications
      m_camera = null;
    }
  }

}
