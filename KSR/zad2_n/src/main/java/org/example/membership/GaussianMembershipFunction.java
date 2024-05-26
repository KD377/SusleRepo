package org.example.membership;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GaussianMembershipFunction implements MembershipFunction {
    private double mean;
    private double stdDev;

    @Override
    public double calculateMembership(double value) {
        return Math.exp(-0.5 * Math.pow((value - mean) / stdDev, 2));
    }
}
