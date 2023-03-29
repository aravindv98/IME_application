package model;

/**
 * Factory interface for returning the model object based on extension.
 */
public interface IImageManipulationsModelFactory {

  /**
   * Factory method for returning the model object based on extension.
   *
   * @param fileExtension name of the file along with extension.
   */
  <T extends ImageManipulationsModel> T getModel(String fileExtension);
}
