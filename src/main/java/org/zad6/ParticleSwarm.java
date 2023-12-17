package org.zad6;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.jfree.data.xy.XYSeries;

import javax.swing.*;
import java.util.List;
import java.util.Random;
public class ParticleSwarm {
    private static final int NO_OF_NUMERATIONS = 200;//30;
    private static final double INERTIA_WEIGHT = 0.5;
    private static final double COGNITIVE_WEIGHT = 1.5;
    private static final double SOCIAL_WEIGHT = 1.7;//1.5;
    private static final double INERTIA_DAMPING = 0.99;
    private static final double LOWER_BOUND = -5.0;
    private static final double UPPER_BOUND = 5.0;

    private static final Random random = new Random();

    private static XYSeries functionSeries;
    private static List<Particle> particles;
    private static String function;


    public ParticleSwarm(String _function, XYSeries _functionSeries, List<Particle> _particles) {
        function = _function;
        functionSeries = _functionSeries;
        particles = _particles;
        runParticleSwarmOptimization(function);
    }
    public static void main(String[] args) {}
    static void runParticleSwarmOptimization(String function) {
        for (int iteration = 0; iteration < NO_OF_NUMERATIONS; iteration++) {
            for (Particle particle : particles) {
                double currentX = particle.getX();
                double currentY = particle.getY();
                double currentCost = costFunction(function, currentX, currentY);

                double bestX = particle.getBestX();
                double bestY = particle.getBestY();
                double bestCost = costFunction(function, bestX, bestY);

                if (currentCost < bestCost) {
                    particle.setBestX(currentX);
                    particle.setBestY(currentY);
                }

                Particle globalBest = getGlobalBest();

                double newVelocityX = calculateNewVelocity(particle.getVelocityX(), currentX, bestX, globalBest.getX());
                double newVelocityY = calculateNewVelocity(particle.getVelocityY(), currentY, bestY, globalBest.getY());

                particle.setVelocityX(newVelocityX);
                particle.setVelocityY(newVelocityY);

                double newX = currentX + newVelocityX;
                double newY = currentY + newVelocityY;

                newX = clipToBounds(newX);
                newY = clipToBounds(newY);

                particle.setX(newX);
                particle.setY(newY);

                SwingUtilities.invokeLater(() -> {
                    functionSeries.clear();
                    for (Particle p : particles) {
                        functionSeries.add(p.getX(), p.getY());
                    }
                });

                try {
                    Thread.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println("PSO calculations ended");
        System.out.println("X: " + functionSeries.getX(0));
        System.out.println("Y: " + functionSeries.getY(0));
        System.out.println("\n\n");
    }

    private static double calculateNewVelocity(double oldVelocity, double currentPosition, double personalBest, double globalBest) {
        return INERTIA_WEIGHT * oldVelocity +
                COGNITIVE_WEIGHT * random.nextDouble() * (personalBest - currentPosition) +
                SOCIAL_WEIGHT * random.nextDouble() * (globalBest - currentPosition);
    }

    private static Particle getGlobalBest() {
        Particle globalBest = particles.get(0);
        for (Particle particle : particles) {
            if (costFunction(function, particle.getX(), particle.getY()) < costFunction(function, globalBest.getX(), globalBest.getY())) {
                globalBest = particle;
            }
        }
        return globalBest;
    }

    private static double costFunction(String function, double x, double y) {
        Expression expression = new ExpressionBuilder(function)
                .variables("x", "y")
                .build()
                .setVariable("x", x)
                .setVariable("y", y);

        return expression.evaluate();

    }

    private static double getRandomValue() {
        return LOWER_BOUND + random.nextDouble() * (UPPER_BOUND - LOWER_BOUND);
    }

    private static double clipToBounds(double value) {
        return Math.max(LOWER_BOUND, Math.min(UPPER_BOUND, value));
    }

    static class Particle {
        private double x;
        private double y;
        private double velocityX;
        private double velocityY;
        private double bestX;
        private double bestY;

        public Particle(double x, double y) {
            this.x = x;
            this.y = y;
            this.velocityX = getRandomValue();
            this.velocityY = getRandomValue();
            this.bestX = x;
            this.bestY = y;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public double getVelocityX() {
            return velocityX;
        }

        public double getVelocityY() {
            return velocityY;
        }

        public double getBestX() {
            return bestX;
        }

        public double getBestY() {
            return bestY;
        }

        public void setX(double x) {
            this.x = x;
        }

        public void setY(double y) {
            this.y = y;
        }

        public void setVelocityX(double velocityX) {
            this.velocityX = velocityX;
        }

        public void setVelocityY(double velocityY) {
            this.velocityY = velocityY;
        }

        public void setBestX(double bestX) {
            this.bestX = bestX;
        }

        public void setBestY(double bestY) {
            this.bestY = bestY;
        }
    }
}
