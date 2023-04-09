package view;

import org.jfree.chart.ChartPanel;


import utility.Pixels;

/**
 * An interface that would contain methods that would implement
 * the functionalities of a histogram chart.
 */
public interface IHistogram {
  /**
   * A method to generate the Line Chart that would depict the
   * frequencies of all RGB components in an image along with the
   * intensity component.
   *
   * @param properties pixel properties of an image.
   * @return the chart depicting the histogram.
   */
  ChartPanel createRGBChart(Pixels properties);
}