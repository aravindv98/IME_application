package control.cmd;

import control.ImageManipulationsCmd;
import model.ImageManipulationsModel;

/**
 * Create greyscale image command.
 */
public class CreateGreyscale implements ImageManipulationsCmd {

  private final String componentType;
  private final String sourceImageName;
  private final String destinationImageName;

  /**
   * Constructor for create greyscale image command.
   *
   * @param componentType        Type of greyscaling to be done.
   * @param sourceImageName      Source Image name that needs to be greyscaled.
   * @param destinationImageName Destination Image name that is obtained after greyscaling.
   */
  public CreateGreyscale(String componentType,
                         String sourceImageName,
                         String destinationImageName) throws IllegalArgumentException {
    if (componentType.isEmpty() || sourceImageName.isEmpty() || destinationImageName.isEmpty()) {
      throw new IllegalArgumentException("Invalid arguments passed!");
    }

    this.componentType = componentType;
    this.sourceImageName = sourceImageName;
    this.destinationImageName = destinationImageName;
  }

  @Override
  public boolean execute(ImageManipulationsModel m) throws IllegalArgumentException {
    m.createGreyScale(componentType, sourceImageName, destinationImageName);
    return true;
  }
}