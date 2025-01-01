module com.project.ui {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires spring.context;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires static lombok;

    opens com.project.ui to javafx.fxml;
    exports com.project.ui;
}