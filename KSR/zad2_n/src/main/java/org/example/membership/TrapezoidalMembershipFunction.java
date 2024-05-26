package org.example.membership;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TrapezoidalMembershipFunction implements MembershipFunction{
    private double a, b, c, d;

    @Override
    public double calculateMembership(double value) {
        if (value <= a || value >= d) {
            return 0.0;
        } else if (value >= b && value <= c) {
            return 1.0;
        } else if (value > a && value < b) {
            return (value - a) / (b - a);
        } else {
            return (d - value) / (d - c);
        }
    }

}
