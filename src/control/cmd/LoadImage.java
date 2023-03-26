package control.cmd;
import java.io.OutputStream;

import control.*;
import model.*;

public class LoadImage implements ImageManipulationsCmd {
  String imagePath;
  String imageName;
  OutputStream out;

  public LoadImage(String imagePath,
                   String imageName,
                   OutputStream out) {
    this.imagePath = imagePath;
    this.imageName = imageName;
    this.out = out;
  }

  @Override
  public boolean go(NewImageManipulationsModel m) throws IllegalArgumentException {
    if (m == null){
      throw new IllegalArgumentException("Model not loaded!");
    }
    return m.loadImage(imagePath, imageName, out);
  }
}
