package control.cmd;
import control.NewImageManipulationsCmd;
import model.*;

public class SharpenImage implements NewImageManipulationsCmd {

  String sourceImageName;
  String destImageName;

  public SharpenImage(String sourceImageName,
                   String destImageName) {
    this.sourceImageName = sourceImageName;
    this.destImageName = destImageName;
  }

  @Override
  public boolean go(NewImageManipulationsModel m) throws IllegalArgumentException {
    if (m == null){
      throw new IllegalArgumentException("Model not loaded!");
    }
    m.sharpen(sourceImageName, destImageName);
    return true;
  }
}