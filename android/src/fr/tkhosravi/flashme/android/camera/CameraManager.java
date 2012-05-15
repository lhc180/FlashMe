package fr.tkhosravi.flashme.android.camera;

import fr.tkhosravi.flashme.R;
import android.app.Activity;
import android.hardware.Camera;
import android.os.Handler;
import android.util.Log;
import android.widget.FrameLayout;

public class CameraManager {
  
  private Camera mCamera;
  private Activity mActivity;
  private CameraPreview mPreview;
  private CameraPreviewCallback mPreviewCallback;
  
  public CameraManager(Activity activity) {
    mActivity = activity;
    mPreview = new CameraPreview(activity);
    mPreviewCallback = new CameraPreviewCallback();
    
    FrameLayout preview = (FrameLayout) activity.findViewById(R.id.camera_preview);
    preview.addView(mPreview);
  }

  public void initCamera() {
    safeCameraOpen();
    mPreview.setCamera(mCamera);
  }
  
  public void stopCamera() {
    releaseCameraAndPreview();
  }
  
  public Camera getCamera() {
    return mCamera;
  }

  private boolean safeCameraOpen() {
    boolean qOpened = false;

    try {
      releaseCameraAndPreview();
      mCamera = Camera.open();
      qOpened = (mCamera != null);
    } catch (Exception e) {
      Log.e(mActivity.getString(R.string.app_name), "failed to open Camera");
      e.printStackTrace();
    }

    return qOpened;    
  }

  private void releaseCameraAndPreview() {
    mPreview.setCamera(null);
    if (mCamera != null) {
      mCamera.release();
      mCamera = null;
    }
  }

  public synchronized void startPreviewAndDecode(Handler handler) {
    mPreviewCallback.setHandler(handler);
    mCamera.setPreviewCallback(mPreviewCallback);
  }
  
  public synchronized void stopPreviewAndDecode(){
    mCamera.stopPreview();
    mPreviewCallback.setHandler(null);
  }

}
