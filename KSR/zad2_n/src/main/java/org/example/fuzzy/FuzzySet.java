package org.example.fuzzy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.membership.MembershipFunction;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FuzzySet {
    private MembershipFunction membershipFunction;

    public double calculateMembership(double value) {
       return membershipFunction.calculateMembership(value);
    }
}
