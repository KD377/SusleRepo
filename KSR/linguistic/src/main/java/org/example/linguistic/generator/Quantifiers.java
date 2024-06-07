package org.example.linguistic.generator;

import org.example.linguistic.fuzzy.FuzzySet;
import org.example.linguistic.membership.TrapezoidalMembershipFunction;
import org.example.linguistic.membership.TriangularMembershipFunction;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Quantifiers {

    public enum QuantifierType {
        RELATIVE,
        ABSOLUTE
    }

    private static final Map<String, FuzzySet> relativeQuantifiers = Collections.synchronizedMap(new HashMap<>());
    private static final Map<String, FuzzySet> absoluteQuantifiers = Collections.synchronizedMap(new HashMap<>());

    static {
        relativeQuantifiers.put("duża mniejszość", new FuzzySet(new TrapezoidalMembershipFunction(0.0, 0.0, 0.1, 0.2)));
        relativeQuantifiers.put("mniejszość", new FuzzySet(new TrapezoidalMembershipFunction(0.08, 0.2, 0.3, 0.42)));
        relativeQuantifiers.put("połowa", new FuzzySet(new TrapezoidalMembershipFunction(0.3, 0.45, 0.55, 0.7)));
        relativeQuantifiers.put("większość", new FuzzySet(new TrapezoidalMembershipFunction(0.55, 0.7, 0.8, 0.92)));
        relativeQuantifiers.put("duża większość", new FuzzySet(new TrapezoidalMembershipFunction(0.8, 0.92, 1.0, 1.0)));

        absoluteQuantifiers.put("poniżej 3000", new FuzzySet(new TriangularMembershipFunction(0, 0, 5000)));
        absoluteQuantifiers.put("około 5000", new FuzzySet(new TriangularMembershipFunction(5000, 5000, 9750)));
        absoluteQuantifiers.put("około 9000", new FuzzySet(new TriangularMembershipFunction(9000, 9500, 14000)));
        absoluteQuantifiers.put("powyżej 11000", new FuzzySet(new TriangularMembershipFunction(9500, 14573, 14573)));
    }

    public static Map<String, FuzzySet> getRelativeQuantifiers() {
        return new HashMap<>(relativeQuantifiers);
    }

    public static Map<String, FuzzySet> getAbsoluteQuantifiers() {
        return new HashMap<>(absoluteQuantifiers);
    }

    public static void addRelativeQuantifier(String name, FuzzySet fuzzySet) {
        relativeQuantifiers.put(name, fuzzySet);
    }

    public static void addAbsoluteQuantifier(String name, FuzzySet fuzzySet) {
        absoluteQuantifiers.put(name, fuzzySet);
    }

    public static void removeRelativeQuantifier(String name) {
        relativeQuantifiers.remove(name);
    }

    public static void removeAbsoluteQuantifier(String name) {
        absoluteQuantifiers.remove(name);
    }
}
