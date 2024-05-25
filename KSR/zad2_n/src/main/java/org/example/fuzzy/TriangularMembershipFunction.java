package org.example.fuzzy;

public class TriangularMembershipFunction implements MembershipFunction {
    private double a, b, c;

    public TriangularMembershipFunction(double a, double b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    @Override
    public double calculateMembership(double value) {
        if (value <= a || value >= c) {
            return 0.0;
        } else if (value == b) {
            return 1.0;
        } else if (value > a && value < b) {
            return (value - a) / (b - a);
        } else {
            return (c - value) / (c - b);
        }
    }
}