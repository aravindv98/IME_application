package control.cmd;
import control.*;
import model.*;

public class BrightenImage implements ImageManipulationsCmd {

  int increment;
  String sourceImageName;
  String destImageName;

  public BrightenImage(int increment,
    String sourceImageName,
    String destImageName) {
    this.increment = increment;
    this.sourceImageName = sourceImageName;
    this.destImageName = destImageName;
  }

  @Override
  public void go(NewImageManipulationsModel m) throws IllegalArgumentException {
    if (m == null){
      throw new IllegalArgumentException("Model not loaded!");
    }
    m.brighten(increment, sourceImageName, destImageName);
  }
}