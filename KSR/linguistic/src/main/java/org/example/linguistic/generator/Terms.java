package org.example.linguistic.generator;

import org.example.linguistic.fuzzy.FuzzySet;
import org.example.linguistic.membership.GaussianMembershipFunction;
import org.example.linguistic.membership.TrapezoidalMembershipFunction;
import org.example.linguistic.membership.TriangularMembershipFunction;

import java.util.*;

public class Terms {

    private static final Map<String, FuzzySet> gamePlayedTerms = Collections.synchronizedMap(new HashMap<>());
    private static final Map<String, FuzzySet> ageTerms = Collections.synchronizedMap(new HashMap<>());
    private static final Map<String, FuzzySet> threePointersMadeTerms = Collections.synchronizedMap(new HashMap<>());
    private static final Map<String, FuzzySet> threePointerAttemptsTerms = Collections.synchronizedMap(new HashMap<>());
    private static final Map<String, FuzzySet> twoPointersMadeTerms = Collections.synchronizedMap(new HashMap<>());
    private static final Map<String, FuzzySet> twoPointerAttemptsTerms = Collections.synchronizedMap(new HashMap<>());
    private static final Map<String, FuzzySet> assistsTerms = Collections.synchronizedMap(new HashMap<>());
    private static final Map<String, FuzzySet> stealsTerms = Collections.synchronizedMap(new HashMap<>());
    private static final Map<String, FuzzySet> blocksTerms = Collections.synchronizedMap(new HashMap<>());
    private static final Map<String, FuzzySet> turnoversTerms = Collections.synchronizedMap(new HashMap<>());

    static {
        gamePlayedTerms.put("bardzo mało gier rozegranych", new FuzzySet(new TrapezoidalMembershipFunction(1, 1, 9, 17)));
        gamePlayedTerms.put("mało gier rozegranych", new FuzzySet(new TrapezoidalMembershipFunction(9, 16, 27, 33)));
        gamePlayedTerms.put("średnio gier rozegranych", new FuzzySet(new TrapezoidalMembershipFunction(27, 34, 45, 54)));
        gamePlayedTerms.put("dużo gier rozegranych", new FuzzySet(new TrapezoidalMembershipFunction(46, 53, 64, 73)));
        gamePlayedTerms.put("bardzo dużo gier rozgeranych", new FuzzySet(new TrapezoidalMembershipFunction(66, 72, 85, 85)));

        ageTerms.put("bardzo młody wiek", new FuzzySet(new TrapezoidalMembershipFunction(18, 18, 22, 25)));
        ageTerms.put("młody wiek", new FuzzySet(new TrapezoidalMembershipFunction(22, 25, 29, 32)));
        ageTerms.put("średni wiek", new FuzzySet(new TrapezoidalMembershipFunction(29, 32, 36, 39)));
        ageTerms.put("stary wiek", new FuzzySet(new TrapezoidalMembershipFunction(36, 40, 41, 44)));

        threePointersMadeTerms.put("bardzo mało 3P", new FuzzySet(new TriangularMembershipFunction(0.0, 0.0, 1.3)));
        threePointersMadeTerms.put("mało 3P", new FuzzySet(new TriangularMembershipFunction(0.1, 1.2, 2.4)));
        threePointersMadeTerms.put("średnio 3P", new FuzzySet(new TriangularMembershipFunction(1.3, 2.5, 3.6)));
        threePointersMadeTerms.put("dużo 3P", new FuzzySet(new TriangularMembershipFunction(2.5, 3.8, 5.1)));
        threePointersMadeTerms.put("bardzo dużo 3P", new FuzzySet(new TriangularMembershipFunction(3.8, 5.3, 5.3)));

        threePointerAttemptsTerms.put("bardzo mało 3PA", new FuzzySet(new TriangularMembershipFunction(0.0, 0.0, 3.3)));
        threePointerAttemptsTerms.put("mało 3PA", new FuzzySet(new TriangularMembershipFunction(0.3, 3.3, 6.3)));
        threePointerAttemptsTerms.put("średnio 3PA", new FuzzySet(new TriangularMembershipFunction(3.4, 6.2, 9.3)));
        threePointerAttemptsTerms.put("dużo 3PA", new FuzzySet(new TriangularMembershipFunction(6.6, 9.3, 12.1)));
        threePointerAttemptsTerms.put("bardzo dużo 3PA", new FuzzySet(new TriangularMembershipFunction(9.5, 13.2, 13.2)));

        twoPointersMadeTerms.put("bardzo mało 2P", new FuzzySet(new TriangularMembershipFunction(0.0, 0.0, 2.8)));
        twoPointersMadeTerms.put("mało 2P", new FuzzySet(new TriangularMembershipFunction(0.2, 3.0, 5.6)));
        twoPointersMadeTerms.put("średnio 2P", new FuzzySet(new TriangularMembershipFunction(3.0, 5.9, 8.8)));
        twoPointersMadeTerms.put("dużo 2P", new FuzzySet(new TriangularMembershipFunction(5.8, 8.8, 11.6)));
        twoPointersMadeTerms.put("bardzo dużo 2P", new FuzzySet(new TriangularMembershipFunction(8.9, 12.1, 12.1)));

        twoPointerAttemptsTerms.put("bardzo mało 2PA", new FuzzySet(new TriangularMembershipFunction(0.0, 0.0, 6.0)));
        twoPointerAttemptsTerms.put("mało 2PA", new FuzzySet(new TriangularMembershipFunction(0.9, 6.2, 11.5)));
        twoPointerAttemptsTerms.put("średnio 2PA", new FuzzySet(new TriangularMembershipFunction(6.3, 11.6, 17.4)));
        twoPointerAttemptsTerms.put("dużo 2PA", new FuzzySet(new TriangularMembershipFunction(12.2, 17.2, 22.2)));
        twoPointerAttemptsTerms.put("bardzo dużo 2PA", new FuzzySet(new TriangularMembershipFunction(17.5, 23.4, 23.4)));

        assistsTerms.put("bardzo mało AST", new FuzzySet(new TriangularMembershipFunction(0.0, 0.0, 3.0)));
        assistsTerms.put("mało AST", new FuzzySet(new TriangularMembershipFunction(0.4, 3.1, 5.7)));
        assistsTerms.put("średnio AST", new FuzzySet(new TriangularMembershipFunction(3.1, 6.0, 8.8)));
        assistsTerms.put("dużo AST", new FuzzySet(new TriangularMembershipFunction(6.0, 9.0, 12.0)));
        assistsTerms.put("bardzo dużo AST", new FuzzySet(new TriangularMembershipFunction(9.0, 12.8, 12.8)));

        stealsTerms.put("mało przechwytów", new FuzzySet(new GaussianMembershipFunction(0.0, 0.6)));
        stealsTerms.put("średnio przechwytów", new FuzzySet(new GaussianMembershipFunction(1.5, 0.6)));
        stealsTerms.put("dużo przechwytów", new FuzzySet(new GaussianMembershipFunction(3.0, 0.6)));

        blocksTerms.put("mało bloków", new FuzzySet(new GaussianMembershipFunction(0.0, 1.2)));
        blocksTerms.put("średnio bloków", new FuzzySet(new GaussianMembershipFunction(3.0, 1.2)));
        blocksTerms.put("dużo bloków", new FuzzySet(new GaussianMembershipFunction(6.0, 1.2)));

        turnoversTerms.put("mało strat", new FuzzySet(new GaussianMembershipFunction(0.0, 0.45)));
        turnoversTerms.put("średnio strat", new FuzzySet(new GaussianMembershipFunction(2.85, 0.55)));
        turnoversTerms.put("dużo strat", new FuzzySet(new GaussianMembershipFunction(5.7, 0.525)));
    }

