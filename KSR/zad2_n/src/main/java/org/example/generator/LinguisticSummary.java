package org.example.generator;

import java.util.List;

public class LinguisticSummary {
    private String subject;
    private String predicate;
    private String quantifier;
    private double truthValue;

    public LinguisticSummary(String subject, String predicate, String quantifier, double truthValue) {
        this.subject = subject;
        this.predicate = predicate;
        this.quantifier = quantifier;
        this.truthValue = truthValue;
    }

    @Override
    public String toString() {
        return quantifier + " " + subject + " ma " + predicate + " with truth value " + truthValue;
    }
}
