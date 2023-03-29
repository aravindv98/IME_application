package control.cmd;

import java.io.OutputStream;

import control.ImageManipulationsCmd;
import model.ImageManipulationsModel;

/**
 * Save of image command.
 */
public class SaveImage implements ImageManipulationsCmd {
  private final String imagePath;
  private final String imageName;
  private final OutputStream out;

  /**
   * Constructor for load image command.
   *
   * @param imagePath Image path where wwe need to save.
   * @param imageName Image name that has to be saved.
   * @param out OutputStream object used for printing exception information.
   */
  public SaveImage(String imagePath,
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
  public boolean execute(ImageManipulationsModel m) throws IllegalArgumentException {
    return m.saveImage(imagePath, imageName, out);
  }
}