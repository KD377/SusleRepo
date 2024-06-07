package org.example.linguistic.generator;

import org.example.linguistic.data.PlayerStats;
import org.example.linguistic.fuzzy.FuzzySet;
import org.example.linguistic.membership.MembershipFunction;
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
    public static double degreeOfImprecision(Map<String,FuzzySet> summarizers, List<PlayerStats> players) {
        double value = 1;
        int x = players.size();
        for(String summarizer : summarizers.keySet()){
            int supp = 0;
            for(PlayerStats player : players) {
                if(summarizers.get(summarizer).calculateMembership(getAttribute(summarizer,player)) > 0) {
                    supp ++;
                }
            }
            value *= (double) supp /x;
        }
        return 1 - Math.pow(value,(double)1/summarizers.size());

    }

    // Degree of covering (T3)
    public static double degreeOfCovering(List<PlayerStats> players, Map<String,FuzzySet> summarizers,FuzzySet qualifier,String qualifierName) {
        int t = 0;
        int h = 0;
        if (qualifier != null) {
            for(String summarizer : summarizers.keySet()) {
                for(PlayerStats player : players) {
                    if(summarizers.get(summarizer).calculateMembership(getAttribute(summarizer,player)) > 0 && qualifier.calculateMembership(getAttribute(qualifierName,player)) >0) {
                        t += 1;
                    }
                    if(qualifier.calculateMembership(getAttribute(qualifierName,player)) > 0) {
                        h += 1;
                    }
                }
            }
            return (double)t/h;
        } else {
            for(String summarizer : summarizers.keySet()) {
                for(PlayerStats player : players) {
                    if(summarizers.get(summarizer).calculateMembership(getAttribute(summarizer,player)) > 0) {
                        t += 1;
                    }
                    h +=1;
                }
            }
            return (double)t/h;
        }

    }


    public static double degreeOfAppropriateness(List<PlayerStats> players, Map<String,FuzzySet> summarizers,FuzzySet qualifier,String qualifierName) {
        double r = 0;
        int m = players.size();
        int g =0;
        double multi = 1;
        for(String summarizer : summarizers.keySet()) {
            for(PlayerStats player : players) {
                if(summarizers.get(summarizer).calculateMembership(getAttribute(summarizer,player)) > 0) {
                    g =1;
                } else {
                    g =0;
                }
                r += g;
            }
            multi *= r/m;
        }
        return Math.abs(multi - degreeOfCovering(players,summarizers,qualifier,qualifierName));

    }

    public static double lengthOfSummary(Map<String,FuzzySet> summarizers) {
        return 2 * (Math.pow(0.5, summarizers.size()));
    }

    public static double degreeOfQuantifierImprecision(List<PlayerStats> players, FuzzySet quantifier) {
        return 0;
    }

    // Degree of quantifier cardinality (T7)
    public static double degreeOfQuantifierCardinality(MembershipFunction quantifierFunction) {
        // Assuming the universal set range is from 0 to 1 for relative quantifiers
        double qCardinality = quantifierFunction.calculateMembership(1);
        return 1 - qCardinality;
    }

    // Degree of summarizer cardinality (T8)
    public static double degreeOfSummarizerCardinality(List<PlayerStats> players, Map<String,FuzzySet> summarizers) {
        double multi = 1;
        for (String summarizer : summarizers.keySet()) {
            double sum = 0;
            for (PlayerStats player : players) {
                sum += summarizers.get(summarizer).calculateMembership(getAttribute(summarizer,player));
            }
            multi *= sum / players.size();
        }
        return 1 - Math.pow(multi,(double) 1/summarizers.size());
    }

    // Degree of qualifier imprecision (T9)
    public static double degreeOfQualifierImprecision(List<PlayerStats> players,FuzzySet qualifier,String qualifierName) {
        int supp = 0;
        for(PlayerStats player : players) {
            if (qualifier.calculateMembership(getAttribute(qualifierName,player)) > 1) {
                supp ++;
            }
        }
        return 1 - ((double)supp/ players.size());
    }


    public static double degreeOfQualifierCardinality(List<PlayerStats> players,FuzzySet qualifier,String qualifierName) {
        int supp = 0;
        for(PlayerStats player : players) {
            if (qualifier.calculateMembership(getAttribute(qualifierName,player)) > 1) {
                supp ++;
            }
        }
        return 1 - ((double)supp/ players.size());
    }


    public static double lengthOfQualifier(FuzzySet qualifier) {
        return 2 * (Math.pow(0.5, 1));
    }

    public static double firsTypeT(List<PlayerStats> p1, List<PlayerStats> p2,Map<String,FuzzySet> summarizers,FuzzySet quantifierFunction) {
        double sum1 = 0;
        double sum2 = 0;
        for (String summarizer : summarizers.keySet()) {
            for (PlayerStats player : p1) {
                sum1 += summarizers.get(summarizer).calculateMembership(getAttribute(summarizer,player));
            }
            for (PlayerStats player : p2) {
                sum2 += summarizers.get(summarizer).calculateMembership(getAttribute(summarizer,player));
            }
        }
        sum1 /= p1.size();
        sum2 /= p2.size();
        return quantifierFunction.calculateMembership(sum1/(sum1 + sum2));
    }

    public static double secondTypeT(List<PlayerStats> p1, List<PlayerStats> p2,Map<String,FuzzySet> summarizers,FuzzySet quantifierFunction,FuzzySet qualifier,String qualifierName) {
        double sum1 = 0;
        double sum2 = 0;
        for (String summarizer : summarizers.keySet()) {
            for (PlayerStats player : p1) {
                sum1 += summarizers.get(summarizer).calculateMembership(getAttribute(summarizer,player));
            }
            for (PlayerStats player : p2) {
                double value = Math.min(summarizers.get(summarizer).calculateMembership(getAttribute(summarizer,player)),qualifier.calculateMembership(getAttribute(qualifierName,player)));
                sum2 += value;
            }
        }
        sum1 /= p1.size();
        sum2 /= p2.size();
        return quantifierFunction.calculateMembership(sum1/(sum1 + sum2));
    }

    public static double thirdTypeT(List<PlayerStats> p1, List<PlayerStats> p2,Map<String,FuzzySet> summarizers,FuzzySet quantifierFunction,FuzzySet qualifier,String qualifierName) {
        double sum1 = 0;
        double sum2 = 0;
        for (String summarizer : summarizers.keySet()) {
            for (PlayerStats player : p1) {
                double value = Math.min(summarizers.get(summarizer).calculateMembership(getAttribute(summarizer,player)),qualifier.calculateMembership(getAttribute(qualifierName,player)));
                sum1 += value;
            }
            for (PlayerStats player : p2) {
                sum2 += summarizers.get(summarizer).calculateMembership(getAttribute(summarizer,player));
            }
        }
        sum1 /= p1.size();
        sum2 /= p2.size();
        return quantifierFunction.calculateMembership(sum1/(sum1 + sum2));
    }

    public static double fourthTypeT(List<PlayerStats> p1, List<PlayerStats> p2,Map<String,FuzzySet> summarizers) {
        double sum1 = 0;
        double sum2 = 0;
        for (String summarizer : summarizers.keySet()) {
            for (PlayerStats player : p1) {
                sum1 += summarizers.get(summarizer).calculateMembership(getAttribute(summarizer,player));
            }
            for (PlayerStats player : p2) {
                sum2 += summarizers.get(summarizer).calculateMembership(getAttribute(summarizer,player));
            }
        }

        return sum1/(sum1 + sum2);
    }

}
