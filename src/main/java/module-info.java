module filmspool {
    requires javafx.controls;
    requires javafx.fxml;

    // Erlaube JavaFX, auf deine Controller-Klassen zuzugreifen
    opens naik05.filmspool to javafx.fxml;

    // Exportiere dein Hauptpaket, damit die App starten kann
    exports naik05.filmspool;
}