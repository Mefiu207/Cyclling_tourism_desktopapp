package com.project.ui.buttons;

import com.project.springbootjavafx.models.Pokoje;
import com.project.springbootjavafx.models.Wycieczki;
import com.project.springbootjavafx.services.ListyHoteliService;
import com.project.springbootjavafx.services.PokojeService;
import com.project.springbootjavafx.services.WycieczkiService;
import com.project.ui.SpringContextHolder;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The {@code RightDeleteListaHoteliButton} class represents a specialized button located in the right sidebar
 * that facilitates the deletion of hotel lists for selected rooms associated with a chosen trip.
 *
 * <p>
 * When this button is clicked, the following actions occur:
 * </p>
 * <ol>
 *   <li>A dialog is displayed to allow the user to select a trip (Wycieczki) for which hotel lists will be deleted.</li>
 *   <li>After selecting a trip, another dialog appears, allowing the user to select one or more rooms (Pokoje)
 *       from those available with hotel lists associated with the selected trip.</li>
 *   <li>The hotel lists for the selected rooms are then deleted using the {@link ListyHoteliService}.</li>
 *   <li>The {@link PokojeService} is updated to mark that these rooms no longer have an associated hotel list.</li>
 *   <li>An information alert is displayed to indicate successful deletion, and the left sidebar is refreshed.</li>
 * </ol>
 *
 * <p>
 * Required services ({@link ListyHoteliService}, {@link WycieczkiService}, and {@link PokojeService}) are retrieved from the
 * Spring context via {@link SpringContextHolder}.
 * </p>
 */
public class RightDeleteListaHoteliButton extends Button {

    /**
     * Service for managing hotel lists.
     */
    private final ListyHoteliService listyHoteliService;

    /**
     * Service for managing trips.
     */
    private final WycieczkiService wycieczkiService;

    /**
     * Service for managing rooms.
     */
    private final PokojeService pokojeService;

    /**
     * The associated left sidebar button, used to trigger a refresh of the table view after deletion.
     */
    private final CustomLeftButton<?, ?> leftButton;

    /**
     * Constructs a new {@code RightDeleteListaHoteliButton} with the specified text and associated left sidebar button.
     *
     * @param name       the text to display on the button
     * @param leftButton the left sidebar button that is associated with this deletion action
     */
    public RightDeleteListaHoteliButton(String name, CustomLeftButton<?, ?> leftButton) {
        super(name);
        this.listyHoteliService = SpringContextHolder.getContext().getBean(ListyHoteliService.class);
        this.wycieczkiService = SpringContextHolder.getContext().getBean(WycieczkiService.class);
        this.pokojeService = SpringContextHolder.getContext().getBean(PokojeService.class);
        this.leftButton = leftButton;
        this.setOnAction(e -> onClick());
    }

    /**
     * Handles the click event for this button.
     *
     * <p>
     * The method performs the following actions:
     * </p>
     * <ol>
     *   <li>Invokes {@link #wybierzWycieczke()} to prompt the user to select a trip.</li>
     *   <li>Calls {@link #wybierzPokoje(Wycieczki)} to let the user select one or more rooms associated with the selected trip.</li>
     *   <li>For each selected room, deletes the hotel list using {@link ListyHoteliService#usunDlaPokoju(Pokoje)}.</li>
     *   <li>Updates the {@link PokojeService} to mark that the selected rooms no longer have an associated hotel list.</li>
     *   <li>Displays an information alert indicating success, then refreshes the left sidebar view by calling {@code leftButton.onClick()}.</li>
     * </ol>
     */
    public void onClick() {
        Wycieczki wycieczka = wybierzWycieczke();
        List<Pokoje> pokoje = wybierzPokoje(wycieczka);

        if (pokoje == null) return;

        pokoje.forEach(listyHoteliService::usunDlaPokoju);
        pokojeService.setListaHoteliFalse(pokoje);

        showAlert(Alert.AlertType.INFORMATION, "Sukces", "Usunięto listy hoteli");
        leftButton.onClick();
    }

