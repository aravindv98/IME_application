package control.cmd;

import control.ImageManipulationsCmd;
import model.ImageManipulationsModel;

/**
 * RGB Split of image command.
 */
public class RGBSplit implements ImageManipulationsCmd {
  private final String imageName;
  private final String redDestinationImageName;
  private final String greenDestinationImageName;
  private final String blueDestinationImageName;

  /**
   * Constructor for rgb split image command.
   *
   * @param imageName                 Image name that needs to be split.
   * @param redDestinationImageName   Image name which after splitting red component.
   * @param greenDestinationImageName Image name which after splitting green component.
   * @param blueDestinationImageName  Image name which after splitting blue component.
   */
  public RGBSplit(String imageName,
                  String redDestinationImageName,
                  String greenDestinationImageName,
                  String blueDestinationImageName) throws IllegalArgumentException {
    if (redDestinationImageName.isEmpty() || greenDestinationImageName.isEmpty() ||
            blueDestinationImageName.isEmpty() || imageName.isEmpty()) {
      throw new IllegalArgumentException("Invalid arguments passed!");
    }

    this.imageName = imageName;
    this.redDestinationImageName = redDestinationImageName;
    this.greenDestinationImageName = greenDestinationImageName;
    this.blueDestinationImageName = blueDestinationImageName;
  }

  @Override
  public boolean go(ImageManipulationsModel m) throws IllegalArgumentException {
    m.rgbSplit(imageName, redDestinationImageName, greenDestinationImageName,
            blueDestinationImageName);
    return true;
  }
}
