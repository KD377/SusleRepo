package org.example.generator;

import org.example.fuzzy.FuzzySet;
import org.example.membership.GaussianMembershipFunction;
import org.example.membership.TrapezoidalMembershipFunction;
import org.example.membership.TriangularMembershipFunction;

import java.util.HashMap;
import java.util.Map;

public class Terms {

    public static Map<String, FuzzySet> getGamePlayedTerms() {
        Map<String, FuzzySet> terms = new HashMap<>();
        terms.put("bardzo mało gier rozegranych", new FuzzySet(new TrapezoidalMembershipFunction(1, 1, 9, 17)));
        terms.put("mało gier rozegranych", new FuzzySet(new TrapezoidalMembershipFunction(9, 16, 27, 33)));
        terms.put("średnio gier rozegranych", new FuzzySet(new TrapezoidalMembershipFunction(27, 34, 45, 54)));
        terms.put("dużo gier rozegranych", new FuzzySet(new TrapezoidalMembershipFunction(46, 53, 64, 73)));
        terms.put("bardzo dużo gier rozgeranych", new FuzzySet(new TrapezoidalMembershipFunction(66, 72, 85, 85)));
        return terms;
    }

    public static Map<String, FuzzySet> getAgeTerms() {
        Map<String, FuzzySet> terms = new HashMap<>();
        terms.put("bardzo młody wiek", new FuzzySet(new TrapezoidalMembershipFunction(18, 18, 22, 25)));
        terms.put("młody wiek", new FuzzySet(new TrapezoidalMembershipFunction(22, 25, 29, 32)));
        terms.put("średni wiek", new FuzzySet(new TrapezoidalMembershipFunction(29, 32, 36, 39)));
        terms.put("stary wiek", new FuzzySet(new TrapezoidalMembershipFunction(36, 40, 41, 44)));
        return terms;
    }

    public static Map<String, FuzzySet> getThreePointersMadeTerms() {
        Map<String, FuzzySet> terms = new HashMap<>();
        terms.put("bardzo mało 3P", new FuzzySet(new TriangularMembershipFunction(0.0, 0.0, 1.3)));
        terms.put("mało 3P", new FuzzySet(new TriangularMembershipFunction(0.1, 1.2, 2.4)));
        terms.put("średnio 3P", new FuzzySet(new TriangularMembershipFunction(1.3, 2.5, 3.6)));
        terms.put("dużo 3P", new FuzzySet(new TriangularMembershipFunction(2.5, 3.8, 5.1)));
        terms.put("bardzo dużo 3P", new FuzzySet(new TriangularMembershipFunction(3.8, 5.3, 5.3)));
        return terms;
    }

    public static Map<String, FuzzySet> getThreePointerAttemptsTerms() {
        Map<String, FuzzySet> terms = new HashMap<>();
        terms.put("bardzo mało 3PA", new FuzzySet(new TriangularMembershipFunction(0.0, 0.0, 3.3)));
        terms.put("mało 3PA", new FuzzySet(new TriangularMembershipFunction(0.3, 3.3, 6.3)));
        terms.put("średnio 3PA", new FuzzySet(new TriangularMembershipFunction(3.4, 6.2, 9.3)));
        terms.put("dużo 3PA", new FuzzySet(new TriangularMembershipFunction(6.6, 9.3, 12.1)));
        terms.put("bardzo dużo 3PA", new FuzzySet(new TriangularMembershipFunction(9.5, 13.2, 13.2)));
        return terms;
    }

    public static Map<String, FuzzySet> getTwoPointersMadeTerms() {
        Map<String, FuzzySet> terms = new HashMap<>();
        terms.put("bardzo mało 2P", new FuzzySet(new TriangularMembershipFunction(0.0, 0.0, 2.8)));
        terms.put("mało 2P", new FuzzySet(new TriangularMembershipFunction(0.2, 3.0, 5.6)));
        terms.put("średnio 2P", new FuzzySet(new TriangularMembershipFunction(3.0, 5.9, 8.8)));
        terms.put("dużo 2P", new FuzzySet(new TriangularMembershipFunction(5.8, 8.8, 11.6)));
        terms.put("bardzo dużo 2P", new FuzzySet(new TriangularMembershipFunction(8.9, 12.1, 12.1)));
        return terms;
    }

    public static Map<String, FuzzySet> getTwoPointerAttemptsTerms() {
        Map<String, FuzzySet> terms = new HashMap<>();
        terms.put("bardzo mało 2PA", new FuzzySet(new TriangularMembershipFunction(0.0, 0.0, 6.0)));
        terms.put("mało 2PA", new FuzzySet(new TriangularMembershipFunction(0.9, 6.2, 11.5)));
        terms.put("średnio 2PA", new FuzzySet(new TriangularMembershipFunction(6.3, 11.6, 17.4)));
        terms.put("dużo 2PA", new FuzzySet(new TriangularMembershipFunction(12.2, 17.2, 22.2)));
        terms.put("bardzo dużo 2PA", new FuzzySet(new TriangularMembershipFunction(17.5, 23.4, 23.4)));
        return terms;
    }

    public static Map<String, FuzzySet> getAssistsTerms() {
        Map<String, FuzzySet> terms = new HashMap<>();
        terms.put("bardzo mało AST", new FuzzySet(new TriangularMembershipFunction(0.0, 0.0, 3.0)));
        terms.put("mało AST", new FuzzySet(new TriangularMembershipFunction(0.4, 3.1, 5.7)));
        terms.put("średnio AST", new FuzzySet(new TriangularMembershipFunction(3.1, 6.0, 8.8)));
        terms.put("dużo AST", new FuzzySet(new TriangularMembershipFunction(6.0, 9.0, 12.0)));
        terms.put("bardzo dużo AST", new FuzzySet(new TriangularMembershipFunction(9.0, 12.8, 12.8)));
        return terms;
    }

    public static Map<String, FuzzySet> getStealsTerms() {
        Map<String, FuzzySet> terms = new HashMap<>();
        terms.put("mało przechwytów", new FuzzySet(new GaussianMembershipFunction(0.0, 0.6)));
        terms.put("średnio przechwytów", new FuzzySet(new GaussianMembershipFunction(1.5, 0.6)));
        terms.put("dużo przechwytów", new FuzzySet(new GaussianMembershipFunction(3.0, 0.6)));
        return terms;
    }

    public static Map<String, FuzzySet> getBlocksTerms() {
        Map<String, FuzzySet> terms = new HashMap<>();
        terms.put("mało bloków", new FuzzySet(new GaussianMembershipFunction(0.0, 1.2)));
        terms.put("średnio bloków", new FuzzySet(new GaussianMembershipFunction(3.0, 1.2)));
        terms.put("dużo bloków", new FuzzySet(new GaussianMembershipFunction(6.0, 1.2)));
        return terms;
    }

    public static Map<String, FuzzySet> getTurnoversTerms() {
        Map<String, FuzzySet> terms = new HashMap<>();
        terms.put("mało strat", new FuzzySet(new GaussianMembershipFunction(0.0, 0.45)));
        terms.put("średnio strat", new FuzzySet(new GaussianMembershipFunction(2.85, 0.55)));
        terms.put("dużo strat", new FuzzySet(new GaussianMembershipFunction(5.7, 0.525)));
        return terms;
    }
}
