package control.cmd;
import control.*;
import model.*;

public class SharpenImage implements ImageManipulationsCmd {

  String sourceImageName;
  String destImageName;

  public SharpenImage(String sourceImageName,
                   String destImageName) {
    this.sourceImageName = sourceImageName;
    this.destImageName = destImageName;
  }

  @Override
  public void go(NewImageManipulationsModel m) throws IllegalArgumentException {
    if (m == null){
      throw new IllegalArgumentException("Model not loaded!");
    }
    m.sharpen(sourceImageName, destImageName);
  }
}