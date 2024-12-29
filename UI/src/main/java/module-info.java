module com.project.ui {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.project.ui to javafx.fxml;
    exports com.project.ui;
}