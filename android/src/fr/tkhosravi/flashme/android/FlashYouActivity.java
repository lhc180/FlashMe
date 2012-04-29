package fr.tkhosravi.flashme.android;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import fr.tkhosravi.flashme.R;
import fr.tkhosravi.flashme.android.FlashMeActivity.IdRequestListener;

public class FlashYouActivity extends Activity {
  
  TextView text;
  SharedPreferences prefs;
  

  Facebook facebook = new Facebook("222242837882073");

  public void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.flashyou);
    text = (TextView) findViewById(R.id.test);
  }
  
  public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
    if (scanResult != null) {
      String result = scanResult.getContents();
      JSONObject json = new JSONObject(result);
      String friendID = json.getString("id");
      text.setText(friendID);
      prefs = getSharedPreferences(MainActivity.PREFS_NAME, MODE_PRIVATE);
      String access_token = prefs.getString("access_token", null);
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
      for(int i = 0; i < data.length(); ++i) {
        JSONObject field = data.getJSONObject(i);
      }

      
    }
  }
  
  public void barcodeScannerLauncherButtonClick(View view) {
    IntentIntegrator integrator = new IntentIntegrator(this);
    integrator.initiateScan();
  }
}
