package model;

public interface IImageManipulationsModelFactory {
  public <T extends ImageManipulationsModel> T getModel(String fileName);
}
