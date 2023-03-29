package control;

import model.NewImageManipulationsModel;

/**
 * New command Interface working on the new model object and supports the new operations.
 */
public interface NewImageManipulationsCmd {

  /**
   * Can be extended by the new model functionalities.
   *
   * @param m model object supporting functionalities of new Image Manipulation operations.
   */
  boolean go(NewImageManipulationsModel m);
}