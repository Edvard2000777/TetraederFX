package com.example.demo45;


import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class InputFormPane extends VBox {
    private final TextField[] fields = new TextField[12];
    private final ColorPicker colorPicker = new ColorPicker(Color.BLACK);
    private final Stage stage;

    public InputFormPane(Stage stage) {
        this.stage = stage;
        setSpacing(10);
        setPadding(new Insets(10));

        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(5);
        inputGrid.setVgap(5);

        for (int i = 0; i < 12; i++) {
            fields[i] = new TextField();
            fields[i].setPromptText("Число " + (i + 1));
            inputGrid.add(fields[i], i % 3, i / 3);
        }

        HBox colorBox = new HBox(10, new Label("Цвет:"), colorPicker);

        Button saveButton = new Button("Сохранить");
        saveButton.setOnAction(e -> saveToJson());

        getChildren().addAll(inputGrid, colorBox, saveButton);
    }


    private void setCoordinate(TextField field, double value) {
        field.setText(String.valueOf(value));
    }
    private void saveToJson() {
        try {
            double[] values = new double[12];
            for (int i = 0; i < 12; i++) {
                values[i] = Double.parseDouble(fields[i].getText());
            }

            DataModel model = new DataModel();
            model.setValues(values);
            model.setColor(toHex(colorPicker.getValue()));

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Сохранить JSON");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON файлы", "*.json"));
            File file = fileChooser.showSaveDialog(stage);

            if (file != null) {
                ObjectMapper mapper = new ObjectMapper();
                mapper.writerWithDefaultPrettyPrinter().writeValue(file, model);
                System.out.println("Сохранено: " + file.getAbsolutePath());
            }
        } catch (Exception ex) {
            new Alert(Alert.AlertType.ERROR, "Ошибка: " + ex.getMessage()).showAndWait();
        }
    }

    private String toHex(Color color) {
        return String.format("#%02X%02X%02X",
                (int)(color.getRed()*255),
                (int)(color.getGreen()*255),
                (int)(color.getBlue()*255));
    }
}