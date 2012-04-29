package fr.tkhosravi.flashme.android.util;

/**
 * Implementation of an ImageConfig giving config for ImageWriter
 * Based on the MatrixToImageConfig class of zxing lib
 * @author tkhosravi
 *
 */
public class ImageConfig {

  public static final int BLACK = 0xFF000000;
  public static final int WHITE = 0xFFFFFFFF;
  
  private final int onColor;
  private final int offColor;

  /**
   * Creates a default config with on color {@link #BLACK} and off color {@link #WHITE}, generating normal
   * black-on-white barcodes.
   */
  public ImageConfig() {
    this(BLACK, WHITE);
  }

  /**
   * @param onColor pixel on color, specified as an ARGB value as an int
   * @param offColor pixel off color, specified as an ARGB value as an int
   */
  public ImageConfig(int onColor, int offColor) {
    this.onColor = onColor;
    this.offColor = offColor;
  }

  public int getPixelOnColor() {
    return onColor;
  }

  public int getPixelOffColor() {
    return offColor;
  }
}
