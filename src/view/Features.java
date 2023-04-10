package view;

import java.io.File;

/**
 * An interface that would contain methods that would implement
 * the functionalities of an image used by the controller.
 */
public interface Features {

  /**
   * Used by the controller to load an image into the view
   * after performing operations by the model.
   */
  void loadImage();

  /**
   * Used by the controller to load an image into the view
   * after performing operations by the model. This function is also used bu the reload functionality.
   *
   * @param f File that needs to be loaded.
   */
  void load(File f);

  /**
   * Used by the controller to save an image into the view
   * after performing operations by the model.
   *
   * @param sourceImages that need to be saved.
   */
  void saveImage(String[] sourceImages);

  /**
   * Used by the controller to perform sepia of an image
   * into the view after performing operations by the model.
   *
   * @param sourceImages that need to be operated on.
   */
  void sepia(String[] sourceImages);

  /**
   * Used by the controller to perform dither of an image
   * into the view after performing operations by the model.
   *
   * @param sourceImages that need to be operated on.
   */
  void dither(String[] sourceImages);

  /**
   * Used by the controller to perform blur of an image
   * into the view after performing operations by the model.
   *
   * @param sourceImages that need to be operated on.
   */
  void blur(String[] sourceImages);

  /**
   * Used by the controller to perform sharp image
   * into the view after performing operations by the model.
   *
   * @param sourceImages that need to be operated on.
   */
  void sharpen(String[] sourceImages);

  /**
   * Used by the controller to perform horizontal-flip of an image
   * into the view after performing operations by the model.
   *
   * @param sourceImages that need to be operated on.
   */
  void horizontalFlip(String[] sourceImages);

  /**
   * Used by the controller to perform vertical-flip of an image
   * into the view after performing operations by the model.
   *
   * @param sourceImages that need to be operated on.
   */
  void verticalFlip(String[] sourceImages);

  /**
   * Used by the controller to perform rgbSplit of an image
   * into the view after performing operations by the model.
   *
   * @param sourceImages that need to be operated on.
   */
  void rgbSplit(String[] sourceImages);

  /**
   * Used by the controller to perform rgbCombine of an image
   * into the view after performing operations by the model.
   *
   * @param sourceImages that need to be operated on.
   */
  void rgbCombine(String[] sourceImages);

  /**
   * Used by the controller to perform brighten of an image
   * into the view after performing operations by the model.
   *
   * @param sourceImages that need to be operated on.
   */
  void brightenImage(int brightnessFactor, String[] sourceImages);

  /**
   * Used by the controller to perform greyscale of an image
   * into the view after performing operations by the model.
   *
   * @param sourceImages  that need to be operated on.
   * @param componentType the component type.
   */
  void greyscaleImage(String componentType, String[] sourceImages);

  /**
   * Used by the controller to perform greyscale of an image
   * into the view after performing operations by the model.
   *
   * @param sourceImages that need to be operated on.
   */
  void createGreyscale(String[] sourceImages);
}
