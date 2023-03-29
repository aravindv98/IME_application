package control.cmd;

import control.ImageManipulationsCmd;
import model.ImageManipulationsModel;

/**
 * Vertical Flip of image command.
 */
public class VerticalFlip implements ImageManipulationsCmd {
  private final String sourceImageName;
  private final String destinationImageName;

  /**
   * Constructor for Vertical Flip of image command.
   *
   * @param sourceImageName      Source Image name that needs to be vertically flipped.
   * @param destinationImageName Destination Image name that is obtained after vertical flip.
   */
  public VerticalFlip(String sourceImageName,
                      String destinationImageName) throws IllegalArgumentException {
    if (sourceImageName.isEmpty() || destinationImageName.isEmpty()) {
      throw new IllegalArgumentException("Invalid arguments passed!");
    }

    this.sourceImageName = sourceImageName;
    this.destinationImageName = destinationImageName;
  }

  @Override
  public boolean execute(ImageManipulationsModel m) throws IllegalArgumentException {
    m.verticalFlip(sourceImageName, destinationImageName);
    return true;
  }
}
