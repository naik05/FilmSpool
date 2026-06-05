package naik05.filmspool;

import javafx.scene.image.Image;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;

public class Main extends Application {

    private String selectedFolderPath = null;
    private final ExifToolService exifToolService = new ExifToolService();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("FilmSpool");
        try {
            Image appIcon = new Image(getClass().getResourceAsStream("/icon.png"));
            primaryStage.getIcons().add(appIcon);
        } catch (Exception e) {
            System.out.println("Icon Could Not Be Loaded.");
        }

        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(10);

        TextField makeField = new TextField();
        makeField.setPromptText("e.g. Olympus");
        TextField modelField = new TextField();
        modelField.setPromptText("e.g. OM-1");
        TextField isoField = new TextField();
        isoField.setPromptText("e.g. 400");
        TextField filmField = new TextField();
        filmField.setPromptText("Kodak Tri-X 400");

        TextField lensMakeField = new TextField();
        lensMakeField.setPromptText("e.g. Olympus");
        TextField lensModelField = new TextField();
        lensModelField.setPromptText("e.g. Zuiko 50mm f/1.8");
        TextField focalLengthField = new TextField();
        focalLengthField.setPromptText("e.g. 50");
        TextField apertureField = new TextField();
        apertureField.setPromptText("e.g. 2.8");

        DatePicker datePicker = new DatePicker();
        TextField timeField = new TextField();
        timeField.setPromptText("e.g. 14:30");

        grid.add(new Label("Camera Manufacturer:"), 0, 0); grid.add(makeField, 1, 0);
        grid.add(new Label("Camera Model:"), 0, 1);     grid.add(modelField, 1, 1);
        grid.add(new Label("ISO:"), 0, 2);               grid.add(isoField, 1, 2);
        grid.add(new Label("Film (Comment):"), 0, 3);    grid.add(filmField, 1, 3);

        grid.add(new Label("Lens Manufacturer:"), 2, 0); grid.add(lensMakeField, 3, 0);
        grid.add(new Label("Lens Model:"), 2, 1);     grid.add(lensModelField, 3, 1);
        grid.add(new Label("Focal Length (mm):"), 2, 2);     grid.add(focalLengthField, 3, 2);
        grid.add(new Label("Aperture (f/):"), 2, 3);         grid.add(apertureField, 3, 3);

        grid.add(new Label("Date (Optional):"), 2, 4);    grid.add(datePicker, 3, 4);
        grid.add(new Label("Time (Optional):"), 2, 5);  grid.add(timeField, 3, 5);



        Label dropLabel = new Label("Drop Folder Here\n(Drag & Drop)");
        dropLabel.setStyle("-fx-text-alignment: center; -fx-text-fill: #555; -fx-font-weight: bold;");

        Button browseBtn = new Button("Or Search Folder...");
        browseBtn.setStyle("-fx-cursor: hand;");

        VBox dropZone = new VBox(10, dropLabel, browseBtn);
        dropZone.setPrefHeight(120);
        dropZone.setAlignment(Pos.CENTER);
        dropZone.setStyle("-fx-border-color: #aaa; -fx-border-width: 2; -fx-border-style: dashed; -fx-background-color: #f4f4f4; -fx-border-radius: 5;");


        Label statusLabel = new Label("Waiting For Folder To Scan...");

        ProgressIndicator spinner = new ProgressIndicator();
        spinner.setPrefSize(20, 20); // Schön klein
        spinner.setVisible(false);   // Unsichtbar am Start


        HBox statusBox = new HBox(10, spinner, statusLabel);
        statusBox.setAlignment(Pos.CENTER_LEFT);


        dropZone.setOnDragOver(event -> {
            if (event.getDragboard().hasFiles()) event.acceptTransferModes(TransferMode.COPY);
            event.consume();
        });

        dropZone.setOnDragDropped(event -> {
            boolean success = false;
            if (event.getDragboard().hasFiles()) {
                File file = event.getDragboard().getFiles().get(0);
                if (file.isDirectory()) {
                    onFolderSelected(file, dropZone, statusLabel);
                    success = true;
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });


        browseBtn.setOnAction(e -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Choose Folder To Scan");

            File selectedDirectory = directoryChooser.showDialog(primaryStage);

            if (selectedDirectory != null) {
                onFolderSelected(selectedDirectory, dropZone, statusLabel);
            }
        });



        Button applyBtn = new Button("Apply Metadata");
        applyBtn.setMaxWidth(Double.MAX_VALUE);
        applyBtn.setStyle("-fx-font-size: 14px; -fx-padding: 10px; -fx-base: #2196F3;");

        applyBtn.setOnAction(e -> {
            if (selectedFolderPath == null) {
                statusLabel.setText("Error: Please Choose a Folder First!");
                return;
            }


            statusLabel.setText("Processing Images... Please wait.");
            spinner.setVisible(true);
            applyBtn.setDisable(true); // Verhindert Doppel-Klicks
            grid.setDisable(true);     // Sperrt die Eingabefelder
            dropZone.setDisable(true); // Sperrt die Drop-Zone


            Preset p = new Preset();
            p.setMake(makeField.getText());
            p.setModel(modelField.getText());
            p.setIso(isoField.getText());
            p.setComment(filmField.getText());

            p.setLensMake(lensMakeField.getText());
            p.setLensModel(lensModelField.getText());
            p.setFocalLength(focalLengthField.getText());
            p.setfNumber(apertureField.getText());

            LocalDate date = datePicker.getValue();
            String time = timeField.getText().trim();

            if (date != null) {
                String formattedDate = date.toString().replace("-", ":");
                String formattedTime = time.isEmpty() ? "12:00:00" : time + ":00";
                p.setDateTime(formattedDate + " " + formattedTime);
            }


            CompletableFuture.runAsync(() -> {

                exifToolService.applyPresetToFolder(selectedFolderPath, p);

            }).thenRun(() -> {

                Platform.runLater(() -> {
                    spinner.setVisible(false);
                    applyBtn.setDisable(false);
                    grid.setDisable(false);
                    dropZone.setDisable(false);
                    statusLabel.setText("Done! Metadata Written Successfully.");
                    dropZone.setStyle("-fx-border-color: #aaa; -fx-border-width: 2; -fx-border-style: dashed; -fx-background-color: #f4f4f4; -fx-border-radius: 5;");
                });
            });
        });


        VBox root = new VBox(20, grid, dropZone, applyBtn, statusBox);
        root.setPadding(new Insets(20));

        primaryStage.setScene(new Scene(root, 650, 480));
        primaryStage.show();
    }

    private void onFolderSelected(File folder, VBox dropZone, Label statusLabel) {
        selectedFolderPath = folder.getAbsolutePath();
        statusLabel.setText("Folder Ready: " + folder.getName());
        dropZone.setStyle("-fx-border-color: #4CAF50; -fx-border-width: 2; -fx-border-style: solid; -fx-background-color: #E8F5E9; -fx-border-radius: 5;");
    }

    public static void main(String[] args) {
        launch(args);
    }
}