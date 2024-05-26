package org.example.generator;

import org.example.data.PlayerStats;
import org.example.fuzzy.FuzzySet;
import org.example.generator.Quantifiers;
import org.example.generator.Terms;
import org.example.membership.TrapezoidalMembershipFunction;

import java.util.List;
import java.util.Map;

public class SummaryGenerator {
//    public static LinguisticSummary generateSummary(List<PlayerStats> players, LinguisticVariable variable, String term) {
//        FuzzySet set = variable.getTerm(term);
//        double sum = 0;
//        for (PlayerStats player : players) {
//            double value = player.getGamesPlayed();
//            sum += set.calculateMembership(value);
//        }
//        double truthValue = sum / players.size();
//        return new LinguisticSummary("graczy", term, "Większość", truthValue);
//    }
    public static void generateAllSummariesForGamesPlayed(List<PlayerStats> players) {
        Map<String, TrapezoidalMembershipFunction> relativeQuantifiers = Quantifiers.getRelativeQuantifiers();
        Map<String, FuzzySet> gamePlayedTerms = Terms.getGamePlayedTerms();

        for (String quantifier : relativeQuantifiers.keySet()) {
            for (String termName : gamePlayedTerms.keySet()) {
                FuzzySet term = gamePlayedTerms.get(termName);
                LinguisticSummary summary = generateSummary(players, "games played", term,List.of(termName), quantifier, relativeQuantifiers.get(quantifier));
                System.out.println(summary);
            }
        }
    }

    public static void generateAllSummariesForAge(List<PlayerStats> players) {
        Map<String, TrapezoidalMembershipFunction> relativeQuantifiers = Quantifiers.getRelativeQuantifiers();
        Map<String, FuzzySet> ageTerms = Terms.getAgeTerms();

        for (String quantifier : relativeQuantifiers.keySet()) {
            for (String termName : ageTerms.keySet()) {
                FuzzySet term = ageTerms.get(termName);
                LinguisticSummary summary = generateSummary(players, "age", term,List.of(termName), quantifier, relativeQuantifiers.get(quantifier));
                System.out.println(summary);
            }
        }
    }

    public static LinguisticSummary generateSummary(List<PlayerStats> players, String attribute, FuzzySet term,List<String> summarizers, String quantifier, TrapezoidalMembershipFunction quantifierFunction) {
        double sum = 0;
        for (PlayerStats player : players) {
            double value = 0;
            if (attribute.equals("games played")) {
                value = player.getGamesPlayed();
            } else if (attribute.equals("age")) {
                value = player.getAge();
            }
            //double membershipValue = term.calculateMembership(value);
           // sum += membershipValue;
        }
        double averageMembership = sum / players.size();
        double truthValue = quantifierFunction.calculateMembership(averageMembership);
        String summarizer = String.join(" i ", summarizers);
        //System.out.println("Term: '" + summarizer + "', Quantifier: '" + quantifier + "', Average Membership: " + averageMembership + ", Truth Value: " + truthValue);
        return new LinguisticSummary("players", summarizer, quantifier, truthValue);
    }
}
