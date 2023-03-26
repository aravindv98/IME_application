package control.cmd;

import control.ImageManipulationsCmd;
import model.NewImageManipulationsModel;

public class SepiaImage implements ImageManipulationsCmd {

  String sourceImageName;
  String destImageName;

  public SepiaImage(String sourceImageName,
                   String destImageName) {
    this.sourceImageName = sourceImageName;
    this.destImageName = destImageName;
  }

  @Override
  public boolean go(NewImageManipulationsModel m) throws IllegalArgumentException {
    if (m == null){
      throw new IllegalArgumentException("Model not loaded!");
    }
    m.sepia(sourceImageName, destImageName);
    return true;
  }
}
