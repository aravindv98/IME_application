package control.cmd;

import control.ImageManipulationsCmd;
import model.ImageManipulationsModel;

/**
 * RGB Combine of image command.
 */
public class RGBCombine implements ImageManipulationsCmd {
  String destinationImage;

  String redSourceImageName;
  String greenSourceImageName;
  String blueSourceImageName;

  /**
   * Constructor for rgb combine image command.
   *
   * @param destinationImage     Image name obtained after combine.
   * @param redSourceImageName   Red component of image name that needs to be combined.
   * @param greenSourceImageName Green component of image name that needs to be combined.
   * @param blueSourceImageName  Blue component of image name that needs to be combined.
   */
  public RGBCombine(String destinationImage,
                    String redSourceImageName,
                    String greenSourceImageName,
                    String blueSourceImageName) throws IllegalArgumentException {
    if (destinationImage.isEmpty() || redSourceImageName.isEmpty() ||
            greenSourceImageName.isEmpty() || blueSourceImageName.isEmpty()) {
      throw new IllegalArgumentException("Invalid arguments passed!");
    }

    this.destinationImage = destinationImage;
    this.redSourceImageName = redSourceImageName;
    this.greenSourceImageName = greenSourceImageName;
    this.blueSourceImageName = blueSourceImageName;
  }

  @Override
  public boolean execute(ImageManipulationsModel m) throws IllegalArgumentException {
    m.rgbCombine(destinationImage, redSourceImageName, greenSourceImageName,
            blueSourceImageName);
    return true;
  }
}
