package fr.tkhosravi.flashme.android.util;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;

import com.google.zxing.common.BitMatrix;

/**
 * Implementation of an ImageWriter converting a BitMatrix to Bitmap
 * Based on the MatrixToImageWriter class of zxing lib
 * @author tkhosravi
 *
 */
public class ImageWriter {

  private static final ImageConfig DEFAULT_CONFIG = new ImageConfig();

  private ImageWriter() {}

  /**
   * Renders a {@link BitMatrix} as an image, where "false" bits are rendered
   * as white, and "true" bits are rendered as black.
   */
  public static Bitmap toBitmapImage(BitMatrix matrix) {
    return toBitmapImage(matrix, DEFAULT_CONFIG);
  }

  /**
   * As {@link #toBitmapImage(BitMatrix)}, but allows customization of the output.
   */
  public static Bitmap toBitmapImage(BitMatrix matrix, ImageConfig config) {
    int width = matrix.getWidth();
    int height = matrix.getHeight();
    Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
    int onColor = config.getPixelOnColor();
    int offColor = config.getPixelOffColor();
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        bmp.setPixel(x, y, matrix.get(x, y) ? onColor : offColor);
      }
    }
    return bmp;
  }

  /**
   * Writes a {@link BitMatrix} to a stream.
   * @param quality TODO
   *
   * @see #toBitmapImage(BitMatrix)
   */
  public static void writeToStream(BitMatrix matrix, CompressFormat format, int quality, OutputStream stream) throws IOException {
    writeToStream(matrix, format, stream, quality, DEFAULT_CONFIG);
  }

  /**
   * As {@link #writeToStream(BitMatrix, CompressFormat, int, OutputStream)}, but allows customization of the output.
   * @param quality TODO
   */
  public static void writeToStream(BitMatrix matrix, CompressFormat format, OutputStream stream, int quality, ImageConfig config) 
      throws IOException {  
    Bitmap image = toBitmapImage(matrix, config);
    if (!image.compress(format, quality, stream)) {
      throw new IOException("Could not write an image of format " + format);
    }
  }
}
