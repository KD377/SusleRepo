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
        double T3;
        double T4;
        double T9;
        double T10;
        double T11;
        if(qualifier != null){
            FuzzySet qualifierFuzzy = getFuzzySetsForSummarizers(List.of(qualifier)).get(qualifier);
            T3 = QualityMeasures.degreeOfCovering(players,summarizersFuzzySets,qualifierFuzzy,qualifier);
            T4 = QualityMeasures.degreeOfCovering(players,summarizersFuzzySets,qualifierFuzzy,qualifier);
            T9 = QualityMeasures.degreeOfQualifierImprecision(players,qualifierFuzzy,qualifier);
            T10 = QualityMeasures.degreeOfQualifierCardinality(players,qualifierFuzzy,qualifier);
            T11 = QualityMeasures.lengthOfQualifier(qualifierFuzzy);
        }
        else {
            T3 = QualityMeasures.degreeOfCovering(players,summarizersFuzzySets,null,null);
            T4 = QualityMeasures.degreeOfCovering(players,summarizersFuzzySets,null,null);
            T9 = 0;
            T10 = 0;
            T11 = 0;
        }
        double T2 = QualityMeasures.degreeOfImprecision(summarizersFuzzySets,players);
        double T5 = QualityMeasures.lengthOfSummary(summarizersFuzzySets);
        double T8 = QualityMeasures.degreeOfSummarizerCardinality(players,summarizersFuzzySets);
        double T6 = 0;
        double T7 = 0;

//        System.out.println("Summarizer: '" + summarizer + "', Quantifier: '" + quantifier + "', Qualifier: '" + (qualifier != null ? qualifier : "none") + "', Truth Value: " + truthValue + " t2: " + T2 + "\nT3: " + T3 + "\nT4: " + T4 +"\nT5: " + T5
//                + "\nT8: " + T8 + "\nT9: " + T9+ "\nT10: " + T10+ "\nT11: " + T11);
        double T = 0.4 * truthValue + 0.075 * T2 + 0.075 * T3 + 0.075 * T4 + 0.075 * T5 + 0.075 * T6 + 0.075 * T7 + 0.075 * T8 + 0.075 * T9 + 0.075 * T10 + 0.075 * T11;

        return new LinguisticSummary("graczy", summarizer, quantifier, qualifier, T);
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
