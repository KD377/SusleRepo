package org.example.linguistic.generator;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LinguisticSummary {
    private String subject;
    private String summarizer;
    private String quantifier;
    private String qualifier;
    private String secondarySubject; // Drugi podmiot dla wielopodmiotowych podsumowań
    private double truthValue;
    private String summaryType; // Typ podsumowania (jednopodmiotowe, wielopodmiotowe pierwsza forma, itp.)

    @Override
    public String toString() {
        switch (summaryType) {
            case "Jednopodmiotowe":
                if (qualifier != null && !qualifier.isEmpty()) {
                    return quantifier + " " + subject + " którzy mają " + qualifier + " mają " + summarizer + " with truth value " + truthValue;
                } else {
                    return quantifier + " " + subject + " mają " + summarizer + " with truth value " + truthValue;
                }
            case "Wielopodmiotowe Pierwsza Forma":
                return quantifier + " " + subject + " w porównaniu do " + secondarySubject + " mają " + summarizer + " with truth value " + truthValue;
            case "Wielopodmiotowe Druga Forma":
                return quantifier + " " + subject + " w porównaniu do " + secondarySubject + " którzy mają " + qualifier + " mają " + summarizer + " with truth value " + truthValue;
            case "Wielopodmiotowe Trzecia Forma":
                if (qualifier != null && !qualifier.isEmpty()) {
                    return quantifier + " " + subject + ", którzy mają " + qualifier + ", w porównaniu do " + secondarySubject + ", mają " + summarizer + " with truth value " + truthValue;
                } else {
                    return quantifier + " " + subject + " w porównaniu do " + secondarySubject + " jest " + summarizer + " with truth value " + truthValue;
                }
            case "Wielopodmiotowe Czwarta Forma":
                return "Więcej " + subject + " w porównaniu do " + secondarySubject + " mają " + summarizer + " with truth value " + truthValue;
            default:
                return "Nieznany typ podsumowania";
        }
    }
}
