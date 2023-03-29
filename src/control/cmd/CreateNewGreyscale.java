package control.cmd;

import control.NewImageManipulationsCmd;
import model.NewImageManipulationsModel;

/**
 * Create new greyscale image command.
 */
public class CreateNewGreyscale implements NewImageManipulationsCmd {

  private final String sourceImageName;
  private final String destinationImageName;

  /**
   * Constructor for create greyscale image command.
   *
   * @param sourceImageName      Source Image name that needs to be greyscaled.
   * @param destinationImageName Destination Image name that is obtained after greyscaling.
   */
  public CreateNewGreyscale(String sourceImageName,
                            String destinationImageName) throws IllegalArgumentException {
    if (sourceImageName.isEmpty() || destinationImageName.isEmpty()) {
      throw new IllegalArgumentException("Invalid arguments passed!");
    }

    this.sourceImageName = sourceImageName;
    this.destinationImageName = destinationImageName;
  }

  @Override
  public boolean go(NewImageManipulationsModel m) throws IllegalArgumentException {
    m.createGreyScale(sourceImageName, destinationImageName);
    return true;
  }
}