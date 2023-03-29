package model;

import java.io.IOException;
import java.io.OutputStream;

/**
 * This interface represents methods for various image manipulations.
 */
public interface ImageManipulationsModel {

  /**
   * Load an image from the specified path and refer it to henceforth in the program
   * by the given image name.
   *
   * @param imagePath the path of the file from which we want to load the data.
   * @param imageName the name used thereafter to refer to the image in the program in the future.
   * @param out       this is used to get the exception message if file is not found.
   * @return true if load is successful.
   */
  boolean loadImage(String imagePath, String imageName, OutputStream out)
          throws IllegalArgumentException;

  /**
   * Save the image with the given name to the specified path
   * which should include the name of the file.
   *
   * @param imagePath the path of the file where it will be saved.
   * @param imageName the name used thereafter to refer to the image in the program in the future.
   * @param out       this is used to get the exception message if file is not found.
   * @return true if save is successful.
   */
  boolean saveImage(String imagePath, String imageName, OutputStream out)
          throws IllegalArgumentException;

  /**
   * Create a greyscale image with the red-component of the image with the given name,
   * and refer to it henceforth in the program by the given destination name.
   * Similar commands for green, blue, value, luma, intensity components are supported.
   *
   * @param componentType        the component with which to create the greyscale image.
   * @param imageName            the name of the image that we need to create a greyscale out of.
   * @param destinationImageName the name of the image after creating greyscale.
   */
  void createGreyScale(String componentType,
                       String imageName,
                       String destinationImageName) throws IllegalArgumentException;

  /**
   * Flip an image horizontally to create a new image,
   * referred to henceforth by the given destination name.
   *
   * @param imageName            the name of the image that we need to flip horizontally.
   * @param destinationImageName the new image name after horizontal flip,
   *                             used thereafter to refer it in the future.
   */
  void horizontalFlip(String imageName, String destinationImageName)
          throws IllegalArgumentException;

  /**
   * Flip an image vertically to create a new image,
   * referred to henceforth by the given destination name.
   *
   * @param imageName            the name of the image that we need to flip vertically.
   * @param destinationImageName the new image name after vertical flip,
   *                             used thereafter to refer it in the future.
   */
  void verticalFlip(String imageName, String destinationImageName)
          throws IllegalArgumentException;

  /**
   * Brighten the image by the given increment to create a new image,
   * referred to henceforth by the given destination name.
   * The increment may be positive (brightening) or negative (darkening).
   *
   * @param imageName            the name of the image that we need to brighten.
   * @param destinationImageName the new image name after brightening,
   *                             used thereafter to refer it in the future.
   */
  void brighten(int increment, String imageName, String destinationImageName)
          throws IllegalArgumentException;

  /**
   * Split the given image into three greyscale images containing its red, green and blue
   * components respectively.
   *
   * @param imageName                 the name of the image that we need to split.
   * @param redDestinationImageName   the new image name after splitting the red component,
   *                                  used thereafter to refer it in the future.
   * @param greenDestinationImageName the new image name after splitting the green component,
   *                                  used thereafter to refer it in the future.
   * @param blueDestinationImageName  the new image name after splitting the blue component,
   *                                  used thereafter to refer it in the future.
   */
  void rgbSplit(String imageName, String redDestinationImageName,
                String greenDestinationImageName, String blueDestinationImageName)
          throws IllegalArgumentException;

  /**
   * Combines three greyscale images having red, green, blue components
   * into a single greyscale image.
   *
   * @param destinationImageName the name of the image obtained after combining.
   * @param redSourceImageName   the grey scale image name with red component.
   * @param greenSourceImageName the grey scale image name with green component.
   * @param blueSourceImageName  the grey scale image name with blue component.
   */
  void rgbCombine(String destinationImageName, String redSourceImageName,
                  String greenSourceImageName, String blueSourceImageName)
          throws IllegalArgumentException;

  /**
   * Gets the width, height, maximum Value, listOfPixels as a String for an Image.
   *
   * @param imageName the name of the image to obtain properties.
   */
  String getImageProperties(String imageName);
}