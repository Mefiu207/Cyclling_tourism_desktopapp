package com.project.ui.buttons;

import com.project.springbootjavafx.services.ServicesInterface;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import com.project.springbootjavafx.utils.Pair;

import java.util.List;
import java.util.function.Consumer;
import java.util.ArrayList;


public class CustomLeftButton<T, ID> extends Button {

    private ServicesInterface<T, ID> services;
    private Consumer<TableView<T>> onTableCreated;

    // Lista pól: nazwa pola i jego typ (potrzebne do generowania pól tabeli)
    private ArrayList<Pair<String, String>> fieldsTypes;

    public CustomLeftButton(ServicesInterface<T, ID> services, String name, Consumer<TableView<T>> onTableCreated) {
        super(name);
        this.services = services;
        this.onTableCreated = onTableCreated;

        this.fieldsTypes = services.getFieldsTypes();

        this.setOnAction(e -> onClick());
    }

    // Tworzy tabele z danych z services
    private void onClick() {
        TableView<T> tableView = new TableView<>();

        for (Pair<String, String> fieldPair : fieldsTypes) {
            String fieldName = fieldPair.getKey();

            TableColumn<T, Object> column = new TableColumn<>(fieldName);

            column.setCellValueFactory(new PropertyValueFactory<>(fieldName));

            tableView.getColumns().add(column);
        }

        // Pobranie danych z serwisu i ustawienie ich w tabeli
        List<T> data = services.getAll();
        tableView.setItems(FXCollections.observableArrayList(data));

        // Przekazanie tabeli do callbacku
        if (onTableCreated != null) {
            onTableCreated.accept(tableView);
        }
    }

    public ServicesInterface<T,ID> getServices() {
        return services;
    }
}