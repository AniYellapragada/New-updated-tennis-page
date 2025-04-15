module com.tennis {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.tennis to javafx.fxml;
    exports com.tennis;
}
