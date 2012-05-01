package fr.tkhosravi.flashme.android;

import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.facebook.android.FbDialog;

import fr.tkhosravi.flashme.R;
import fr.tkhosravi.flashme.android.camera.DecodeThread;

public class FlashYouActivityHandler extends Handler {

  private FlashYouActivity mActivity;
  private DecodeThread mDecodeThread;
  private Camera mCamera;
  
  private SharedPreferences mPrefs;

  final private String TAG = "FlashYouActivityHandler";

  private Facebook mFacebook = new Facebook("222242837882073");

  public FlashYouActivityHandler(FlashYouActivity flashYouActivity, Camera camera) {
    mActivity = flashYouActivity;
    
    mCamera = camera;

    // Launch the decode thread instance now
    mDecodeThread = new DecodeThread(this, mCamera);
    mDecodeThread.start();
  }
  
  public DecodeThread getDecodeThread() {
    return mDecodeThread;
  }

  public void handleMessage(Message msg) {
    String result = (String) msg.obj;
    //FBRequest

    if (result != null) {
      Log.d(TAG, "get contents");
      JSONObject json;
      try {
        Log.d(TAG, "get json from contents");
        json = new JSONObject(result);
        String m_friendID = json.getString("id");
        mPrefs = mActivity.getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);
        String access_token = mPrefs.getString("access_token", null);
        mFacebook.setAccessToken(access_token);
        /*
         * Add a progress dialog?
         */
        String fbResult = mFacebook.request("me/friends");
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

        CharSequence text;
        if(isFriend) {
          text = "You are already friend with " + friendName;
          Toast toast = Toast.makeText(mActivity.getApplicationContext(), text, Toast.LENGTH_LONG);
          toast.show();
          msg = Message.obtain(mDecodeThread.getHandler(), R.integer.reset);
          msg.sendToTarget();
        }
        else {
          String url = "http://www.facebook.com/dialog/friends?id=" + m_friendID +
              "&app_id=222242837882073&redirect_uri=http://www.facebook.com";

          FbDialog dialog = new FbDialog(mActivity, url, new DialogListener() {

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
  
  public void stopThread() {
    if(mDecodeThread.isAlive())
      mDecodeThread.getHandler().sendEmptyMessage(R.integer.stop);
  }

  public void restartThread() {
    if(!mDecodeThread.isAlive())
      mDecodeThread.start();
  }
}
