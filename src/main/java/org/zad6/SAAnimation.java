package org.zad6;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;

public class SAAnimation {

    private static XYSeries functionSeries;
    private static XYSeries saPathSeries;
    private static String function;

    public SAAnimation(String inputed_function) {
        function = inputed_function;
        SwingUtilities.invokeLater(() -> createAndShowGui(function));
    }

    public static void main(String[] args) {}

    private static void createAndShowGui(String function) {
        functionSeries = new XYSeries("Minimum");
        saPathSeries = new XYSeries("SA Path");

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(functionSeries);
        dataset.addSeries(saPathSeries);

        JFreeChart chart = ChartFactory.createScatterPlot(
                "Simulated Annealing Animation",
                "X",
                "Y",
                dataset
        );

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setRenderer(1, new XYLineAndShapeRenderer());
        plot.getRenderer(1).setSeriesPaint(0, Color.RED);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 600));
        chartPanel.setMouseWheelEnabled(true);

        JFrame frame = new JFrame("Simulated Annealing Animation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(chartPanel, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);

        // Run the algorithm in a separate thread to avoid freezing the UI
        new Thread(() -> new SimulatedAnnealing(function, functionSeries, saPathSeries)).start();
    }

}
