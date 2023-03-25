package control.cmd;
import control.*;
import model.*;

public class CreateGreyscale implements ImageManipulationsCmd {

  String componentType;
  String sourceImageName;
  String destinationImageName;

  public CreateGreyscale(String componentType,
                       String sourceImageName,
                       String destinationImageName) {
    this.componentType = componentType;
    this.sourceImageName = sourceImageName;
    this.destinationImageName = destinationImageName;
  }

  @Override
  public void go(NewImageManipulationsModel m) throws IllegalArgumentException {
    if (m == null){
      throw new IllegalArgumentException("Model not loaded!");
    }
    m.createGreyScale(componentType, sourceImageName, destinationImageName);
  }
}