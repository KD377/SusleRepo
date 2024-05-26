package org.example.generator;

import org.example.data.PlayerStats;
import org.example.fuzzy.FuzzySet;
import org.example.generator.Quantifiers;
import org.example.generator.Terms;
import org.example.membership.MembershipFunction;
import org.example.membership.TrapezoidalMembershipFunction;
import org.example.membership.TriangularMembershipFunction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SummaryGenerator {
    public static LinguisticSummary generateSummary(List<PlayerStats> players, List<String> summarizers, String quantifier) {
        return generateSummary(players, summarizers, quantifier, null);
    }

    public static Map<String, FuzzySet> getFuzzySetsForSummarizers(List<String> summarizers) {
        Map<String, FuzzySet> ageTerms = Terms.getAgeTerms();
        Map<String, FuzzySet> gamePlayedTerms = Terms.getGamePlayedTerms();
        Map<String, FuzzySet> threePointersMadeTerms = Terms.getThreePointersMadeTerms();
        Map<String, FuzzySet> threePointerAttemptsTerms = Terms.getThreePointerAttemptsTerms();
        Map<String, FuzzySet> twoPointersMadeTerms = Terms.getTwoPointersMadeTerms();
        Map<String, FuzzySet> twoPointerAttemptsTerms = Terms.getTwoPointerAttemptsTerms();
        Map<String, FuzzySet> assistsTerms = Terms.getAssistsTerms();
        Map<String, FuzzySet> stealsTerms = Terms.getStealsTerms();
        Map<String, FuzzySet> blocksTerms = Terms.getBlocksTerms();
        Map<String, FuzzySet> turnoversTerms = Terms.getTurnoversTerms();

        Map<String, FuzzySet> fuzzySets = new HashMap<>();

        for (String summarizer : summarizers) {
            if (ageTerms.containsKey(summarizer)) {
                fuzzySets.put(summarizer, ageTerms.get(summarizer));
            } else if (gamePlayedTerms.containsKey(summarizer)) {
                fuzzySets.put(summarizer, gamePlayedTerms.get(summarizer));
            } else if (threePointersMadeTerms.containsKey(summarizer)) {
                fuzzySets.put(summarizer, threePointersMadeTerms.get(summarizer));
            } else if (threePointerAttemptsTerms.containsKey(summarizer)) {
                fuzzySets.put(summarizer, threePointerAttemptsTerms.get(summarizer));
            } else if (twoPointersMadeTerms.containsKey(summarizer)) {
                fuzzySets.put(summarizer, twoPointersMadeTerms.get(summarizer));
            } else if (twoPointerAttemptsTerms.containsKey(summarizer)) {
                fuzzySets.put(summarizer, twoPointerAttemptsTerms.get(summarizer));
            } else if (assistsTerms.containsKey(summarizer)) {
                fuzzySets.put(summarizer, assistsTerms.get(summarizer));
            } else if (stealsTerms.containsKey(summarizer)) {
                fuzzySets.put(summarizer, stealsTerms.get(summarizer));
            } else if (blocksTerms.containsKey(summarizer)) {
                fuzzySets.put(summarizer, blocksTerms.get(summarizer));
            } else if (turnoversTerms.containsKey(summarizer)) {
                fuzzySets.put(summarizer, turnoversTerms.get(summarizer));
            } else {
                System.out.println("Summarizer '" + summarizer + "' not found in any term map.");
            }
        }

        return fuzzySets;
    }

    public static Map<String,FuzzySet> getQuantifierWithType(String quantifier) {
        Map<String,FuzzySet> relative = Quantifiers.getRelativeQuantifiers();
        Map<String,FuzzySet> absolute = Quantifiers.getAbsoluteQuantifiers();
        Map<String, FuzzySet> quantifierWithType = new HashMap<>();
        if (relative.containsKey(quantifier)) {
            quantifierWithType.put("relative",relative.get(quantifier));
        } else if (absolute.containsKey(quantifier)) {
            quantifierWithType.put("absolute",absolute.get(quantifier));
        }
        else {
            quantifierWithType.put("not found",absolute.get(0));
        }
        return  quantifierWithType;
    }

    public static LinguisticSummary generateSummary(List<PlayerStats> players, List<String> summarizers, String quantifier, String qualifier) {

        String summarizer = String.join(" i ", summarizers);
        Map<String,FuzzySet> summarizersFuzzySets = getFuzzySetsForSummarizers(summarizers);


        Map<String,FuzzySet> quantifierWithType = getQuantifierWithType(quantifier);

        double truthValue;
        if (qualifier == null){
            if(quantifierWithType.containsKey("relative")){
                truthValue = QualityMeasures.degreeOfTruthRelative(players,summarizersFuzzySets,quantifierWithType.get("relative"));
            }
            else {
                truthValue = QualityMeasures.degreeOfTruthAbsolute(players,summarizersFuzzySets,quantifierWithType.get("absolute"));
            }

        } else {
            FuzzySet qualifierFuzzy = getFuzzySetsForSummarizers(List.of(qualifier)).get(qualifier);
            truthValue = QualityMeasures.degreeOfTruthSecondType(players,summarizersFuzzySets,quantifierWithType.get("relative"),qualifierFuzzy,qualifier);
        }


        System.out.println("Summarizer: '" + summarizer + "', Quantifier: '" + quantifier + "', Qualifier: '" + (qualifier != null ? qualifier : "none") + "', Truth Value: " + truthValue);

        return new LinguisticSummary("graczy", summarizer, quantifier, qualifier, truthValue);
    }

    private static List<FuzzySet> extractSummarizers(List<String> summarizers, Map<String, FuzzySet> threePointersMadeTerms) {
        List<FuzzySet> fuzzySets = new ArrayList<>();
        for (String summarizer : summarizers) {
            if (threePointersMadeTerms.containsKey(summarizer)) {
                fuzzySets.add(threePointersMadeTerms.get(summarizer));
            }
        }
        return fuzzySets;
    }
}
