package control.cmd;
import java.io.OutputStream;

import control.*;
import model.*;

public class SaveImage implements ImageManipulationsCmd {
  String imagePath;
  String imageName;
  OutputStream out;

  public SaveImage(String imagePath,
                   String imageName,
                   OutputStream out) {
    this.imagePath = imagePath;
    this.imageName = imageName;
    this.out = out;
  }

  @Override
  public void go(NewImageManipulationsModel m) throws IllegalArgumentException {
    if (m == null){
      throw new IllegalArgumentException("Model not loaded!");
    }
    m.saveImage(imagePath, imageName, out);
  }
}