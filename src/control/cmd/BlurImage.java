package control.cmd;

import control.NewImageManipulationsCmd;
import model.NewImageManipulationsModel;

/**
 * Blur image command.
 */
public class BlurImage implements NewImageManipulationsCmd {

  private final String sourceImageName;
  private final String destImageName;

  /**
   * Constructor for blur image command.
   *
   * @param sourceImageName Source Image name that needs to be blurred.
   * @param destImageName   Destination Image name that is obtained after blurring.
   */
  public BlurImage(String sourceImageName,
                   String destImageName) throws IllegalArgumentException {
    if (sourceImageName.isEmpty() || destImageName.isEmpty()) {
      throw new IllegalArgumentException("Invalid arguments passed!");
    }

    this.sourceImageName = sourceImageName;
    this.destImageName = destImageName;
  }

  @Override
  public boolean go(NewImageManipulationsModel m) throws IllegalArgumentException {
    m.blur(sourceImageName, destImageName);
    return true;
  }
}