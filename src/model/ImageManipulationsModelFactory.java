package model;

public class ImageManipulationsModelFactory implements IImageManipulationsModelFactory {

  @Override
  public NewImageManipulationsModel getModel(String fileName) {
    String[] tokens = fileName.split("\\.(?=[^\\.]+$)");
    if (tokens[1].equalsIgnoreCase("ppm")) {
      return PPMImageManipulationsModel.getInstance();
    }
    else {
      return ConventionalImageManipulationsModel.getInstance();
    }
  }
}