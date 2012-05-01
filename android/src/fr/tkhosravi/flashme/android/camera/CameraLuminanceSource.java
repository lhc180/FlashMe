package fr.tkhosravi.flashme.android.camera;

import com.google.zxing.LuminanceSource;

public class CameraLuminanceSource extends LuminanceSource {
  
  private byte[] m_data;

  public CameraLuminanceSource(byte[] data, int width, int height) {
    super(width, height);
    m_data = data;
  }

  @Override
  public byte[] getMatrix() {

    return m_data;

  }

  @Override
  public byte[] getRow(int y, byte[] row) {
    if (y < 0 || y >= getHeight()) {
      throw new IllegalArgumentException("Requested row is outside the image: " + y);
    }
    int width = getWidth();
    if (row == null || row.length < width) {
      row = new byte[width];
    }
    int offset = y * width;
    System.arraycopy(m_data, offset, row, 0, width);
    return row;
  }

}
