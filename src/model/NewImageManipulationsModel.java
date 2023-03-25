package model;

public interface NewImageManipulationsModel extends ImageManipulationsModel {

  void sepia(String imageName,
                       String destinationImageName) throws IllegalArgumentException;

  void dither(String imageName,
             String destinationImageName) throws IllegalArgumentException;

  void blur(String imageName,
             String destinationImageName) throws IllegalArgumentException;

  void sharpen(String imageName,
             String destinationImageName) throws IllegalArgumentException;
}