    public static Map<String, FuzzySet> getGamePlayedTerms() {
        return new HashMap<>(gamePlayedTerms);
    }

    public static Map<String, FuzzySet> getAgeTerms() {
        return new HashMap<>(ageTerms);
    }

    public static Map<String, FuzzySet> getThreePointersMadeTerms() {
        return new HashMap<>(threePointersMadeTerms);
    }

    public static Map<String, FuzzySet> getThreePointerAttemptsTerms() {
        return new HashMap<>(threePointerAttemptsTerms);
    }

    public static Map<String, FuzzySet> getTwoPointersMadeTerms() {
        return new HashMap<>(twoPointersMadeTerms);
    }

    public static Map<String, FuzzySet> getTwoPointerAttemptsTerms() {
        return new HashMap<>(twoPointerAttemptsTerms);
    }

    public static Map<String, FuzzySet> getAssistsTerms() {
        return new HashMap<>(assistsTerms);
    }

    public static Map<String, FuzzySet> getStealsTerms() {
        return new HashMap<>(stealsTerms);
    }

    public static Map<String, FuzzySet> getBlocksTerms() {
        return new HashMap<>(blocksTerms);
    }

    public static Map<String, FuzzySet> getTurnoversTerms() {
        return new HashMap<>(turnoversTerms);
    }

    public static void addGamePlayedTerm(String name, FuzzySet fuzzySet) {
        gamePlayedTerms.put(name, fuzzySet);
    }

    public static void addAgeTerm(String name, FuzzySet fuzzySet) {
        ageTerms.put(name, fuzzySet);
    }

    public static void addThreePointersMadeTerm(String name, FuzzySet fuzzySet) {
        threePointersMadeTerms.put(name, fuzzySet);
    }

    public static void addThreePointerAttemptsTerm(String name, FuzzySet fuzzySet) {
        threePointerAttemptsTerms.put(name, fuzzySet);
    }

    public static void addTwoPointersMadeTerm(String name, FuzzySet fuzzySet) {
        twoPointersMadeTerms.put(name, fuzzySet);
    }

    public static void addTwoPointerAttemptsTerm(String name, FuzzySet fuzzySet) {
        twoPointerAttemptsTerms.put(name, fuzzySet);
    }

    public static void addAssistsTerm(String name, FuzzySet fuzzySet) {
        assistsTerms.put(name, fuzzySet);
    }

    public static void addStealsTerm(String name, FuzzySet fuzzySet) {
        stealsTerms.put(name, fuzzySet);
    }

    public static void addBlocksTerm(String name, FuzzySet fuzzySet) {
        blocksTerms.put(name, fuzzySet);
    }

    public static void addTurnoversTerm(String name, FuzzySet fuzzySet) {
        turnoversTerms.put(name, fuzzySet);
    }

    public static List<String> getAllTerms() {
        List<String> allTerms = new ArrayList<>();
        allTerms.addAll(getGamePlayedTerms().keySet());
        allTerms.addAll(getAgeTerms().keySet());
        allTerms.addAll(getThreePointersMadeTerms().keySet());
        allTerms.addAll(getThreePointerAttemptsTerms().keySet());
        allTerms.addAll(getTwoPointersMadeTerms().keySet());
        allTerms.addAll(getTwoPointerAttemptsTerms().keySet());
        allTerms.addAll(getAssistsTerms().keySet());
        allTerms.addAll(getStealsTerms().keySet());
        allTerms.addAll(getBlocksTerms().keySet());
        allTerms.addAll(getTurnoversTerms().keySet());
        return allTerms;
    }
}
