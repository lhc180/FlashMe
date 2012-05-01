package fr.tkhosravi.flashme.android;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.widget.FrameLayout;
import android.widget.TextView;
import fr.tkhosravi.flashme.R;
import fr.tkhosravi.flashme.android.camera.CameraPreview;
import fr.tkhosravi.flashme.android.camera.DecodeThread;

public class FlashYouActivity extends Activity {

  SharedPreferences mPrefs;
  String mFriendID;

  private static final String TAG = "FlashYouActivity";

  private Camera mCamera;
  private CameraPreview mPreview;
  
  // TODO delete this
  private TextView result;
  
  private FlashYouActivityHandler mHandler;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.flashyou);

    // Create an instance of Camera
    mCamera = getCameraInstance();
    
    // Initialize handler
    mHandler = new FlashYouActivityHandler(this, mCamera);    

    // Create our Preview view and set it as the content of our activity.
    mPreview = new CameraPreview(this, mCamera, mHandler.getDecodeThread());
    
    FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
    preview.addView(mPreview);
  }

  @Override
  protected void onPause() {
    super.onPause();  
    mHandler.stopThread();
    releaseCamera();     // release the camera immediately on pause even
  }
  
  @Override
  protected void onResume() {
    super.onResume();
    mCamera.setPreviewCallback(null);
    mHandler.restartThread();
  }
  
  public Handler getHandler() {
    return mHandler;
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
    if (mCamera != null){
      mCamera.release();        // release the camera for other applications
      mCamera = null;
    }
  }

}
