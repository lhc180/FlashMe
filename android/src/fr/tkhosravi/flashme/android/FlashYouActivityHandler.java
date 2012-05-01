package fr.tkhosravi.flashme.android;

import android.os.Handler;
import android.os.Message;

public class FlashYouActivityHandler extends Handler {
  
  private FlashYouActivity mActivity;

  public FlashYouActivityHandler(FlashYouActivity flashYouActivity) {
    mActivity = flashYouActivity;
  }

  public void handleMessage(Message msg) {
    String s = (String) msg.obj;
    //FBRequest
    
//  IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
//  if (scanResult != null) {
//    Log.d(TAG, "get contents");
//    String result = scanResult.getContents();
//    JSONObject json;
//    try {
//      Log.d(TAG, "get json from contents");
//      json = new JSONObject(result);
//      m_friendID = json.getString("id");
//      m_prefs = getSharedPreferences(MainActivity.PREFS_NAME, MODE_PRIVATE);
//      String access_token = "AAADKIPFn5NkBAPIsAF8lhNfcCeQeoTyZCQCdckeo4zRanfvlyCWDoZBKLMlNZAvBX0pPU3zULoARHYZAdQQntxXlXh2GC2YfDzKVYeHo0Hapn8AZBSWTS";//prefs.getString("access_token", null);
//      facebook.setAccessToken(access_token);
//      /*
//       * Add a progress dialog?
//       */
//      String fbResult = facebook.request("me/friends");
//      /*
//       * Split the field data
//       */
//      json = new JSONObject(fbResult);
//      JSONArray data = json.getJSONArray("data");
//      boolean isFriend = false;
//      String friendName = null;
//      for(int i = 0; i < data.length(); ++i) {
//        JSONObject field = data.getJSONObject(i);
//        if(field.get("id") != null && field.getString("id").equals(m_friendID)) {
//          isFriend = true;
//          friendName = field.getString("name");
//          break;
//        }
//      }
//      Context context = getApplicationContext();
//      CharSequence text;
//      if(isFriend) {
//        text = "You are already friend with " + friendName;
//        Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
//        toast.show();
//      }
//      else {
//        String url = "http://www.facebook.com/dialog/friends?id=1432296467" +
//            "&app_id=222242837882073&redirect_uri=http://www.facebook.com";
//
//        FbDialog dialog = new FbDialog(this, url, new DialogListener() {
//
//          public void onFacebookError(FacebookError arg0) {
//            // TODO Auto-generated method stub
//
//          }
//
//          public void onError(DialogError arg0) {
//            // TODO Auto-generated method stub
//
//          }
//
//          public void onComplete(Bundle arg0) {
//            // TODO Auto-generated method stub
//
//          }
//
//          public void onCancel() {
//            // TODO Auto-generated method stub
//
//          }
//        });
//        dialog.show();
//      }
//
//    } catch (JSONException e) {
//      // TODO Auto-generated catch block
//      e.printStackTrace();
//
//      Log.e(TAG, "JSON Exception");
//    } catch (MalformedURLException e) {
//      // TODO Auto-generated catch block
//      e.printStackTrace();
//      Log.e(TAG, "Malformed URL");
//    } catch (IOException e) {
//      // TODO Auto-generated catch block
//      e.printStackTrace();
//      Log.e(TAG, "IOException");
//    }
//  }
    //result.setText(s);
  }
}
