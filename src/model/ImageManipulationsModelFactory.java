package model;

/**
 * Factory class for returning the model object based on extension.
 */
public class ImageManipulationsModelFactory implements IImageManipulationsModelFactory {

  @Override
  public <T extends ImageManipulationsModel> T getModel(String fileName) {
    String[] tokens = fileName.split("\\.(?=[^\\.]+$)");
    if (tokens[1].equalsIgnoreCase("ppm")) {
      return (T) PPMImageManipulationsModel.getInstance();
    }
    else {
      return (T) ConventionalImageManipulationsModel.getInstance();
    }
  }
}