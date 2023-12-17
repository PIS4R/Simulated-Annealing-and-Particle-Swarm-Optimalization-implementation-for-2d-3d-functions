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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class PSOAnimation {

    private static XYSeries functionSeries;
    private static List<ParticleSwarm.Particle> particles;
    private static String function;
    private static final Random random = new Random();
    private static final int SWARM_SIZE = 30;//30;
    private static final double LOWER_BOUND = -5.0;
    private static final double UPPER_BOUND = 5.0;

    public PSOAnimation(String inputed_function) {
        function = inputed_function;
        SwingUtilities.invokeLater(() -> createAndShowGui(function));
    }

    static void initializeParticles() {
        for (int i = 0; i < SWARM_SIZE; i++) {
            double initialX = getRandomValue();
            double initialY = getRandomValue();
            ParticleSwarm.Particle particle = new ParticleSwarm.Particle(initialX, initialY);
            particles.add(particle);
        }
    }
    private static double getRandomValue() {
        return LOWER_BOUND + random.nextDouble() * (UPPER_BOUND - LOWER_BOUND);
    }
    private static void createAndShowGui(String function) {
        functionSeries = new XYSeries("PSO Particles");
        particles = new ArrayList<>();

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(functionSeries);

        JFreeChart chart = ChartFactory.createScatterPlot(
                "Particle Swarm Optimization Animation",
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

        JFrame frame = new JFrame("Particle Swarm Optimization Animation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(chartPanel, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);

        initializeParticles();
        new Thread(() -> new ParticleSwarm(function, functionSeries, particles)).start();
    }
}
