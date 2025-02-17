package com.project.ui.buttons.adding;

import com.project.springbootjavafx.models.*;
import com.project.springbootjavafx.services.*;
import com.project.ui.SpringContextHolder;
import com.project.ui.buttons.CustomLeftButton;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The {@code AddListaHoteliButton} class extends {@link Button} and provides functionality for adding hotel lists
 * to selected rooms for a chosen trip.
 *
 * <p>
 * When this button is clicked, the following sequence of dialogs is presented:
 * </p>
 * <ol>
 *   <li>
 *     A dialog is displayed to select a trip (Wycieczki) via an editable {@link ComboBox} that supports auto-completion.
 *   </li>
 *   <li>
 *     Next, a dialog is shown to allow the user to select one or more rooms (Pokoje) for the selected trip.
 *   </li>
 *   <li>
 *     Finally, a dialog is presented for selecting hotels for each night of the trip. For each night (derived from
 *     the cities associated with the trip type), the user chooses a hotel from a {@link ComboBox} with auto-completion.
 *   </li>
 * </ol>
 *
 * <p>
 * Once the dialogs are completed, the selected hotel lists are added using {@link ListyHoteliService}, and the rooms are updated
 * via {@link PokojeService}. A success alert is displayed, and the left sidebar view is refreshed.
 * </p>
 */
public class AddListaHoteliButton extends Button {

    private final ListyHoteliService listyHoteliService;
    private final WycieczkiService wycieczkiService;
    private final PokojeService pokojeService;
    private final TypyWycieczekService typyWycieczekService;
    private final HoteleService hoteleService;
    private final CustomLeftButton<?, ?> leftButton;

    /**
     * Constructs a new {@code AddListaHoteliButton} with the specified text and associated left sidebar button.
     *
     * @param name       the text to display on the button
     * @param leftButton the left sidebar button that is used to refresh the view after hotel lists are added
     */
    public AddListaHoteliButton(String name, CustomLeftButton<?, ?> leftButton) {
        super(name);
        this.listyHoteliService = SpringContextHolder.getContext().getBean(ListyHoteliService.class);
        this.wycieczkiService = SpringContextHolder.getContext().getBean(WycieczkiService.class);
        this.pokojeService = SpringContextHolder.getContext().getBean(PokojeService.class);
        this.typyWycieczekService = SpringContextHolder.getContext().getBean(TypyWycieczekService.class);
        this.hoteleService = SpringContextHolder.getContext().getBean(HoteleService.class);
        this.leftButton = leftButton;
        this.setOnAction(e -> onClick());
    }

    /**
     * Handles the button click event to add hotel lists.
     *
     * <p>
     * This method executes the following steps:
     * </p>
     * <ol>
     *   <li>Displays a dialog to select a trip using {@link #wybierzWycieczke()}.</li>
     *   <li>Displays a dialog to select one or more rooms for the selected trip using {@link #wybierzPokoje(Wycieczki)}.</li>
     *   <li>Displays a dialog to select hotels for each night of the trip for the selected rooms using {@link #wybierzHotele(List)}.</li>
     *   <li>Adds the selected hotel lists via {@link ListyHoteliService} and updates the rooms with {@link PokojeService}.</li>
     *   <li>Shows a success alert and refreshes the left sidebar view.</li>
     * </ol>
     */
    public void onClick() {
        Wycieczki wycieczka = wybierzWycieczke();
        List<Pokoje> wybranePokoje = wybierzPokoje(wycieczka);

        if (wybranePokoje == null) return;

        List<ListyHoteli> listaHoteli = wybierzHotele(wybranePokoje);

        if (listaHoteli == null) return;

        // Add each hotel list via the service
        listaHoteli.forEach(listyHoteliService::add);

        // Mark the selected rooms as having a hotel list
        pokojeService.setListaHoteliTrue(wybranePokoje);

        showAlert(Alert.AlertType.INFORMATION, "Sukces", "Dodano listy hoteli");
        leftButton.onClick();
    }

