package control.cmd;

import control.NewImageManipulationsCmd;
import model.NewImageManipulationsModel;

/**
 * Command to create sepia of image.
 */
public class SepiaImage implements NewImageManipulationsCmd {

  private final String sourceImageName;
  private final String destImageName;

  /**
   * Constructor for Sepia image command.
   *
   * @param sourceImageName Source Image name that needs to be changed to sepia.
   * @param destImageName   Destination Image name that is obtained after sepia.
   */
  public SepiaImage(String sourceImageName,
                    String destImageName) throws IllegalArgumentException {
    if (sourceImageName.isEmpty() || destImageName.isEmpty()) {
      throw new IllegalArgumentException("Invalid arguments passed!");
    }
    this.sourceImageName = sourceImageName;
    this.destImageName = destImageName;
  }

  @Override
  public boolean execute(NewImageManipulationsModel m) throws IllegalArgumentException {
    m.sepia(sourceImageName, destImageName);
    return true;
  }
}
