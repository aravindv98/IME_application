package model;

/**
 * This interface represents new methods for various image manipulations.
 */
public interface NewImageManipulationsModel extends ImageManipulationsModel {

  /**
   * Creates a sepia that has a characteristic red brown tone.
   *
   * @param imageName            the name of the image that we need to create a sepia out of.
   * @param destinationImageName the name of the image after creating sepia.
   */
  void sepia(String imageName,
                       String destinationImageName) throws IllegalArgumentException;

  /**
   * Creates a dither image.
   *
   * @param imageName            the name of the image that we need to create a dither out of.
   * @param destinationImageName the name of the image after creating dither.
   */
  void dither(String imageName,
             String destinationImageName) throws IllegalArgumentException;

  /**
   * Creates a greyscale image.
   *
   * @param imageName            the name of the image that we need to create a greyscale out of.
   * @param destinationImageName the name of the image after creating greyscale.
   */
  void createGreyScale(String imageName,
                       String destinationImageName) throws IllegalArgumentException;

  /**
   * Creates a blur image.
   *
   * @param imageName            the name of the image that we need to create a blur out of.
   * @param destinationImageName the name of the image after creating blur.
   */
  void blur(String imageName,
            String destinationImageName) throws IllegalArgumentException;

  /**
   * Creates a sharpened image.
   *
   * @param imageName            the name of the image that we need to create a sharpened
   *                             image out of.
   * @param destinationImageName the name of the image after creating sharpened image.
   */
  void sharpen(String imageName,
             String destinationImageName) throws IllegalArgumentException;
}