    /**
     * Displays a dialog that allows the user to select one or more rooms (Pokoje) associated with the given trip (Wycieczki).
     *
     * <p>
     * The method creates a {@link ListView} displaying available rooms with hotel lists for the specified trip.
     * The user can select multiple rooms by clicking on the list items. Selected items are highlighted.
     * </p>
     *
     * @param wycieczka the selected trip for which available rooms are to be retrieved
     * @return a {@code List} of selected {@link Pokoje} objects, or {@code null} if no valid selection is made
     */
    private List<Pokoje> wybierzPokoje(Wycieczki wycieczka) {
        Dialog<List<Pokoje>> dialog = new Dialog<>();
        dialog.setTitle("Wybierz pokoje");
        dialog.setHeaderText("Wybierz pokoje dla których chcesz usunąć listę hoteli");

        ButtonType nextRoomButtonType = new ButtonType("Dalej", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(nextRoomButtonType, ButtonType.CANCEL);

        List<Pokoje> dostepnePokoje = pokojeService.getPokojeZListami(wycieczka);

        if (dostepnePokoje.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "Brak Dostępnych Pokoi",
                    "Dla wybranej wycieczki nie ma dostępnych pokoi z listami hoteli.");
            return null;
        }

        ListView<Pokoje> pokojeListView = new ListView<>();
        ObservableList<Pokoje> observablePokoje = FXCollections.observableArrayList(dostepnePokoje);
        pokojeListView.setItems(observablePokoje);
        pokojeListView.setPrefHeight(150);

        Set<Pokoje> wybranePokoje = new HashSet<>();

        pokojeListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Pokoje pokoj, boolean empty) {
                super.updateItem(pokoj, empty);
                if (empty || pokoj == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText("Pokój ID: " + pokoj.getId() + ", Typ: " + pokoj.getTypPokoju());
                    // Highlight if the room is selected.
                    if (wybranePokoje.contains(pokoj)) {
                        setStyle("-fx-background-color: #5db20e;");
                    } else {
                        setStyle("");
                    }
                }
            }
        });

        pokojeListView.setOnMouseClicked(event -> {
            Node node = event.getPickResult().getIntersectedNode(); // Node that was clicked
            while (node != null && !(node instanceof ListCell)) {
                node = node.getParent();
            }
            if (node instanceof ListCell cell && !cell.isEmpty()) {
                Pokoje pokoj = (Pokoje) cell.getItem();
                if (wybranePokoje.contains(pokoj)) {
                    wybranePokoje.remove(pokoj);
                } else {
                    wybranePokoje.add(pokoj);
                }
                pokojeListView.refresh();
            }
        });

        VBox roomContent = new VBox();
        roomContent.setSpacing(10);
        roomContent.getChildren().addAll(new Label("Wybierz pokoje:"), pokojeListView);

        dialog.getDialogPane().setContent(roomContent);
        Platform.runLater(pokojeListView::requestFocus);

        dialog.setResultConverter(dialogButtonRoom -> {
            if (dialogButtonRoom == nextRoomButtonType) {
                List<Pokoje> selectedPokoje = wybranePokoje.stream().toList();
                if (selectedPokoje == null || selectedPokoje.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Błąd", "Musisz wybrać przynajmniej jeden pokój.");
                    return null;
                }
                return new ArrayList<>(selectedPokoje);
            }
            return null;
        });

        Optional<List<Pokoje>> result = dialog.showAndWait();
        return result.orElse(null);
    }

    /**
     * Displays a dialog that allows the user to select a trip (Wycieczki) for which hotel lists should be deleted.
     *
     * <p>
     * The method creates a dialog with a {@link ComboBox} populated with available trips. It uses a
     * {@link StringConverter} to properly display trip names and supports auto-completion.
     * </p>
     *
     * @return the selected {@link Wycieczki} object, or {@code null} if no selection is made
     */
    private Wycieczki wybierzWycieczke() {
        Dialog<Wycieczki> dialog = new Dialog<>();
        dialog.setTitle("Wybór wycieczki");
        dialog.setHeaderText("Wybierz wycieczkę dla której chcesz usunąć listy hoteli");

        ButtonType nextButtonType = new ButtonType("Dalej", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(nextButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        ComboBox<Wycieczki> wycieczkiComboBox = new ComboBox<>();
        wycieczkiComboBox.setEditable(true);
        List<Wycieczki> wycieczkiList = wycieczkiService.getAll();
        ObservableList<Wycieczki> observableWycieczki = FXCollections.observableArrayList(wycieczkiList);
        wycieczkiComboBox.setItems(observableWycieczki);
        wycieczkiComboBox.setPromptText("Wybierz lub wpisz wycieczkę");

        // Set converter to properly display trip names
        wycieczkiComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Wycieczki wycieczki) {
                return wycieczki != null ? wycieczki.toString() : "";
            }

            @Override
            public Wycieczki fromString(String string) {
                return wycieczkiList.stream()
                        .filter(wycieczka -> wycieczka.toString().equalsIgnoreCase(string))
                        .findFirst()
                        .orElse(null);
            }
        });

        // Auto-completion implementation
        wycieczkiComboBox.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                wycieczkiComboBox.hide();
                wycieczkiComboBox.setItems(observableWycieczki);
            } else {
                List<Wycieczki> filtered = wycieczkiList.stream()
                        .filter(wycieczka -> wycieczka.toString().toLowerCase().contains(newValue.toLowerCase()))
                        .collect(Collectors.toList());
                wycieczkiComboBox.setItems(FXCollections.observableArrayList(filtered));
                wycieczkiComboBox.show();
            }
        });

        grid.add(new Label("Wycieczka:"), 0, 0);
        grid.add(wycieczkiComboBox, 1, 0);

        dialog.getDialogPane().setContent(grid);
        Platform.runLater(wycieczkiComboBox::requestFocus);

        dialog.showAndWait();
        return wycieczkiComboBox.getValue();
    }

    /**
     * Displays an alert dialog with the specified type, title, and message.
     *
     * <p>
     * This method ensures that the alert is displayed on the JavaFX Application Thread by using
     * {@link Platform#runLater(Runnable)}.
     * </p>
     *
     * @param alertType the type of alert to display
     * @param title     the title of the alert dialog
     * @param message   the content message of the alert dialog
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(alertType);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}
