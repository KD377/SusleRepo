package org.example;

import org.example.membership.TrapezoidalMembershipFunction;

public class TrapezoidalMembershipFunctionTest {
    public static void main(String[] args) {
        testTrapezoidalFunction(new TrapezoidalMembershipFunction(0.0, 0.0, 0.1, 0.2), new double[]{0.0, 0.05, 0.1, 0.15, 0.2, 0.25});
        testTrapezoidalFunction(new TrapezoidalMembershipFunction(0.08, 0.2, 0.3, 0.42), new double[]{0.0, 0.1, 0.2, 0.25, 0.3, 0.35, 0.42, 0.5});
        testTrapezoidalFunction(new TrapezoidalMembershipFunction(0.3, 0.45, 0.55, 0.7), new double[]{0.2, 0.3, 0.4, 0.45, 0.5, 0.55, 0.6, 0.7, 0.8});
        testTrapezoidalFunction(new TrapezoidalMembershipFunction(0.55, 0.7, 0.8, 0.92), new double[]{0.5, 0.55, 0.65, 0.7, 0.75, 0.8, 0.85, 0.92, 1.0});
        testTrapezoidalFunction(new TrapezoidalMembershipFunction(0.8, 0.92, 1.0, 1.0), new double[]{0.75, 0.8, 0.85, 0.9, 0.92, 0.95, 1.0});
    }

    private static void testTrapezoidalFunction(TrapezoidalMembershipFunction function, double[] testValues) {
        System.out.println("Testing function with a=" + function.getA() + ", b=" + function.getB() + ", c=" + function.getC() + ", d=" + function.getD());
        for (double value : testValues) {
            System.out.println("Value: " + value + ", Membership: " + function.calculateMembership(value));
        }
        System.out.println();
    }
}