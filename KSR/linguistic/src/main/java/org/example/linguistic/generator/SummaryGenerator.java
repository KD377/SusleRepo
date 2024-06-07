package org.example.linguistic.generator;

import org.example.linguistic.data.*;
import org.example.linguistic.fuzzy.FuzzySet;
import org.example.linguistic.generator.Terms;
import org.example.linguistic.membership.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SummaryGenerator {
    public static LinguisticSummary generateSummary(List<PlayerStats> players, List<String> summarizers, String quantifier,List<Double> weights) {
        return generateSummary(players, summarizers, quantifier, null,weights);
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

    public static Map<String, FuzzySet> getQuantifierWithType(String quantifier) {
        Map<String, FuzzySet> relative = Quantifiers.getRelativeQuantifiers();
        Map<String, FuzzySet> absolute = Quantifiers.getAbsoluteQuantifiers();
        Map<String, FuzzySet> quantifierWithType = new HashMap<>();
        if (relative.containsKey(quantifier)) {
            quantifierWithType.put("relative", relative.get(quantifier));
        } else if (absolute.containsKey(quantifier)) {
            quantifierWithType.put("absolute", absolute.get(quantifier));
        } else {
            quantifierWithType.put("not found", absolute.get(0));
        }
        return quantifierWithType;
    }

    public static LinguisticSummary generateSummary(List<PlayerStats> players, List<String> summarizers, String quantifier, String qualifier,List<Double> weights) {

        String summarizer = String.join(" i ", summarizers);
        Map<String, FuzzySet> summarizersFuzzySets = getFuzzySetsForSummarizers(summarizers);


        Map<String, FuzzySet> quantifierWithType = getQuantifierWithType(quantifier);

        double truthValue;
        if (qualifier == null) {
            if (quantifierWithType.containsKey("relative")) {
                truthValue = QualityMeasures.degreeOfTruthRelative(players, summarizersFuzzySets, quantifierWithType.get("relative"));
            } else {
                truthValue = QualityMeasures.degreeOfTruthAbsolute(players, summarizersFuzzySets, quantifierWithType.get("absolute"));
            }

        } else {
            FuzzySet qualifierFuzzy = getFuzzySetsForSummarizers(List.of(qualifier)).get(qualifier);
            truthValue = QualityMeasures.degreeOfTruthSecondType(players, summarizersFuzzySets, quantifierWithType.get("relative"), qualifierFuzzy, qualifier);
        }
        double T3;
        double T4;
        double T9;
        double T10;
        double T11;
        if (qualifier != null) {
            FuzzySet qualifierFuzzy = getFuzzySetsForSummarizers(List.of(qualifier)).get(qualifier);
            T3 = QualityMeasures.degreeOfCovering(players, summarizersFuzzySets, qualifierFuzzy, qualifier);
            T4 = QualityMeasures.degreeOfCovering(players, summarizersFuzzySets, qualifierFuzzy, qualifier);
            T9 = QualityMeasures.degreeOfQualifierImprecision(players, qualifierFuzzy, qualifier);
            T10 = QualityMeasures.degreeOfQualifierCardinality(players, qualifierFuzzy, qualifier);
            T11 = QualityMeasures.lengthOfQualifier(qualifierFuzzy);
        } else {
            T3 = QualityMeasures.degreeOfCovering(players, summarizersFuzzySets, null, null);
            T4 = QualityMeasures.degreeOfCovering(players, summarizersFuzzySets, null, null);
            T9 = 1;
            T10 = 1;
            T11 = 1;
        }
        double T2 = QualityMeasures.degreeOfImprecision(summarizersFuzzySets, players);
        double T5 = QualityMeasures.lengthOfSummary(summarizersFuzzySets);
        double T8 = QualityMeasures.degreeOfSummarizerCardinality(players, summarizersFuzzySets);
        double T6 = 0;
        double T7 = 0;

        System.out.println("Summarizer: '" + summarizer + "', Quantifier: '" + quantifier + "', Qualifier: '" + (qualifier != null ? qualifier : "none") + "', Truth Value: " + truthValue + " t2: " + T2 + "\nT3: " + T3 + "\nT4: " + T4 + "\nT5: " + T5
                + "\nT8: " + T8 + "\nT9: " + T9 + "\nT10: " + T10 + "\nT11: " + T11);
        double T = weights.get(0) * truthValue + weights.get(1) * T2 + weights.get(2) * T3 + weights.get(3) * T4 + weights.get(4) * T5 + weights.get(5) * T6 + weights.get(6) * T7 + weights.get(7) * T8 + weights.get(8) * T9 + weights.get(9) * T10 + weights.get(10) * T11;

        return new LinguisticSummary("graczy", summarizer, quantifier, qualifier, null, Math.round(T * 100.0) / 100.0, "Jednopodmiotowe");
    }

    public static LinguisticSummary generateMultiSubjectSummaryFirstForm(
            List<PlayerStats> players, List<String> summarizers, String quantifier, String position1, String position2) {

        Map<String, List<PlayerStats>> groupedPlayers = PlayerGroupHelper.groupPlayersByPosition(players);
        List<PlayerStats> playersP1 = groupedPlayers.get(position1);
        List<PlayerStats> playersP2 = groupedPlayers.get(position2);

        Map<String, FuzzySet> summarizersFuzzySets = getFuzzySetsForSummarizers(summarizers);
        Map<String, FuzzySet> relative = Quantifiers.getRelativeQuantifiers();
        Map<String, FuzzySet> absolute = Quantifiers.getAbsoluteQuantifiers();
        FuzzySet quantifierFunction;
        if (relative.containsKey(quantifier)){
            quantifierFunction = relative.get(quantifier);
        }
        else {
            quantifierFunction = absolute.get(quantifier);
        }

        String summarizer = String.join(" i ", summarizers);
        double truthValue = QualityMeasures.firsTypeT(playersP1,playersP2,summarizersFuzzySets,quantifierFunction);

        return new LinguisticSummary(position1, summarizer, quantifier, null, position2, Math.round(truthValue * 100.0) / 100.0, "Wielopodmiotowe Pierwsza Forma");
    }


    public static LinguisticSummary generateMultiSubjectSummarySecondForm(
            List<PlayerStats> players, List<String> summarizers, String quantifier, String qualifier, String position1, String position2) {

        Map<String, List<PlayerStats>> groupedPlayers = PlayerGroupHelper.groupPlayersByPosition(players);
        List<PlayerStats> playersP1 = groupedPlayers.get(position1);
        List<PlayerStats> playersP2 = groupedPlayers.get(position2);

        Map<String, FuzzySet> summarizersFuzzySets = getFuzzySetsForSummarizers(summarizers);
        Map<String, FuzzySet> relative = Quantifiers.getRelativeQuantifiers();
        Map<String, FuzzySet> absolute = Quantifiers.getAbsoluteQuantifiers();
        FuzzySet qualifierFuzzySet = getFuzzySetsForSummarizers(List.of(qualifier)).get(qualifier);
        FuzzySet quantifierFunction;
        if (relative.containsKey(quantifier)){
            quantifierFunction = relative.get(quantifier);
        }
        else {
            quantifierFunction = absolute.get(quantifier);
        }

        String summarizer = String.join(" i ", summarizers);
        double truthValue = QualityMeasures.secondTypeT(playersP1,playersP2,summarizersFuzzySets,quantifierFunction,qualifierFuzzySet,qualifier);

        return new LinguisticSummary(position1, summarizer, quantifier, qualifier, position2, Math.round(truthValue * 100.0) / 100.0, "Wielopodmiotowe Druga Forma");
    }

    public static LinguisticSummary generateMultiSubjectSummaryThirdForm(
            List<PlayerStats> players, List<String> summarizers, String quantifier, String position1, String position2, String qualifier) {

        Map<String, List<PlayerStats>> groupedPlayers = PlayerGroupHelper.groupPlayersByPosition(players);
        List<PlayerStats> playersP1 = groupedPlayers.get(position1);
        List<PlayerStats> playersP2 = groupedPlayers.get(position2);

        Map<String, FuzzySet> summarizersFuzzySets = getFuzzySetsForSummarizers(summarizers);
        Map<String, FuzzySet> relative = Quantifiers.getRelativeQuantifiers();
        Map<String, FuzzySet> absolute = Quantifiers.getAbsoluteQuantifiers();
        FuzzySet qualifierFuzzySet = getFuzzySetsForSummarizers(List.of(qualifier)).get(qualifier);
        FuzzySet quantifierFunction;
        if (relative.containsKey(quantifier)){
            quantifierFunction = relative.get(quantifier);
        }
        else {
            quantifierFunction = absolute.get(quantifier);
        }
        String summarizer = String.join(" i ", summarizers);


        double truthValue = QualityMeasures.thirdTypeT(playersP1,playersP2,summarizersFuzzySets,quantifierFunction,qualifierFuzzySet,qualifier);
        return new LinguisticSummary(position1, summarizer, quantifier, qualifier, position2, Math.round(truthValue * 100.0) / 100.0, "Wielopodmiotowe Trzecia Forma");
    }


    public static LinguisticSummary generateMultiSubjectSummaryFourthForm(
            List<PlayerStats> players, List<String> summarizers, String position1, String position2) {

        Map<String, List<PlayerStats>> groupedPlayers = PlayerGroupHelper.groupPlayersByPosition(players);
        List<PlayerStats> playersP1 = groupedPlayers.get(position1);
        List<PlayerStats> playersP2 = groupedPlayers.get(position2);

        Map<String, FuzzySet> summarizersFuzzySets = getFuzzySetsForSummarizers(summarizers);

        String summarizer = String.join(" i ", summarizers);
        double truthValue = QualityMeasures.fourthTypeT(playersP1,playersP2,summarizersFuzzySets);

        return new LinguisticSummary(position1, summarizer, "Więcej", null, position2, Math.round(truthValue * 100.0) / 100.0, "Wielopodmiotowe Czwarta Forma");
    }


}
