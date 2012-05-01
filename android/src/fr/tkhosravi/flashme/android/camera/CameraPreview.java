package fr.tkhosravi.flashme.android.camera;

import java.io.IOException;

import fr.tkhosravi.flashme.android.FlashYouActivity;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraPreview extends  SurfaceView implements SurfaceHolder.Callback{

  private SurfaceHolder m_surfaceHolder;
  private Camera m_camera;
  private Camera.PreviewCallback m_previewCallback;
  private DecodeThread decodeThread;

  private String TAG = "CameraPreview";

  public CameraPreview(Context context, Camera camera, DecodeThread thread) {
    super(context);
    m_camera = camera;

    decodeThread = thread;
    // Install a SurfaceHolder.Callback so we get notified when the
    // underlying surface is created and destroyed.
    m_surfaceHolder = getHolder();
    m_surfaceHolder.addCallback(this);
    // deprecated setting, but required on Android versions prior to 3.0
    m_surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

  }

  public void surfaceChanged(SurfaceHolder holder, int format, int width,
      int height) {
    // If your preview can change or rotate, take care of those events here.
    // Make sure to stop the preview before resizing or reformatting it.

    if (m_surfaceHolder.getSurface() == null){
      // preview surface does not exist
      return;
    }

    // stop preview before making changes
    try {
      m_camera.stopPreview();
    } catch (Exception e){
      // ignore: tried to stop a non-existent preview
    }

    // set preview size and make any resize, rotate or
    // reformatting changes here

    // start preview with new settings
    try {
      m_camera.setPreviewDisplay(m_surfaceHolder);
      m_camera.startPreview();

      m_previewCallback = new PreviewCallback(decodeThread);
      m_camera.setPreviewCallback(m_previewCallback);

    } catch (Exception e){
      Log.d(TAG, "Error starting camera preview: " + e.getMessage());
    }


  }

  public void surfaceCreated(SurfaceHolder holder) {
    // The Surface has been created, now tell the camera where to draw the preview.
    try {
      m_camera.setPreviewDisplay(holder);
      m_camera.startPreview();
    } catch (IOException e) {
      Log.d(TAG, "Error setting camera preview: " + e.getMessage());
    }

  }

  public void surfaceDestroyed(SurfaceHolder holder) {
    // empty. Take care of releasing the Camera preview in your activity.

  }

}
