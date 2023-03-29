package control.cmd;

import control.ImageManipulationsCmd;
import model.ImageManipulationsModel;

/**
 * Horizontal Flip of image command.
 */
public class HorizontalFlip implements ImageManipulationsCmd {
  private final String sourceImageName;
  private final String destinationImageName;

  /**
   * Constructor for Horizontal Flip of image command.
   *
   * @param sourceImageName      Source Image name that needs to be horizontally flipped.
   * @param destinationImageName Destination Image name that is obtained after horizontal flip.
   */
  public HorizontalFlip(String sourceImageName,
                        String destinationImageName) throws IllegalArgumentException {
    if (sourceImageName.isEmpty() || destinationImageName.isEmpty()) {
      throw new IllegalArgumentException("Invalid arguments passed!");
    }

    this.sourceImageName = sourceImageName;
    this.destinationImageName = destinationImageName;
  }

  @Override
  public boolean go(ImageManipulationsModel m) throws IllegalArgumentException {
    m.horizontalFlip(sourceImageName, destinationImageName);
    return true;
  }
}
