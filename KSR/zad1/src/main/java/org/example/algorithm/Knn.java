package org.example.algorithm;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.function.BiFunction;

public class Knn {

    private HashMap<Object[], String> vectors;
    private final HashMap<Object[], String> trainingSet = new HashMap<>();
    private final HashMap<Object[], String> testingSet = new HashMap<>();
    // Dodatkowa mapa do przechowywania oryginalnych etykiet dla zbioru testowego
    private final HashMap<Object[], String> originalLabelsForTestingSet = new HashMap<>();

    private final int k;

    public Knn(HashMap<Object[], String> vectors, int trainingSetPercentage, int k) {
        if (trainingSetPercentage >= 100 || trainingSetPercentage < 0) {
            throw new IllegalArgumentException("Training set percentage must be lower than 100 and positive");
        }
        this.k = k;
        this.vectors = vectors;

        int numOfVectors = (trainingSetPercentage * this.vectors.size()) / 100;

        int count = 0;
        for (Map.Entry<Object[], String> entry : this.vectors.entrySet()) {
            if (count < numOfVectors) {
                this.trainingSet.put(entry.getKey(), entry.getValue());
                count++;
            } else {
                this.testingSet.put(entry.getKey(), "");
                // Zachowaj oryginalne etykiety dla zbioru testowego
                this.originalLabelsForTestingSet.put(entry.getKey(), entry.getValue());
            }
        }
    }

    public void classifyTestingSet(BiFunction<Object[], Object[], Double> distanceMetric) {
        for (Map.Entry<Object[], String> testEntry : this.testingSet.entrySet()) {
            Object[] testVector = testEntry.getKey();
            String predictedLabel = classifyVector(testVector, distanceMetric);
            this.testingSet.put(testVector, predictedLabel);
        }
    }

    private String classifyVector(Object[] testVector, BiFunction<Object[], Object[], Double> distanceMetric) {
        PriorityQueue<Map.Entry<Object[], Double>> pq = new PriorityQueue<>((a, b) -> Double.compare(a.getValue(), b.getValue()));
        for (Map.Entry<Object[], String> trainEntry : trainingSet.entrySet()) {
            Object[] trainVector = trainEntry.getKey();
            double distance = distanceMetric.apply(testVector, trainVector);
            pq.offer(Map.entry(trainVector, distance));
        }

        int count = 0;
        Map<String, Integer> classCounts = new HashMap<>();
        while (!pq.isEmpty() && count < k) {
            Object[] neighborVector = pq.poll().getKey();
            String label = trainingSet.get(neighborVector);
            classCounts.put(label, classCounts.getOrDefault(label, 0) + 1);
            count++;
        }

        String predictedLabel = null;
        int maxCount = 0;
        for (Map.Entry<String, Integer> entry : classCounts.entrySet()) {
            if (entry.getValue() > maxCount) {
                predictedLabel = entry.getKey();
                maxCount = entry.getValue();
            }
        }

        return predictedLabel;
    }

    public HashMap<Object[], String> getTestingSet() {
        return testingSet;
    }

    // Nowa metoda do pobierania oryginalnych etykiet dla zbioru testowego
    public HashMap<Object[], String> getOriginalLabelsForTestingSet() {
        return originalLabelsForTestingSet;
    }
}
