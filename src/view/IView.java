package view;

import utility.Pixels;

import java.io.File;
import java.io.IOException;

public interface IView {
    void addFeatures(Features features);

    File getChosenFile();

    File getFilesToSave();

    void setImageInPanel(String images[], Pixels[] properties);

    void disableRGBSplitRadioButton();

    void enableRGBSplitRadioButton();

    void showLoadInfoMessage();

    void clearAllInputFields();
}