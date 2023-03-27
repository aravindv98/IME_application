package control.cmd;

import java.io.OutputStream;

import control.ImageManipulationsCmd;
import model.ImageManipulationsModel;

/**
 * Load image command.
 */
public class LoadImage implements ImageManipulationsCmd {
  String imagePath;
  String imageName;
  OutputStream out;

  /**
   * Constructor for load image command.
   *
   * @param imagePath Image path from where to load.
   * @param imageName Image name which can be used to refer to the image in the future.
   * @param out OutputStream object used for printing exception information.
   */
  public LoadImage(String imagePath,
                   String imageName,
                   OutputStream out) throws IllegalArgumentException {
    if (imagePath.isEmpty() || imageName.isEmpty() || out == null) {
      throw new IllegalArgumentException("Invalid arguments passed!");
    }
    this.imagePath = imagePath;
    this.imageName = imageName;
    this.out = out;
  }

  @Override
  public boolean go(ImageManipulationsModel m) throws IllegalArgumentException {
    return m.loadImage(imagePath, imageName, out);
  }
}
