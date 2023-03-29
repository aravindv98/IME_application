package control.cmd;

import control.NewImageManipulationsCmd;
import model.NewImageManipulationsModel;

/**
 * Dither image command.
 */
public class DitherImage implements NewImageManipulationsCmd {

  private final String sourceImageName;
  private final String destImageName;

  /**
   * Constructor for dither image command.
   *
   * @param sourceImageName Source Image name that needs to be dithered.
   * @param destImageName   Destination Image name that is obtained after dithering.
   */
  public DitherImage(String sourceImageName,
                     String destImageName) throws IllegalArgumentException {
    if (sourceImageName.isEmpty() || destImageName.isEmpty()) {
      throw new IllegalArgumentException("Invalid arguments passed!");
    }

    this.sourceImageName = sourceImageName;
    this.destImageName = destImageName;
  }

  @Override
  public boolean execute(NewImageManipulationsModel m) throws IllegalArgumentException {
    m.dither(sourceImageName, destImageName);
    return true;
  }
}