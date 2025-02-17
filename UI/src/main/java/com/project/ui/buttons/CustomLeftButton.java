package com.project.ui.buttons;

import com.project.springbootjavafx.services.AbstractServices;
import com.project.ui.MainContent;
import com.project.ui.RightSidebar;
import com.project.ui.SpringContextHolder;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.CheckBoxTableCell;
import com.project.springbootjavafx.utils.Pair;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.lang.reflect.Method;
import java.util.List;
import java.util.ArrayList;
import static org.springframework.util.StringUtils.capitalize;

/**
 * A generic left sidebar button that operates on any entity type.
 *
 * <p>
 * This button is designed to be placed in the left sidebar of the user interface and, upon being clicked,
 * dynamically generates a {@link TableView} for a given entity type. It retrieves the entity's field information
 * from an {@link AbstractServices} instance and creates table columns accordingly. It also updates the right sidebar
 * with context-sensitive actions based on the selected button.
 * </p>
 *
 * <p>
 * Generic type parameters:
 * </p>
 * <ul>
 *   <li><b>T</b> - the type of the entity associated with this button</li>
 *   <li><b>ID</b> - the type of the entity's identifier</li>
 * </ul>
 *
 * <p>Annotations used:</p>
 * <ul>
 *   <li>{@code @Getter} (from Lombok) - automatically generates getter methods for all fields.</li>
 *   <li>{@code @Setter} (from Lombok) - automatically generates setter methods for all fields.</li>
 *   <li>{@code @NoArgsConstructor} (from Lombok) - automatically generates a no-argument constructor.</li>
 * </ul>
 */
@Getter
@Setter
@NoArgsConstructor
public class CustomLeftButton<T, ID> extends Button {

    /**
     * The service associated with this button, used to retrieve data and field types.
     */
    protected AbstractServices<T, ID> services;

    /**
     * A list of field names and their types (as strings) used for dynamically generating table columns.
     */
    protected ArrayList<Pair<String, String>> fieldsTypes;

    /**
     * Constructs a new {@code CustomLeftButton} with the specified service and button name.
     *
     * @param services the service used to fetch data and field metadata for the entity type
     * @param name     the text to be displayed on the button
     */
    public CustomLeftButton(AbstractServices<T, ID> services, String name) {
        super(name);
        this.services = services;
        this.fieldsTypes = services.getFieldsTypes();
        this.setOnAction(e -> onClick());
    }

    /**
     * Handles the click event for this button.
     *
     * <p>
     * When clicked, this method performs the following actions:
     * </p>
     * <ol>
     *   <li>Retrieves the {@link MainContent} bean from the Spring context.</li>
     *   <li>Retrieves the {@link RightSidebar} bean from the Spring context and updates its buttons
     *       based on the current selection.</li>
     *   <li>Creates a new {@link TableView} for the entity type T.</li>
     *   <li>Iterates through the list of field types, dynamically creating a table column for each field.
     *       Boolean fields are handled with a checkbox cell; all other types use a standard property cell.</li>
     *   <li>Fetches all data of type T from the associated service and populates the table view.</li>
     *   <li>Updates the main content area with the newly created table view.</li>
     * </ol>
     */
    public void onClick() {

        MainContent mainContent = SpringContextHolder.getContext().getBean(MainContent.class);

        // Update the right sidebar based on the selected button
        RightSidebar rightSidebar = SpringContextHolder.getContext().getBean(RightSidebar.class);
        rightSidebar.updateButtons(this);

        TableView<T> tableView = new TableView<>();

        for (Pair<String, String> fieldPair : fieldsTypes) {
            String fieldName = fieldPair.getKey();
            String fieldType = fieldPair.getValue();

            if ("boolean".equalsIgnoreCase(fieldType) || "Boolean".equalsIgnoreCase(fieldType)) {
                TableColumn<T, Boolean> booleanColumn = new TableColumn<>(fieldName);
                booleanColumn.setCellValueFactory(param -> {
                    try {
                        Method getter = param.getValue().getClass().getMethod("get" + capitalize(fieldName));
                        return new SimpleBooleanProperty((Boolean) getter.invoke(param.getValue()));
                    } catch (Exception e) {
                        return new SimpleBooleanProperty(false);
                    }
                });
                booleanColumn.setCellFactory(CheckBoxTableCell.forTableColumn(booleanColumn));
                tableView.getColumns().add(booleanColumn);
            } else {
                TableColumn<T, Object> column = new TableColumn<>(fieldName);
                column.setCellValueFactory(new PropertyValueFactory<>(fieldName));
                tableView.getColumns().add(column);
            }
        }

        // Retrieve data from the service and set it into the table
        List<T> data = services.getAll();
        tableView.setItems(FXCollections.observableArrayList(data));

        mainContent.updateContent(tableView);
    }
}
