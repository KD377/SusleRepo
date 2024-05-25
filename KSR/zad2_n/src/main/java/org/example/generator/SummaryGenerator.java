package org.example.generator;

import org.example.data.PlayerStats;
import org.example.fuzzy.FuzzySet;

import java.util.List;

public class SummaryGenerator {
    public static LinguisticSummary generateSummary(List<PlayerStats> players, LinguisticVariable variable, String term) {
        FuzzySet set = variable.getTerm(term);
        double sum = 0;
        for (PlayerStats player : players) {
            double value = player.getGamesPlayed();
            sum += set.calculateMembership(value);
        }
        double truthValue = sum / players.size();
        return new LinguisticSummary("players", term, "Most", truthValue);
    }
}
