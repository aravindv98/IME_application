package control.cmd;

import control.ImageManipulationsCmd;
import model.ImageManipulationsModel;

/**
 * Brighten image command.
 */
public class BrightenImage implements ImageManipulationsCmd {

  private final int increment;
  private final String sourceImageName;
  private final String destImageName;

  /**
   * Constructor for brighten image command.
   *
   * @param increment       Increment to be done.
   * @param sourceImageName Source Image name that needs to be brightened.
   * @param destImageName   Destination Image name that is obtained after brightened.
   */
  public BrightenImage(int increment,
                       String sourceImageName,
                       String destImageName) throws IllegalArgumentException {
    if (increment == 0 || sourceImageName.isEmpty() || destImageName.isEmpty()) {
      throw new IllegalArgumentException("Invalid arguments passed!");
    }

    this.increment = increment;
    this.sourceImageName = sourceImageName;
    this.destImageName = destImageName;
  }

  @Override
  public boolean go(ImageManipulationsModel m) throws IllegalArgumentException {
    m.brighten(increment, sourceImageName, destImageName);
    return true;
  }
}