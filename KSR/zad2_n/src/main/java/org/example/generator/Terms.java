package org.example.generator;

import org.example.fuzzy.FuzzySet;
import org.example.membership.TrapezoidalMembershipFunction;

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
}
