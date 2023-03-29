package control;

import model.ImageManipulationsModel;

/**
 * Command Interface working on the model object and supports different operations.
 */
public interface ImageManipulationsCmd {
  /**
   * This is the method for command line interface object where in the
   * respective object is created for a image processing function.
   *
   * @param m object of ImageManipulationsModel interface.
   * @return if the operation is successful or not.
   */
  boolean execute(ImageManipulationsModel m);
}
