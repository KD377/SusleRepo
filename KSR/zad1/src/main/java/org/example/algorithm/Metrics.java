package org.example.algorithm;

public class Metrics {

    public static double euclideanDistance(Object[] vector1, Object[] vector2) {
        if (vector1.length != vector2.length) {
            throw new IllegalArgumentException("Vectors must be of equal length");
        }

        double sum = 0.0;
        for (int i = 0; i < vector1.length; i++) {
            // Dodane sprawdzenie null dla obu wektorów
            if (vector1[i] == null || vector2[i] == null) {
                continue; // Możesz zdecydować, co zrobić w przypadku null; tutaj po prostu pomijamy tę wartość
            }

            if (vector1[i] instanceof String && vector2[i] instanceof String) {
                // If both elements are strings, compare them
                String str1 = (String) vector1[i];
                String str2 = (String) vector2[i];
                if (str1.equals(str2)) {
                    sum += 0; // Strings are equal, add 0
                } else {
                    sum += 1; // Strings are different, add 1
                }
            } else {
                // If elements are not strings, calculate numerical difference
                double diff = Double.parseDouble(vector1[i].toString()) - Double.parseDouble(vector2[i].toString());
                sum += Math.pow(diff, 2);
            }
        }
        return Math.sqrt(sum);
    }

    public static double manhattanDistance(Object[] vector1, Object[] vector2) {
        if (vector1.length != vector2.length) {
            throw new IllegalArgumentException("Vectors must be of equal length");
        }

        double sum = 0.0;
        for (int i = 0; i < vector1.length; i++) {
            if (vector1[i] instanceof String && vector2[i] instanceof String) {
                // If both elements are strings, compare them
                String str1 = (String) vector1[i];
                String str2 = (String) vector2[i];
                if (!str1.equals(str2)) {
                    sum += 1; // Strings are different, add 1
                }
            } else {
                // If elements are not strings, calculate numerical difference
                double diff = Double.parseDouble(vector1[i].toString()) - Double.parseDouble(vector2[i].toString());
                sum += Math.abs(diff);
            }
        }
        return sum;
    }

    public static double chebyshevDistance(Object[] vector1, Object[] vector2) {
        if (vector1.length != vector2.length) {
            throw new IllegalArgumentException("Vectors must be of equal length");
        }

        double maxDiff = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < vector1.length; i++) {
            if (vector1[i] instanceof String && vector2[i] instanceof String) {
                // If both elements are strings, compare them
                String str1 = (String) vector1[i];
                String str2 = (String) vector2[i];
                if (str1.equals(str2)) {
                    maxDiff = Math.max(maxDiff, 0); // Strings are equal
                } else {
                    maxDiff = Math.max(maxDiff, 1); // Strings are different
                }
            } else {
                // If elements are not strings, calculate numerical difference
                double diff = Math.abs(Double.parseDouble(vector1[i].toString()) - Double.parseDouble(vector2[i].toString()));
                maxDiff = Math.max(maxDiff, diff);
            }
        }
        return maxDiff;
    }
}
