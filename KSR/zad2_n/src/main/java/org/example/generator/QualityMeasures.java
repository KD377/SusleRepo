package org.example.generator;

import org.example.fuzzy.FuzzySet;
import org.example.membership.MembershipFunction;
import org.example.data.PlayerStats;
import org.example.membership.TrapezoidalMembershipFunction;
import org.example.membership.TriangularMembershipFunction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class QualityMeasures {

    public static double getAttribute(String summarizer,PlayerStats player) {
        if (summarizer.endsWith("wiek")) {
            return player.getAge();
        } else if (summarizer.endsWith("gier rozegranych")) {
            return player.getGamesPlayed();
        } else if (summarizer.endsWith("3P")) {
            return player.getThreePointersMade();
        } else if (summarizer.endsWith("3PA")) {
            return player.getThreePointerAttempts();
        } else if (summarizer.endsWith("2P")) {
            return player.getTwoPointersMade();
        } else if (summarizer.endsWith("2PA")) {
            return player.getTwoPointerAttempts();
        } else if (summarizer.endsWith("AST")) {
            return player.getAssists();
        } else if (summarizer.endsWith("przechwytów")) {
            return player.getSteals();
        } else if (summarizer.endsWith("bloków")) {
            return player.getBlocks();
        } else if (summarizer.endsWith("strat")) {
            return player.getTurnovers();
        }
        return 0;
    }
    public static double degreeOfTruthRelative(List<PlayerStats> players, Map<String,FuzzySet> summarizers, FuzzySet quantifierFunction) {
        int M = players.size();
        double sum = 0;
        if (summarizers.size() > 1) {
            for (PlayerStats player : players) {
                List<Double> values = new ArrayList<>();
                for (String summarizer: summarizers.keySet()){
                     values.add(summarizers.get(summarizer).calculateMembership(getAttribute(summarizer,player)));
                }
                sum += Collections.min(values);
            }

        } else {
            for (PlayerStats player : players) {
                for (String summarizer: summarizers.keySet()){
                    sum+= summarizers.get(summarizer).calculateMembership(getAttribute(summarizer,player));
                }
            }
        }
        return quantifierFunction.calculateMembership(sum/M);



    }

    public static double degreeOfTruthSecondType(List<PlayerStats> players, Map<String,FuzzySet> summarizers, FuzzySet quantifierFunction,FuzzySet qualifier,String qualifierName) {
        double sum1 = 0;
        double sum2 = 0;
        double r = 2;
        if (summarizers.size() == 1) {
            for (PlayerStats player : players) {
                List<Double> values = new ArrayList<>();
                for (String summarizer: summarizers.keySet()){
                    values.add(summarizers.get(summarizer).calculateMembership(getAttribute(summarizer,player)));
                }
                double w = qualifier.calculateMembership(getAttribute(qualifierName,player));
                values.add(w);
                sum1 += Collections.min(values);
                sum2 += w;
            }
            r = sum1/sum2;

        }
        return quantifierFunction.calculateMembership(r);
    }

    // Degree of truth (T1) for absolute quantifiers
    public static double degreeOfTruthAbsolute(List<PlayerStats> players, Map<String,FuzzySet> summarizers, FuzzySet quantifierFunction) {
        int M = 1;
        double sum = 0;
        if (summarizers.size() > 1) {
            for (PlayerStats player : players) {
                List<Double> values = new ArrayList<>();
                for (String summarizer: summarizers.keySet()){
                    values.add(summarizers.get(summarizer).calculateMembership(getAttribute(summarizer,player)));
                }
                sum += Collections.min(values);
            }

        } else {
            for (PlayerStats player : players) {
                for (String summarizer: summarizers.keySet()){
                    sum+= summarizers.get(summarizer).calculateMembership(getAttribute(summarizer,player));
                }
            }
        }
        return quantifierFunction.calculateMembership(sum/M);
    }

    // Degree of imprecision (T2)
    public static double degreeOfImprecision(List<FuzzySet> summarizers) {
        double product = 1.0;
        for (FuzzySet summarizer : summarizers) {
            TrapezoidalMembershipFunction function = (TrapezoidalMembershipFunction) summarizer.getMembershipFunction();
            double inSj = (function.getD() - function.getA()) / (function.getD() - function.getA()); // Assuming universal set range
            product *= inSj;
        }
        return 1 - Math.pow(product, 1.0 / summarizers.size());
    }

    // Degree of covering (T3)
    public static double degreeOfCovering(List<PlayerStats> players, FuzzySet summarizer, FuzzySet qualifier) {
        double numerator = 0;
        double denominator = 0;
        for (PlayerStats player : players) {
            double summarizerMembership = summarizer.calculateMembership(player.getGamesPlayed());
            double qualifierMembership = qualifier.calculateMembership(player.getGamesPlayed());
            numerator += (summarizerMembership > 0 && qualifierMembership > 0) ? 1 : 0;
            denominator += summarizerMembership > 0 ? 1 : 0;
        }
        return numerator / denominator;
    }

    // Degree of appropriateness (T4)
    public static double degreeOfAppropriateness(List<PlayerStats> players, List<FuzzySet> summarizers, FuzzySet qualifier) {
        double t3 = degreeOfCovering(players, summarizers.get(0), qualifier);
        double product = 1.0;
        for (FuzzySet summarizer : summarizers) {
            double rj = 0;
            for (PlayerStats player : players) {
                double summarizerMembership = summarizer.calculateMembership(player.getGamesPlayed());
                rj += summarizerMembership > 0 ? 1 : 0;
            }
            rj /= players.size();
            product *= rj;
        }
        return Math.abs(product - t3);
    }

    // Length of a summary (T5)
    public static double lengthOfSummary(List<FuzzySet> summarizers) {
        return 2 * (Math.pow(0.5, summarizers.size()));
    }

    // Degree of quantifier imprecision (T6)
    public static double degreeOfQuantifierImprecision(MembershipFunction quantifierFunction) {
        // Assuming the universal set range is from 0 to 1 for relative quantifiers
        double suppQ = quantifierFunction.calculateMembership(1);
        return 1 - suppQ;
    }

    // Degree of quantifier cardinality (T7)
    public static double degreeOfQuantifierCardinality(MembershipFunction quantifierFunction) {
        // Assuming the universal set range is from 0 to 1 for relative quantifiers
        double qCardinality = quantifierFunction.calculateMembership(1);
        return 1 - qCardinality;
    }

    // Degree of summarizer cardinality (T8)
    public static double degreeOfSummarizerCardinality(List<FuzzySet> summarizers) {
        double product = 1.0;
        for (FuzzySet summarizer : summarizers) {
            TrapezoidalMembershipFunction function = (TrapezoidalMembershipFunction) summarizer.getMembershipFunction();
            double cardinality = function.getD() - function.getA(); // Assuming the universal set range is known
            product *= cardinality;
        }
        return 1 - Math.pow(product, 1.0 / summarizers.size());
    }

    // Degree of qualifier imprecision (T9)
    public static double degreeOfQualifierImprecision(FuzzySet qualifier) {
        TrapezoidalMembershipFunction function = (TrapezoidalMembershipFunction) qualifier.getMembershipFunction();
        double inWgj = (function.getD() - function.getA()) / (function.getD() - function.getA()); // Assuming universal set range
        return 1 - inWgj;
    }

    // Degree of qualifier cardinality (T10)
    public static double degreeOfQualifierCardinality(FuzzySet qualifier) {
        TrapezoidalMembershipFunction function = (TrapezoidalMembershipFunction) qualifier.getMembershipFunction();
        double wCardinality = function.getD() - function.getA(); // Assuming the universal set range is known
        return 1 - wCardinality;
    }

    // Length of qualifier (T11)
    public static double lengthOfQualifier(FuzzySet qualifier) {
        return 2 * (1 - Math.pow(0.5, 1)); // |W| = 1 since we assume only one qualifier at a time
    }
}
