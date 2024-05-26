package org.example.generator;

import org.example.membership.TrapezoidalMembershipFunction;

import java.util.HashMap;
import java.util.Map;

public class Quantifiers {

    public enum QuantifierType {
        RELATIVE,
        ABSOLUTE
    }

    public static Map<String, TrapezoidalMembershipFunction> getRelativeQuantifiers() {
        Map<String, TrapezoidalMembershipFunction> quantifiers = new HashMap<>();
        quantifiers.put("duża mniejszość", new TrapezoidalMembershipFunction(0.0, 0.0, 0.1, 0.2));
        quantifiers.put("mniejszość", new TrapezoidalMembershipFunction(0.08, 0.2, 0.3, 0.42));
        quantifiers.put("połowa", new TrapezoidalMembershipFunction(0.3, 0.45, 0.55, 0.7));
        quantifiers.put("większość", new TrapezoidalMembershipFunction(0.55, 0.7, 0.8, 0.92));
        quantifiers.put("duża większość", new TrapezoidalMembershipFunction(0.8, 0.92, 1.0, 1.0));
        return quantifiers;
    }

    public static Map<String, TrapezoidalMembershipFunction> getAbsoluteQuantifiers() {
        Map<String, TrapezoidalMembershipFunction> quantifiers = new HashMap<>();
        quantifiers.put("poniżej 3000", new TrapezoidalMembershipFunction(0, 0, 0, 5000));
        quantifiers.put("około 5000", new TrapezoidalMembershipFunction(5000, 5000, 5000, 9750));
        quantifiers.put("około 9000", new TrapezoidalMembershipFunction(9000, 9000, 9500, 14000));
        quantifiers.put("powyżej 11000", new TrapezoidalMembershipFunction(9500, 14573, 14573, 14573));
        return quantifiers;
    }
}
