package view;

import org.jfree.chart.ChartPanel;


import java.io.IOException;

import utility.Pixels;

public interface IHistogram {

  ChartPanel createRGBChart(Pixels properties);
}
