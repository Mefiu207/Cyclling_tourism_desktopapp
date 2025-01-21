package com.project.ui.buttons;

import com.project.springbootjavafx.services.AbstractServices;

import com.project.ui.MainContent;
import com.project.ui.RightSidebar;
import com.project.ui.SpringContextHolder;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import com.project.springbootjavafx.utils.Pair;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.ArrayList;


// Generyczny przycisk w lewym panelu działający dla dowolnego typu encji
@Getter
@Setter
@NoArgsConstructor
public class CustomLeftButton<T, ID> extends Button {

    protected AbstractServices<T, ID> services;


    // Lista pól: nazwa pola i jego typ (potrzebne do generowania pól tabeli)
    protected ArrayList<Pair<String, String>> fieldsTypes;


    public CustomLeftButton(AbstractServices<T, ID> services, String name) {
        super(name);

        this.services = services;

        this.fieldsTypes = services.getFieldsTypes();

        this.setOnAction(e -> onClick());
    }

    // Tworzy tabele z danych z services
    public void onClick() {


        MainContent mainContent = SpringContextHolder.getContext().getBean(MainContent.class);

        // Upadatuje rightSidebar w zależności od wybranego przycisku
        RightSidebar rightSidebar = SpringContextHolder.getContext().getBean(RightSidebar.class);
        rightSidebar.updateButtons(this);


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

        mainContent.updateContent(tableView);
    }

}