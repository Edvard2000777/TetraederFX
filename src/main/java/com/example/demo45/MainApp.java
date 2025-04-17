package com.example.demo45;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static com.example.demo45.TetrahedronRenderer.createTetrahedronMesh;

public class MainApp extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    private SubScene tetraScene;

    @Override
    public void start(Stage primaryStage) {

        // Поля ввода
        TextField v1Field = new TextField();
        TextField v2Field = new TextField();
        TextField v3Field = new TextField();
        TextField v4Field = new TextField();
        TextField v5Field = new TextField();
        TextField v6Field = new TextField();
        TextField v7Field = new TextField();
        TextField v8Field = new TextField();
        TextField v9Field = new TextField();
        TextField v10Field = new TextField();
        TextField v11Field = new TextField();
        TextField v12Field = new TextField();
        ColorPicker colorPicker = new ColorPicker(Color.DODGERBLUE);

        // Кнопки
        Button saveButton = new Button("Сохранить");
        Button loadButton = new Button("Загрузить");

        // Слушатели кнопок
        saveButton.setOnAction(e -> saveData(v1Field, v2Field, v3Field, v4Field, v5Field, v6Field, v7Field, v8Field, v9Field, v10Field, v11Field, v12Field, colorPicker));
        loadButton.setOnAction(e -> loadData(v1Field, v2Field, v3Field, v4Field, v5Field, v6Field, v7Field, v8Field, v9Field, v10Field, v11Field, v12Field, colorPicker));
        loadButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Открыть файл модели");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("JSON файлы", "*.json")
            );
            File file = fileChooser.showOpenDialog(primaryStage);
            if (file != null) {
                try {
                    TetrahedronData loadedData = TetrahedronData.loadFromFile(file);
                    // Обновляем поля ввода
                    v1Field.setText(String.valueOf(loadedData.getV1()));
                    v2Field.setText(String.valueOf(loadedData.getV2()));
                    v3Field.setText(String.valueOf(loadedData.getV3()));
                    v4Field.setText(String.valueOf(loadedData.getV4()));
                    v5Field.setText(String.valueOf(loadedData.getV5()));
                    v6Field.setText(String.valueOf(loadedData.getV6()));
                    v7Field.setText(String.valueOf(loadedData.getV7()));
                    v8Field.setText(String.valueOf(loadedData.getV8()));
                    v9Field.setText(String.valueOf(loadedData.getV9()));
                    v10Field.setText(String.valueOf(loadedData.getV10()));
                    v11Field.setText(String.valueOf(loadedData.getV11()));
                    v12Field.setText(String.valueOf(loadedData.getV12()));
                    colorPicker.setValue(Color.web(loadedData.getColor()));

                    // Обновляем модель и визуализацию
                    DataModel.getInstance().setTetrahedronData(loadedData);
                    TetrahedronRenderer.updateScene(loadedData);
                } catch (IOException ex) {
                    showAlert("Ошибка", "Не удалось загрузить файл: " + ex.getMessage());
                }
            }
        });

        // Сетка для ввода данных
        GridPane inputGrid = new GridPane();
        inputGrid.setTranslateY(-40);
        inputGrid.setAlignment(Pos.CENTER);
        inputGrid.setVgap(10);
        inputGrid.setHgap(10);
        inputGrid.setPadding(new Insets(20, 20, 20, -500));

        TextField[] fields = {
                v1Field, v2Field, v3Field, v4Field, v5Field, v6Field,
                v7Field, v8Field, v9Field, v10Field, v11Field, v12Field
        };

        for (int i = 0; i < fields.length; i++) {
            inputGrid.add(new Label("Значение " + (i + 1) + ":"), 0, i);
            inputGrid.add(fields[i], 1, i);
        }
        File lastFile = getLastOpenedFile();
        if (lastFile != null) {
            try {
                TetrahedronData loadedData = TetrahedronData.loadFromFile(lastFile);
                DataModel.getInstance().setTetrahedronData(loadedData);
                TetrahedronRenderer.updateScene(loadedData);

                // Обновляем поля ввода
                v1Field.setText(String.valueOf(loadedData.getV1()));
                v2Field.setText(String.valueOf(loadedData.getV2()));
                v3Field.setText(String.valueOf(loadedData.getV3()));
                v4Field.setText(String.valueOf(loadedData.getV4()));
                v5Field.setText(String.valueOf(loadedData.getV5()));
                v6Field.setText(String.valueOf(loadedData.getV6()));
                v7Field.setText(String.valueOf(loadedData.getV7()));
                v8Field.setText(String.valueOf(loadedData.getV8()));
                v9Field.setText(String.valueOf(loadedData.getV9()));
                v10Field.setText(String.valueOf(loadedData.getV10()));
                v11Field.setText(String.valueOf(loadedData.getV11()));
                v12Field.setText(String.valueOf(loadedData.getV12()));
                colorPicker.setValue(Color.web(loadedData.getColor()));
            } catch (IOException e) {
                System.out.println("Не удалось загрузить последний файл: " + e.getMessage());
            }
        }
        // Нижняя панель для цветов и кнопок
        HBox bottomBox = new HBox(10);
        bottomBox.setTranslateY(-70);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setPadding(new Insets(15, 0, 0, 0));
        bottomBox.getChildren().addAll(new Label("Цвет рёбер:"), colorPicker, saveButton, loadButton);

        // Объединяем форму и нижние элементы
        VBox fullForm = new VBox(20, inputGrid, bottomBox);

        tetraScene = createTetrahedronScene(new TetrahedronData(), 10);

        // Главный контейнер
        VBox root = new VBox(800, fullForm, tetraScene);
        root.setSpacing(-490);
        root.setAlignment(Pos.CENTER);  // Выравнивание всех элементов по центру

        for (TextField field : fields) {
            field.textProperty().addListener((obs, oldVal, newVal) -> {
                if (areAllFieldsValid(fields)) {
                    TetrahedronData updatedData = getTetrahedronDataFromFields(fields, colorPicker);
                    rebuildTetrahedron(updatedData, root);
                }
            });
        }
        Button buildButton = new Button("Поменять цвет");
        buildButton.setOnAction(e -> {
            if (areAllFieldsValid(fields)) {
                TetrahedronData data = getTetrahedronDataFromFields(fields, colorPicker);
                rebuildTetrahedron(data, root);
            } else {
                showAlert("Ошибка", "Введите корректные числовые значения для всех координат.");
            }
        });
        bottomBox.getChildren().add(buildButton);

        Scene scene = new Scene(root, 780, 500);

        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/TetraederIcon.png")));
        primaryStage.setTitle("3D Tetrahedron");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    private File getLastOpenedFile() {
        Properties props = new Properties();
        File propsFile = new File("lastfile.properties");
        if (propsFile.exists()) {
            try (FileInputStream in = new FileInputStream(propsFile)) {
                props.load(in);
                String lastFilePath = props.getProperty("lastFile");
                if (lastFilePath != null) {
                    return new File(lastFilePath);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static String toHex(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    // Метод для отображения предупреждающего окна
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void saveData(TextField v1Field, TextField v2Field, TextField v3Field, TextField v4Field,
                          TextField v5Field, TextField v6Field, TextField v7Field, TextField v8Field,
                          TextField v9Field, TextField v10Field, TextField v11Field, TextField v12Field,
                          ColorPicker colorPicker) {
        try {
            TetrahedronData data = new TetrahedronData(
                    parseDoubleWithDefault(v1Field.getText(), 0.0),
                    parseDoubleWithDefault(v2Field.getText(), 0.0),
                    parseDoubleWithDefault(v3Field.getText(), 0.0),
                    parseDoubleWithDefault(v4Field.getText(), 0.0),
                    parseDoubleWithDefault(v5Field.getText(), 0.0),
                    parseDoubleWithDefault(v6Field.getText(), 0.0),
                    parseDoubleWithDefault(v7Field.getText(), 0.0),
                    parseDoubleWithDefault(v8Field.getText(), 0.0),
                    parseDoubleWithDefault(v9Field.getText(), 0.0),
                    parseDoubleWithDefault(v10Field.getText(), 0.0),
                    parseDoubleWithDefault(v11Field.getText(), 0.0),
                    parseDoubleWithDefault(v12Field.getText(), 0.0),
                    toHex(colorPicker.getValue())
            );

            // Сохраняем в файл
            ObjectMapper objectMapper = new ObjectMapper();
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
            File file = fileChooser.showSaveDialog(null);
            if (file != null) {
                objectMapper.writeValue(file, data);
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Ошибка", "Произошла ошибка при сохранении данных.");
        }
    }


    // Метод для загрузки данных из JSON
    private void loadData(TextField v1Field, TextField v2Field, TextField v3Field, TextField v4Field,
                          TextField v5Field, TextField v6Field, TextField v7Field, TextField v8Field,
                          TextField v9Field, TextField v10Field, TextField v11Field, TextField v12Field,
                          ColorPicker colorPicker) {

        TetrahedronData data = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
            File file = fileChooser.showOpenDialog(null); // Открытие окна выбора файла
            if (file != null) {
                data = objectMapper.readValue(file, TetrahedronData.class); // Чтение данных из JSON
                if (data != null) {
                    rebuildTetrahedron(data, (VBox) tetraScene.getParent()); // Перестроение тетраэдра с загруженными данными
                }

                // Загружаем данные в поля
                v1Field.setText(String.valueOf(data.getV1()));
                v2Field.setText(String.valueOf(data.getV2()));
                v3Field.setText(String.valueOf(data.getV3()));
                v4Field.setText(String.valueOf(data.getV4()));
                v5Field.setText(String.valueOf(data.getV5()));
                v6Field.setText(String.valueOf(data.getV6()));
                v7Field.setText(String.valueOf(data.getV7()));
                v8Field.setText(String.valueOf(data.getV8()));
                v9Field.setText(String.valueOf(data.getV9()));
                v10Field.setText(String.valueOf(data.getV10()));
                v11Field.setText(String.valueOf(data.getV11()));
                v12Field.setText(String.valueOf(data.getV12()));

                colorPicker.setValue(Color.web(data.getColor())); // Восстановление цвета рёбер
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Ошибка", "Произошла ошибка при загрузке данных.");
        }
        rebuildTetrahedron(data, (VBox) tetraScene.getParent()); // Воссоздание тетраэдра
    }


    private double parseDoubleWithDefault(String text, double defaultValue) {
        if (text == null || text.trim().isEmpty()) {
            return defaultValue; // Если строка пустая, возвращаем значение по умолчанию
        }
        try {
            return Double.parseDouble(text); // Преобразуем строку в double
        } catch (NumberFormatException e) {
            System.out.println("Ошибка преобразования: " + text); // Логируем ошибку
            return defaultValue; // Если произошла ошибка, возвращаем значение по умолчанию
        }
    }


    private void rebuildTetrahedron(TetrahedronData data, VBox root) {
        if (tetraScene != null) {
            root.getChildren().remove(tetraScene);
        }
        tetraScene = createTetrahedronScene(data, 10);
        if (tetraScene != null) {
            root.getChildren().add(tetraScene);
        } else {
            showAlert("Ошибка", "Не удалось создать сцену тетраэдра.");
        }
    }

    public static SubScene createTetrahedronScene(TetrahedronData data, double scale) {
        MeshView meshView = createTetrahedronMesh(data, scale);

        Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
        Rotate rotateY = new Rotate(0, Rotate.Y_AXIS);
        meshView.getTransforms().addAll(
                new Translate(250, 200, 0),
                rotateX,
                rotateY
        );

        Group group = new Group(meshView);
        SubScene subScene = new SubScene(group, 500, 400, true, SceneAntialiasing.BALANCED);
        subScene.setTranslateY(-40);
        subScene.setTranslateX(120);
        subScene.setFill(Color.DARKSLATEGRAY);

        addZoomControl(group, subScene);
        addRotationControl(subScene, rotateX, rotateY);

        return subScene;
    }


    private static void addRotationControl(SubScene scene, Rotate rotateX, Rotate rotateY) {
        final double[] anchorX = new double[1];
        final double[] anchorY = new double[1];
        final double[] angleX = {0};
        final double[] angleY = {0};

        scene.setOnMousePressed(event -> {
            anchorX[0] = event.getSceneX();
            anchorY[0] = event.getSceneY();
        });

        scene.setOnMouseDragged(event -> {
            double deltaX = event.getSceneX() - anchorX[0];
            double deltaY = event.getSceneY() - anchorY[0];
            angleY[0] += deltaX * 0.5;
            angleX[0] -= deltaY * 0.5;
            rotateX.setAngle(angleX[0]);
            rotateY.setAngle(angleY[0]);
            anchorX[0] = event.getSceneX();
            anchorY[0] = event.getSceneY();
        });
    }

    private static void addZoomControl(Group group, SubScene scene) {
        Scale scale = new Scale(1.0, 1.0, 1.0, 250, 200, 0);
        group.getTransforms().add(scale);

        scene.setOnScroll(event -> {
            double zoomFactor = 1.05;
            if (event.getDeltaY() < 0) {
                // отдаление
                scale.setX(scale.getX() / zoomFactor);
                scale.setY(scale.getY() / zoomFactor);
                scale.setZ(scale.getZ() / zoomFactor);
            } else {
                // приближение
                scale.setX(scale.getX() * zoomFactor);
                scale.setY(scale.getY() * zoomFactor);
                scale.setZ(scale.getZ() * zoomFactor);
            }
        });
    }

    private TetrahedronData getTetrahedronDataFromFields(TextField[] fields, ColorPicker colorPicker) {
        return new TetrahedronData(
                parseDoubleWithDefault(fields[0].getText(), 0),
                parseDoubleWithDefault(fields[1].getText(), 0),
                parseDoubleWithDefault(fields[2].getText(), 0),
                parseDoubleWithDefault(fields[3].getText(), 0),
                parseDoubleWithDefault(fields[4].getText(), 0),
                parseDoubleWithDefault(fields[5].getText(), 0),
                parseDoubleWithDefault(fields[6].getText(), 0),
                parseDoubleWithDefault(fields[7].getText(), 0),
                parseDoubleWithDefault(fields[8].getText(), 0),
                parseDoubleWithDefault(fields[9].getText(), 0),
                parseDoubleWithDefault(fields[10].getText(), 0),
                parseDoubleWithDefault(fields[11].getText(), 0),
                toHex(colorPicker.getValue())
        );
    }

    //Проверим, что все поля заполнены и содержат корректные числа
    private boolean areAllFieldsValid(TextField[] fields) {
        for (TextField field : fields) {
            try {
                if (field.getText().trim().isEmpty()) return false;
                Double.parseDouble(field.getText().trim());
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return true;
    }

}




