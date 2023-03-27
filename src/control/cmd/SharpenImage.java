package control.cmd;

import control.ImageManipulationsCmd;
import control.NewImageManipulationsCmd;
import model.NewImageManipulationsModel;

/**
 * Sharpen of image command.
 */
public class SharpenImage implements NewImageManipulationsCmd {

  private final String sourceImageName;
  private final String destImageName;

  /**
   * Constructor for Sharpening of image command.
   *
   * @param sourceImageName Source Image name that needs to be sharpened.
   * @param destImageName Destination Image name that is obtained after sharpening.
   */
  public SharpenImage(String sourceImageName,
                      String destImageName) throws IllegalArgumentException {
    if (sourceImageName.isEmpty() || destImageName.isEmpty()) {
      throw new IllegalArgumentException("Invalid arguments passed!");
    }
    this.sourceImageName = sourceImageName;
    this.destImageName = destImageName;
  }

  @Override
  public boolean go(NewImageManipulationsModel m) throws IllegalArgumentException {
    m.sharpen(sourceImageName, destImageName);
    return true;
  }
}