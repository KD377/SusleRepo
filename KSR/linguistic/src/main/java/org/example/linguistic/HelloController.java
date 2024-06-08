package org.example.linguistic;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import lombok.Setter;
import org.example.linguistic.data.CsvReader;
import org.example.linguistic.data.PlayerStats;
import org.example.linguistic.fuzzy.FuzzySet;
import org.example.linguistic.generator.LinguisticSummary;
import org.example.linguistic.generator.Quantifiers;
import org.example.linguistic.generator.SummaryGenerator;
import org.example.linguistic.generator.Terms;
import org.example.linguistic.membership.MembershipFunction;
import org.example.linguistic.membership.TrapezoidalMembershipFunction;
import org.example.linguistic.membership.TriangularMembershipFunction;
import org.example.linguistic.membership.GaussianMembershipFunction;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.*;

@Setter
public class HelloController implements Initializable {

    private List<PlayerStats> playerStats;


    @FXML
    private ChoiceBox<String> type;

    @FXML
    private TextField filePath;

    @FXML
    private TextField filePath1;


    @FXML
    private ChoiceBox<String> p1;

    @FXML
    private ChoiceBox<String> p2;

    @FXML
    private ListView<String> summarizers;

    @FXML
    private ListView<String> summarizers1;

    @FXML
    private ChoiceBox<String> qualifier;

    @FXML
    private ChoiceBox<String> qualifier1;

    @FXML
    private Label summaryLabel;

    @FXML
    private Label summaryLabel1;

    @FXML
    private ChoiceBox<String> quantifierType;

    @FXML
    private TextField t1;

    @FXML
    private TextField t2;

    @FXML
    private TextField t3;

    @FXML
    private TextField t4;

    @FXML
    private TextField t5;

    @FXML
    private TextField t6;

    @FXML
    private TextField t7;

    @FXML
    private TextField t8;

    @FXML
    private TextField t9;

    @FXML
    private TextField t10;

    @FXML
    private TextField t11;

    @FXML
    private TextField quantifierName;

    @FXML
    private TextField summarizerName;

    @FXML
    private ChoiceBox<String> membershipQuantifier;

    @FXML
    private ChoiceBox<String> membershipSummarizer;

    @FXML
    private ChoiceBox<String> summarizerType;

    @FXML
    private VBox inputFieldsContainer;

    @FXML
    private VBox inputFieldsContainer1;

    private List<Double> weights;
    private List<TextField> membershipFunctionFields;
    private List<TextField> membershipFunctionFields1;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CsvReader csvReader = new CsvReader();
        try {
            playerStats = csvReader.readCsv("src/main/resources/NBA_Player_Stats.csv");
            summarizers.setItems(FXCollections.observableArrayList(
                    Terms.getAllTerms()));
            summarizers1.setItems(FXCollections.observableArrayList(
                    Terms.getAllTerms()));
            summarizers.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            summarizers1.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            qualifier.getItems().addAll(Terms.getAllTerms());
            qualifier.getItems().add("null");
            qualifier.setValue("null");
            qualifier1.getItems().addAll(Terms.getAllTerms());
            qualifier1.getItems().add("null");
            qualifier1.setValue("null");
            type.getItems().addAll(List.of("Pierwsza forma","Druga forma","Trzecia forma","Czwarta forma"));
            type.setValue("Pierwsza forma");
            p1.getItems().addAll(List.of("PG","SG","SF","PF","C"));
            p2.getItems().addAll(List.of("PG","SG","SF","PF","C"));
            weights = new ArrayList<>(List.of(0.4,0.075,0.075,0.075,0.075,0.075,0.075,0.075,0.075,0.075,0.075));
            initializeTextFields();
            addListenersToTextFields();

            quantifierType.getItems().addAll(List.of("względny","absolutny"));
            quantifierType.setValue("względny");

            membershipQuantifier.getItems().addAll(List.of("trapezoidalna","trojkatna","gaussowska"));
            membershipQuantifier.setValue("trapezoidalna");
            membershipQuantifier.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> updateMembershipFunctionFields(newValue, inputFieldsContainer));

