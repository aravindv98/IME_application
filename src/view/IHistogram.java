package view;

import org.jfree.chart.ChartPanel;



import utility.Pixels;

public interface IHistogram {

  ChartPanel createRGBChart(Pixels properties);
}
