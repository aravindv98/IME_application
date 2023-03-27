package model;

public interface IImageManipulationsModelFactory {
  <T extends ImageManipulationsModel> T getModel(String fileName);
}