            membershipSummarizer.getItems().addAll(List.of("trapezoidalna","trojkatna","gaussowska"));
            membershipSummarizer.setValue("trapezoidalna");
            membershipSummarizer.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> updateMembershipFunctionFields(newValue, inputFieldsContainer1));

            summarizerType.getItems().addAll(List.of("Games Played", "Age", "Three Pointers Made", "Three Pointer Attempts", "Two Pointers Made", "Two Pointer Attempts", "Assists", "Steals", "Blocks", "Turnovers"));
            summarizerType.setValue("Games Played");

            membershipFunctionFields = new ArrayList<>();
            updateMembershipFunctionFields(membershipQuantifier.getValue(), inputFieldsContainer);

            membershipFunctionFields1 = new ArrayList<>();
            updateMembershipFunctionFields(membershipSummarizer.getValue(), inputFieldsContainer1);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeTextFields() {
        t1.setText(weights.get(0).toString());
        t2.setText(weights.get(1).toString());
        t3.setText(weights.get(2).toString());
        t4.setText(weights.get(3).toString());
        t5.setText(weights.get(4).toString());
        t6.setText(weights.get(5).toString());
        t7.setText(weights.get(6).toString());
        t8.setText(weights.get(7).toString());
        t9.setText(weights.get(8).toString());
        t10.setText(weights.get(9).toString());
        t11.setText(weights.get(10).toString());
    }

    private void addListenersToTextFields() {
        addListenerToTextField(t1, 0);
        addListenerToTextField(t2, 1);
        addListenerToTextField(t3, 2);
        addListenerToTextField(t4, 3);
        addListenerToTextField(t5, 4);
        addListenerToTextField(t6, 5);
        addListenerToTextField(t7, 6);
        addListenerToTextField(t8, 7);
        addListenerToTextField(t9, 8);
        addListenerToTextField(t10, 9);
        addListenerToTextField(t11, 10);
    }

    private void addListenerToTextField(TextField textField, int index) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                double newWeight = Double.parseDouble(newValue);
                weights.set(index, newWeight);
            } catch (NumberFormatException e) {
                textField.setText(oldValue); // Revert to old value if the input is not a valid double
            }
        });
    }

    @FXML
    private void onHelloButtonClick() {
        List<String> summarizers = this.summarizers.getSelectionModel().getSelectedItems();
        String qualifier = this.qualifier.getValue();
        if (qualifier.equals("null")) {
            qualifier = null;
        }

        List<LinguisticSummary> summariesList = new ArrayList<>();
        Set<String> relativeQuantifiers = Quantifiers.getRelativeQuantifiers().keySet();
        Set<String> absoluteQuantifiers = Quantifiers.getAbsoluteQuantifiers().keySet();

        if (qualifier != null) {
            for (String quantifier : relativeQuantifiers) {
                LinguisticSummary summary = SummaryGenerator.generateSummary(playerStats, summarizers, quantifier, null, weights);
                summariesList.add(summary);
            }
        } else {
            for (String quantifier : relativeQuantifiers) {
                LinguisticSummary summary = SummaryGenerator.generateSummary(playerStats, summarizers, quantifier, qualifier, weights);
                summariesList.add(summary);
            }
            for (String quantifier : absoluteQuantifiers) {
                LinguisticSummary summary = SummaryGenerator.generateSummary(playerStats, summarizers, quantifier, qualifier, weights);
                summariesList.add(summary);
            }
        }

        // Sort summaries by truth value in descending order
        summariesList.sort(Comparator.comparingDouble(LinguisticSummary::getTruthValue).reversed());

        // Build the final summary string
        StringBuilder summaries = new StringBuilder();
        for (LinguisticSummary summary : summariesList) {
            summaries.append(summary).append("\n\n"); // Append summary and an additional empty line
        }

        summaryLabel.setText(summaries.toString());
    }


    @FXML
    private void onGenerateButtonClick() {
        List<LinguisticSummary> summariesList = new ArrayList<>();
        Set<String> relativeQuantifiers = Quantifiers.getRelativeQuantifiers().keySet();

        switch (type.getValue()) {
            case "Pierwsza forma":
                if (!p1.getValue().equals(p2.getValue())) {
                    for (String quantifier : relativeQuantifiers) {
                        LinguisticSummary summary = SummaryGenerator.generateMultiSubjectSummaryFirstForm(playerStats, this.summarizers1.getSelectionModel().getSelectedItems(), quantifier, p1.getValue(), p2.getValue());
                        summariesList.add(summary);
                    }
                }
                break;
            case "Druga forma":
                if (!p1.getValue().equals(p2.getValue()) && qualifier1.getValue() != null) {
                    for (String quantifier : relativeQuantifiers) {
                        LinguisticSummary summary = SummaryGenerator.generateMultiSubjectSummarySecondForm(playerStats, this.summarizers1.getSelectionModel().getSelectedItems(), quantifier, qualifier1.getValue(), p1.getValue(), p2.getValue());
                        summariesList.add(summary);
                    }
                }
                break;
            case "Trzecia forma":
                if (!p1.getValue().equals(p2.getValue()) && qualifier1.getValue() != null) {
                    for (String quantifier : relativeQuantifiers) {
                        LinguisticSummary summary = SummaryGenerator.generateMultiSubjectSummaryThirdForm(playerStats, this.summarizers1.getSelectionModel().getSelectedItems(), quantifier, p1.getValue(), p2.getValue(), qualifier1.getValue());
                        summariesList.add(summary);
                    }
                }
                break;
            case "Czwarta forma":
                if (!p1.getValue().equals(p2.getValue())) {
                    LinguisticSummary summary = SummaryGenerator.generateMultiSubjectSummaryFourthForm(playerStats, this.summarizers1.getSelectionModel().getSelectedItems(), p1.getValue(), p2.getValue());
                    summariesList.add(summary);

                }
                break;
            default:
                System.out.println("Error");
        }

        // Sort summaries by truth value in descending order
        summariesList.sort(Comparator.comparingDouble(LinguisticSummary::getTruthValue).reversed());

        // Build the final summary string
        StringBuilder summaries = new StringBuilder();
        for (LinguisticSummary summary : summariesList) {
            summaries.append(summary).append("\n\n"); // Append summary and an additional empty line
        }

        summaryLabel1.setText(summaries.toString());
    }


    @FXML
    private void onSaveButtonClick() {
        saveSummaryToFile(filePath.getText(), summaryLabel.getText());
    }

    @FXML
    private void onSaveButton1Click() {
        saveSummaryToFile(filePath1.getText(), summaryLabel1.getText());
    }

    private void saveSummaryToFile(String fileName, String content) {
        if (fileName == null || fileName.isEmpty()) {
            System.out.println("File name is empty.");
            return;
        }

        if (content == null || content.isEmpty()) {
            System.out.println("No content to save.");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(content);
            writer.newLine();
            System.out.println("Summary saved to " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void addQuantifier() {
        String name = quantifierName.getText();
        String type = quantifierType.getValue();
        MembershipFunction function = createMembershipFunction(membershipFunctionFields);

        if (function != null && name != null && !name.isEmpty()) {
            FuzzySet fuzzySet = new FuzzySet(function);

            if (type.equals("względny")) {
                Quantifiers.addRelativeQuantifier(name, fuzzySet);
            } else if (type.equals("absolutny")) {
                Quantifiers.addAbsoluteQuantifier(name, fuzzySet);
            }
        }
    }

    private MembershipFunction createMembershipFunction(List<TextField> fields) {
        List<Double> params = new ArrayList<>();
        for (TextField field : fields) {
            try {
                params.add(Double.parseDouble(field.getText()));
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format in membership function parameters.");
                return null;
            }
        }

        switch (membershipQuantifier.getValue()) {
            case "trapezoidalna":
                if (params.size() == 4) {
                    return new TrapezoidalMembershipFunction(params.get(0), params.get(1), params.get(2), params.get(3));
                }
                break;
            case "trojkatna":
                if (params.size() == 3) {
                    return new TriangularMembershipFunction(params.get(0), params.get(1), params.get(2));
                }
                break;
            case "gaussowska":
                if (params.size() == 2) {
                    return new GaussianMembershipFunction(params.get(0), params.get(1));
                }
                break;
        }

        System.out.println("Invalid number of parameters for selected membership function type.");
        return null;
    }

    private MembershipFunction createMembershipFunction1(List<TextField> fields) {
        List<Double> params = new ArrayList<>();
        for (TextField field : fields) {
            try {
                params.add(Double.parseDouble(field.getText()));
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format in membership function parameters.");
                return null;
            }
        }

        switch (membershipSummarizer.getValue()) {
            case "trapezoidalna":
                if (params.size() == 4) {
                    return new TrapezoidalMembershipFunction(params.get(0), params.get(1), params.get(2), params.get(3));
                }
                break;
            case "trojkatna":
                if (params.size() == 3) {
                    return new TriangularMembershipFunction(params.get(0), params.get(1), params.get(2));
                }
                break;
            case "gaussowska":
                if (params.size() == 2) {
                    return new GaussianMembershipFunction(params.get(0), params.get(1));
                }
                break;
        }

        System.out.println("Invalid number of parameters for selected membership function type.");
        return null;
    }

    private void updateMembershipFunctionFields(String type, VBox container) {
        container.getChildren().clear();

        int numFields;
        if (type.equals("trapezoidalna")) {
            numFields = 4;
        } else if (type.equals("trojkatna")) {
            numFields = 3;
        } else {
            numFields = 2;
        }

        List<TextField> fields = container.equals(inputFieldsContainer) ? membershipFunctionFields : membershipFunctionFields1;
        fields.clear();
        for (int i = 0; i < numFields; i++) {
            TextField field = new TextField();
            field.setPromptText("Parameter " + (i + 1));
            fields.add(field);
            container.getChildren().add(field);
        }
    }

    @FXML
    private void addSummarizer() {
        String name = summarizerName.getText();
        String type = summarizerType.getValue();
        MembershipFunction function = createMembershipFunction1(membershipFunctionFields1);

        if (function != null && name != null && !name.isEmpty()) {
            FuzzySet fuzzySet = new FuzzySet(function);

            switch (type) {
                case "Games Played":
                    Terms.addGamePlayedTerm(name, fuzzySet);
                    break;
                case "Age":
                    Terms.addAgeTerm(name, fuzzySet);
                    break;
                case "Three Pointers Made":
                    Terms.addThreePointersMadeTerm(name, fuzzySet);
                    break;
                case "Three Pointer Attempts":
                    Terms.addThreePointerAttemptsTerm(name, fuzzySet);
                    break;
                case "Two Pointers Made":
                    Terms.addTwoPointersMadeTerm(name, fuzzySet);
                    break;
                case "Two Pointer Attempts":
                    Terms.addTwoPointerAttemptsTerm(name, fuzzySet);
                    break;
                case "Assists":
                    Terms.addAssistsTerm(name, fuzzySet);
                    break;
                case "Steals":
                    Terms.addStealsTerm(name, fuzzySet);
                    break;
                case "Blocks":
                    Terms.addBlocksTerm(name, fuzzySet);
                    break;
                case "Turnovers":
                    Terms.addTurnoversTerm(name, fuzzySet);
                    break;
            }

            summarizers.getItems().add(name);
            summarizers1.getItems().add(name);
            qualifier.getItems().add(name);
            qualifier1.getItems().add(name);
        }
    }
}