    /**
     * Displays a dialog to allow the user to select hotels for each night of the trip for the selected rooms.
     *
     * <p>
     * For each night of the trip (based on the cities associated with the trip type), a label with the city name and a
     * {@link ComboBox} for selecting a hotel are added to a grid layout. The selected hotels are used to create a list
     * of {@link ListyHoteli} objects, one for each combination of room and night.
     * </p>
     *
     * @param wybranePokoje the list of selected rooms (Pokoje) for which hotel lists will be created
     * @return a list of {@link ListyHoteli} objects representing the hotel lists, or {@code null} if the dialog is cancelled
     */
    private List<ListyHoteli> wybierzHotele(List<Pokoje> wybranePokoje) {
        Dialog<List<ListyHoteli>> dialog = new Dialog<>();
        dialog.setTitle("Listy hoteli");
        dialog.setHeaderText("Ustaw hotele dla nocy wycieczki");

        ButtonType confirmButton = new ButtonType("Dalej", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButton, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        // Retrieve trip type and corresponding cities for the nights of the trip
        TypyWycieczek typWycieczki = wybranePokoje.get(0).getWycieczka().getTypWycieczki();
        List<MiastaWycieczek> miastaWycieczki = typyWycieczekService.getMiastaWycieczki(typWycieczki);

        // Prepare a list to hold selected hotels for each night
        List<Hotele> hoteleWycieczki = new ArrayList<>(Collections.nCopies(miastaWycieczki.size(), null));

        // For each night, add a label for the city and a ComboBox for hotel selection
        for (int i = 0; i < miastaWycieczki.size(); i++) {
            Label miastoLabel = new Label(miastaWycieczki.get(i).getMiasta().getMiasto());
            TextField hotelField = new TextField();
            hotelField.setPromptText("Kod hotelu");

            List<Hotele> hoteleMiasta = hoteleService.getHoteleMiasta(miastaWycieczki.get(i).getMiasta());
            ObservableList<Hotele> observableHotele = FXCollections.observableArrayList(hoteleMiasta);

            ComboBox<Hotele> hotelCombo = new ComboBox<>(observableHotele);
            hotelCombo.setItems(observableHotele);
            hotelCombo.setPromptText("Wybierz lub wpisz hotel");
            hotelCombo.setEditable(true);

            hotelCombo.setConverter(new StringConverter<Hotele>() {
                @Override
                public String toString(Hotele hotele) {
                    return hotele == null ? "" : hotele.getKod();
                }

                @Override
                public Hotele fromString(String s) {
                    return hoteleMiasta.stream()
                            .filter(hotel -> hotel.getKod().equalsIgnoreCase(s))
                            .findFirst()
                            .orElse(null);
                }
            });

            hotelCombo.getEditor().textProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal == null || newVal.isEmpty()) {
                    hotelCombo.hide();
                    hotelCombo.setItems(observableHotele);
                } else {
                    List<Hotele> filtered = hoteleMiasta.stream()
                            .filter(hotel -> hotel.toString().toLowerCase().contains(newVal.toLowerCase()))
                            .collect(Collectors.toList());
                    hotelCombo.setItems(FXCollections.observableArrayList(filtered));
                    hotelCombo.show();
                }
            });

            final int index = i;
            hotelCombo.valueProperty().addListener((a, old, New) -> {
                hoteleWycieczki.set(index, New);
            });

            grid.add(miastoLabel, 0, i);
            grid.add(hotelCombo, 1, i);
        }

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == confirmButton) {
                List<ListyHoteli> listyDoZwrotu = new ArrayList<>();

                // For each selected room, create hotel lists for each night
                for (Pokoje pokoj : wybranePokoje) {
                    for (int j = 0; j < hoteleWycieczki.size(); j++) {
                        ListyHoteli nowaLista = new ListyHoteli();
                        nowaLista.setPokoj(pokoj);
                        nowaLista.setMiastoWycieczki(miastaWycieczki.get(j));
                        nowaLista.setHotel(hoteleWycieczki.get(j));
                        listyDoZwrotu.add(nowaLista);
                    }
                }
                return listyDoZwrotu;
            }
            return null;
        });

        Optional<List<ListyHoteli>> optionalResult = dialog.showAndWait();

        return optionalResult.orElse(null);
    }

    /**
     * Displays a dialog to allow the user to select one or more rooms (Pokoje) for which a hotel list will be created.
     *
     * <p>
     * The dialog shows a list of available rooms for the selected trip using a {@link ListView}. The user may select one
     * or more rooms, and the selected rooms are returned as a list.
     * </p>
     *
     * @param wycieczka the selected trip (Wycieczki) used to filter available rooms
     * @return a list of selected {@link Pokoje} objects, or {@code null} if no rooms are selected or if the dialog is cancelled
     */
    private List<Pokoje> wybierzPokoje(Wycieczki wycieczka) {
        Dialog<List<Pokoje>> dialog = new Dialog<>();
        dialog.setTitle("Wybierz pokoje");
        dialog.setHeaderText("Wybierz pokoje dla których chcesz stworzyć listę hoteli");

        ButtonType nextRoomButtonType = new ButtonType("Dalej", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(nextRoomButtonType, ButtonType.CANCEL);

        List<Pokoje> dostepnePokoje = pokojeService.getPokojeWycieczki(wycieczka);

        if (dostepnePokoje.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "Brak Dostępnych Pokoi",
                    "Dla wybranej wycieczki nie ma dostępnych pokoi.");
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
                    if (wybranePokoje.contains(pokoj)) {
                        setStyle("-fx-background-color: #5db20e;");
                    } else {
                        setStyle("");
                    }
                }
            }
        });

        pokojeListView.setOnMouseClicked(event -> {
            Node node = event.getPickResult().getIntersectedNode();
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
     * Displays a dialog to allow the user to select a trip (Wycieczki) for which the hotel list will be created.
     *
     * <p>
     * The dialog contains an editable {@link ComboBox} with auto-completion that allows the user to select or type a trip.
     * </p>
     *
     * @return the selected {@link Wycieczki} object, or {@code null} if no trip is selected
     */
    private Wycieczki wybierzWycieczke() {
        Dialog<Wycieczki> dialog = new Dialog<>();
        dialog.setTitle("Wybór wycieczki");
        dialog.setHeaderText("Wybierz wycieczkę dla której chcesz stworzyć listę hoteli");

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

        wycieczkiComboBox.setConverter(new StringConverter<Wycieczki>() {
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
     * This method ensures that the alert is displayed on the JavaFX Application Thread using
     * {@link Platform#runLater(Runnable)}.
     * </p>
     *
     * @param alertType the type of alert to display
     * @param title     the title of the alert dialog
     * @param message   the message to display in the alert dialog
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
