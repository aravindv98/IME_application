package control;

import model.ImageManipulationsModel;

/**
 * Command Interface working on the model object and supports different operations.
 */
public interface ImageManipulationsCmd {
  boolean go(ImageManipulationsModel m);
}
