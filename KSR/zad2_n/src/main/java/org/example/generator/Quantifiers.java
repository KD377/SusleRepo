package org.example.generator;

import org.example.fuzzy.FuzzySet;
import org.example.membership.TrapezoidalMembershipFunction;
import org.example.membership.TriangularMembershipFunction;

import java.util.HashMap;
import java.util.Map;

public class Quantifiers {

    public enum QuantifierType {
        RELATIVE,
        ABSOLUTE
    }

    public static Map<String, FuzzySet> getRelativeQuantifiers() {
        Map<String, FuzzySet> quantifiers = new HashMap<>();
        quantifiers.put("duża mniejszość", new FuzzySet(new TrapezoidalMembershipFunction(0.0, 0.0, 0.1, 0.2)));
        quantifiers.put("mniejszość", new FuzzySet(new TrapezoidalMembershipFunction(0.08, 0.2, 0.3, 0.42)));
        quantifiers.put("połowa", new FuzzySet(new TrapezoidalMembershipFunction(0.3, 0.45, 0.55, 0.7)));
        quantifiers.put("większość", new FuzzySet(new TrapezoidalMembershipFunction(0.55, 0.7, 0.8, 0.92)));
        quantifiers.put("duża większość", new FuzzySet(new TrapezoidalMembershipFunction(0.8, 0.92, 1.0, 1.0)));
        return quantifiers;
    }

    public static Map<String, FuzzySet> getAbsoluteQuantifiers() {
        Map<String, FuzzySet> quantifiers = new HashMap<>();
        quantifiers.put("poniżej 3000", new FuzzySet(new TriangularMembershipFunction(0, 0, 5000)));
        quantifiers.put("około 5000", new FuzzySet(new TriangularMembershipFunction(5000, 5000, 9750)));
        quantifiers.put("około 9000", new FuzzySet(new TriangularMembershipFunction(9000, 9500, 14000)));
        quantifiers.put("powyżej 11000", new FuzzySet(new TriangularMembershipFunction(9500, 14573, 14573)));
        return quantifiers;
    }
}
