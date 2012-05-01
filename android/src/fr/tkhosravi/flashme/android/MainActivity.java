package fr.tkhosravi.flashme.android;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

import fr.tkhosravi.flashme.R;

public class MainActivity extends Activity {

  //private Button flashmeButton, flashyouButton;

  private Facebook facebook = new Facebook("222242837882073");

  public static final String PREFS_NAME = "FlashMePrefs";
  private SharedPreferences prefs;


  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    //    flashmeButton = (Button) findViewById(R.id.flashme_button);
    //    flashyouButton = (Button) findViewById(R.id.flashyou_button);

    // Get existing access token if any
    prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
    String access_token = prefs.getString("access_token", null);
    long expires = prefs.getLong("access_expires", 0);
    if(access_token != null)
      facebook.setAccessToken(access_token);
    if(expires != 0)
      facebook.setAccessExpires(expires);

    // Authorize is called only if the access_token has expired
    if(!facebook.isSessionValid()) {

      facebook.authorize(this, new String[] {"user_about_me", "friends_about_me"}, new DialogListener() {

        public void onComplete(Bundle values) {
          SharedPreferences.Editor editor = prefs.edit();
          editor.putString("access_token", facebook.getAccessToken());
          editor.putLong("access_expires", facebook.getAccessExpires());
          editor.commit();

          Log.d("access_token", facebook.getAccessToken());

        }

        public void onFacebookError(FacebookError e) {
          // TODO Auto-generated method stub

        }

        public void onError(DialogError e) {
          // TODO Auto-generated method stub

        }

        public void onCancel() {
          // TODO Auto-generated method stub

        }


      });
    }
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    facebook.authorizeCallback(requestCode, resultCode, data);
  }

  public void flashmeButtonClick(View view) {
    Intent intent = new Intent(this, FlashMeActivity.class);
    startActivity(intent);
  }

  public void flashyouButtonClick(View view) {
    Intent intent = new Intent(this, FlashYouActivity.class);
    startActivity(intent);
  }



}
