package org.example.generator;

import lombok.Getter;
import org.example.fuzzy.FuzzySet;

import java.util.HashMap;
import java.util.Map;

public class LinguisticVariable {
    @Getter
    private String name;
    private Map<String, FuzzySet> terms;

    public LinguisticVariable(String name) {
        this.name = name;
        this.terms = new HashMap<>();
    }

    public void addTerm(String termName, FuzzySet set) {
        terms.put(termName, set);
    }

    public FuzzySet getTerm(String termName) {
        return terms.get(termName);
    }

}
