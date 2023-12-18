package org.zad6;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.jfree.data.xy.XYSeries;

import javax.swing.*;
import java.util.Random;

public class SimulatedAnnealing {
    private static final double INITIAL_TEMPERATURE = 10000000.0;
    private static final double COOLING_RATE = 0.3;//0.003;
    private static final int MAX_ITERATIONS = 10000;

    private static final double LOWER_BOUND = -5.0;
    private static final double UPPER_BOUND = 5.0;

    private static final Random random = new Random();

    private static XYSeries functionSeries;
    private static XYSeries saPathSeries;
    private static String function;


    public SimulatedAnnealing(String _function, XYSeries _functionSeries, XYSeries _saPathSeries) {
        function = _function;
        functionSeries = _functionSeries;
        saPathSeries = _saPathSeries;

        runSimulatedAnnealingAlgorithm(function);
    }

    public static void main(String[] args) {
    }


    static void runSimulatedAnnealingAlgorithm(String function) {
        double currentX = getRandomValue();
        double currentY = getRandomValue();
        double currentCost = costFunction(function, currentX, currentY);

        double bestX = currentX;
        double bestY = currentY;
        double bestCost = currentCost;

        double temperature = INITIAL_TEMPERATURE;

        double finalBestX = 0;
        double finalBestY = 0;

        for (int iteration = 0; iteration < MAX_ITERATIONS; iteration++) {
            double newX = currentX + getRandomStep();
            double newY = currentY + getRandomStep();

            newX = clipToBounds(newX);
            newY = clipToBounds(newY);

            double newCost = costFunction(function, newX, newY);

            double acceptanceProbability = calculateAcceptanceProbability(currentCost, newCost, temperature);

            if (random.nextDouble() < acceptanceProbability) {
                currentX = newX;
                currentY = newY;
                currentCost = newCost;
            }

            if (newCost < bestCost) {
                bestX = newX;
                bestY = newY;
                bestCost = newCost;
            }

            temperature *= 1.0 - COOLING_RATE;

            double finalCurrentX = currentX;
            double finalCurrentY = currentY;
            finalBestX = bestX;
            finalBestY = bestY;
            double finalBestX1 = finalBestX;
            double finalBestY1 = finalBestY;

            SwingUtilities.invokeLater(() -> {
                functionSeries.clear();
                functionSeries.add(finalCurrentX, finalCurrentY);
                saPathSeries.add(finalBestX1, finalBestY1);
            });

            try {
                Thread.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("SA calculations ended");
        System.out.println("X: " + finalBestX);
        System.out.println("Y: " + finalBestY);
        System.out.println("\n\n");

    }

    private static double costFunction(String function, double x, double y) {

        Expression expression;
        if(function.contains("y")){
            expression = new ExpressionBuilder(function)
                    .variables("x", "y")
                    .build()
                    .setVariable("x", x)
                    .setVariable("y", y);
        } else{
            expression = new ExpressionBuilder(function)
                    .variables("x")
                    .build()
                    .setVariable("x", x);
        }
        return expression.evaluate();
    }

    private static double getRandomValue() {
        return LOWER_BOUND + random.nextDouble() * (UPPER_BOUND - LOWER_BOUND);
    }

    private static double getRandomStep() {
        return -0.5 + random.nextDouble();
    }

    private static double clipToBounds(double value) {
        return Math.max(LOWER_BOUND, Math.min(UPPER_BOUND, value));
    }

    private static double calculateAcceptanceProbability(double currentCost, double newCost, double temperature) {
        if (newCost < currentCost) {
            return 1.0;
        }
        return Math.exp((currentCost - newCost) / temperature);
    }
}
