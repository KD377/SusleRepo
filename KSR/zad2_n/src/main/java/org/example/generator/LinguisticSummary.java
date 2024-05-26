package org.example.generator;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LinguisticSummary {
    private String subject;
    private String summarizer;
    private String quantifier;
    private String qualifier;
    private double truthValue;

    @Override
    public String toString() {
        if (qualifier != null && !qualifier.isEmpty()) {
            return quantifier + " " + subject + " którzy mają " + qualifier + " mają " + summarizer + " with truth value " + truthValue;
        } else {
            return quantifier + " " + subject + " mają " + summarizer + " with truth value " + truthValue;
        }
    }
}
