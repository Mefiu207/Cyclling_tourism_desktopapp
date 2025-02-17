package com.project.ui.buttons.adding;

import com.project.springbootjavafx.exceptions.WrongLetterException;
import com.project.springbootjavafx.models.Ceny;
import com.project.springbootjavafx.models.Klienci;
import com.project.springbootjavafx.models.Pokoje;
import com.project.springbootjavafx.models.Wycieczki;
import com.project.springbootjavafx.services.KlienciService;
import com.project.springbootjavafx.services.PokojeService;
import com.project.springbootjavafx.services.WycieczkiService;
import com.project.ui.SpringContextHolder;
import com.project.ui.buttons.CustomLeftButton;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The {@code AddKlientButton} class extends {@link Button} and provides functionality
 * for adding a new client (Klienci) to the system.
 *
 * <p>
 * When this button is clicked, a series of dialogs are presented to the user:
 * </p>
 * <ol>
 *   <li>
 *     The first dialog collects the client's personal data: first name (imie), last name (nazwisko),
 *     and the associated trip (Wycieczki). The trip selection uses an auto-completing {@link ComboBox}.
 *   </li>
 *   <li>
 *     If the first dialog is successfully completed, the second dialog allows the user to select an available room (Pokoje)
 *     for the chosen trip. The available rooms are those that are not yet fully occupied.
 *   </li>
 *   <li>
 *     If a room is selected, a third dialog is displayed where the user can select additional services
 *     (e.g. discount, bicycle, E-Bike, extra nights before/after, half board (HB)) via checkboxes.
 *   </li>
 * </ol>
 *
 * <p>
 * After collecting all necessary data, a new {@link Klienci} object is created, the payable amount is calculated
 * based on the room type and selected services (using pricing information from {@link Ceny}), and the client is added to the database.
 * Upon successful addition, the left sidebar is refreshed and an information alert is shown.
 * </p>
 *
 * <p>
 * The required services ({@link WycieczkiService}, {@link PokojeService}, and {@link KlienciService})
 * are retrieved from the Spring context via {@link SpringContextHolder}. The {@code CustomLeftButton}
 * passed in the constructor is used to refresh the view after adding the client.
 * </p>
 */
public class AddKlientButton extends Button {

    /**
     * Service for managing trips.
     */
    private final WycieczkiService wycieczkiService;

    /**
     * Service for managing rooms.
     */
    private final PokojeService pokojeService;

    /**
     * Service for managing clients.
     */
    private final KlienciService klienciService;

    /**
     * Reference to the left sidebar button used to refresh the view after adding a client.
     */
    private final CustomLeftButton<?, ?> leftButton;

    /**
     * Constructs a new {@code AddKlientButton} with the specified text and associated left sidebar button.
     *
     * @param name       the text to display on the button
     * @param leftButton the left sidebar button that provides context and services for client operations
     */
    public AddKlientButton(String name, CustomLeftButton<?, ?> leftButton) {
        super(name);
        this.leftButton = leftButton;
        this.wycieczkiService = SpringContextHolder.getContext().getBean(WycieczkiService.class);
        this.pokojeService = SpringContextHolder.getContext().getBean(PokojeService.class);
        this.klienciService = SpringContextHolder.getContext().getBean(KlienciService.class);
        this.setOnAction(e -> onClick());
    }

    /**
     * Handles the click event for this button.
     *
     * <p>
     * This method initiates a sequence of dialogs:
     * </p>
     * <ol>
     *   <li>
     *     The first dialog collects the client's first name, last name, and a trip selection.
     *     The trip selection is done using an editable {@link ComboBox} with auto-completion.
     *     If any field is missing, an error alert is shown.
     *   </li>
     *   <li>
     *     Upon successful input, the second dialog is shown to allow the user to select a room (Pokoje)
     *     associated with the chosen trip that has available space.
     *   </li>
     *   <li>
     *     If a room is selected, the third dialog is displayed for choosing additional services
     *     (discount, bicycle, E-Bike, extra nights before, extra nights after, HB) using checkboxes.
     *   </li>
     *   <li>
     *     Finally, a new {@link Klienci} object is created, pricing is calculated based on the selected room and services,
     *     and the client is added to the database. The left sidebar is refreshed and a success alert is displayed.
     *   </li>
     * </ol>
     */
    public void onClick() {
        // Create the first dialog to collect client's personal data
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Dodaj Nowego Klienta");
        dialog.setHeaderText("Wprowadź dane nowego klienta.");

        // Set dialog buttons
        ButtonType nextButtonType = new ButtonType("Dalej", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(nextButtonType, ButtonType.CANCEL);

        // Create the form grid for the first dialog
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        // Form fields
        TextField imieField = new TextField();
        imieField.setPromptText("Imię");

        TextField nazwiskoField = new TextField();
        nazwiskoField.setPromptText("Nazwisko");

        // Create an editable ComboBox for selecting a trip with auto-completion
        ComboBox<Wycieczki> wycieczkiComboBox = new ComboBox<>();
        wycieczkiComboBox.setEditable(true);
        List<Wycieczki> wycieczkiList = wycieczkiService.getAll();
        ObservableList<Wycieczki> observableWycieczki = FXCollections.observableArrayList(wycieczkiList);
        wycieczkiComboBox.setItems(observableWycieczki);
        wycieczkiComboBox.setPromptText("Wybierz lub wpisz wycieczkę");

        // Set converter to correctly display trip names in the ComboBox
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

        // Add form fields to the grid
        grid.add(new Label("Imię:"), 0, 0);
        grid.add(imieField, 1, 0);
        grid.add(new Label("Nazwisko:"), 0, 1);
        grid.add(nazwiskoField, 1, 1);
        grid.add(new Label("Wycieczka:"), 0, 2);
        grid.add(wycieczkiComboBox, 1, 2);

        dialog.getDialogPane().setContent(grid);

        // Set initial focus to the first field
        Platform.runLater(imieField::requestFocus);

        // Handle the "Dalej" button: validate fields and return true if valid
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == nextButtonType) {
                String imie = imieField.getText().trim();
                String nazwisko = nazwiskoField.getText().trim();
                Wycieczki wycieczka = wycieczkiComboBox.getValue();

                // Validate that all fields are filled
                if (imie.isEmpty() || nazwisko.isEmpty() || wycieczka == null) {
                    showAlert(Alert.AlertType.ERROR, "Błąd", "Wszystkie pola muszą być wypełnione.");
                    return false;
                }
                return true; // Continue to next dialog
            }
            return false;
        });

        // Show the first dialog and check the result
        Optional<Boolean> result = dialog.showAndWait();

        if (result.isPresent() && result.get()) {
            // Temporarily store the client's data
            String tempImie = imieField.getText().trim();
            String tempNazwisko = nazwiskoField.getText().trim();
            Wycieczki tempWycieczka = wycieczkiComboBox.getValue();

            // Create the second dialog to choose a room
            Dialog<Void> roomDialog = new Dialog<>();
            roomDialog.setTitle("Wybierz Pokój");
            roomDialog.setHeaderText("Wybierz pokój dla klienta.");

            // Set dialog buttons for room selection
            ButtonType nextRoomButtonType = new ButtonType("Dalej", ButtonBar.ButtonData.OK_DONE);
            roomDialog.getDialogPane().getButtonTypes().addAll(nextRoomButtonType, ButtonType.CANCEL);

            // Retrieve available rooms for the selected trip that have available space
            List<Pokoje> dostepnePokoje = pokojeService.getAll().stream()
                    .filter(pokoj -> pokoj.getWycieczka().equals(tempWycieczka))
                    .filter(pokoj -> pokoj.getIlKlientow() < pokoj.getIlMiejsc())
                    .collect(Collectors.toList());

            if (dostepnePokoje.isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION, "Brak Dostępnych Pokoi",
                        "Dla wybranej wycieczki nie ma dostępnych pokoi.");
                return;
            }

            // Create a ListView for room selection
            ListView<Pokoje> pokojeListView = new ListView<>();
            ObservableList<Pokoje> observablePokoje = FXCollections.observableArrayList(dostepnePokoje);
            pokojeListView.setItems(observablePokoje);
            pokojeListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            pokojeListView.setPrefHeight(150);

            // Set cell factory to display room information
            pokojeListView.setCellFactory(param -> new ListCell<>() {
                @Override
                protected void updateItem(Pokoje pokoj, boolean empty) {
                    super.updateItem(pokoj, empty);
                    if (empty || pokoj == null) {
                        setText(null);
                    } else {
                        setText("Pokój: " + pokoj.getId() + " (" + pokoj.getTypPokoju() + ")");
                    }
                }
            });

            VBox roomContent = new VBox();
            roomContent.setSpacing(10);
            roomContent.getChildren().addAll(new Label("Wybierz pokój:"), pokojeListView);

            roomDialog.getDialogPane().setContent(roomContent);
            Platform.runLater(pokojeListView::requestFocus);

            // Handle the room selection dialog result
            roomDialog.setResultConverter(dialogButtonRoom -> {
                if (dialogButtonRoom == nextRoomButtonType) {
                    Pokoje selectedPokoj = pokojeListView.getSelectionModel().getSelectedItem();
                    if (selectedPokoj == null) {
                        showAlert(Alert.AlertType.ERROR, "Błąd", "Musisz wybrać pokój.");
                        return null;
                    }

                    // Create the third dialog to select additional services for the client
                    Dialog<Void> servicesDialog = new Dialog<>();
                    servicesDialog.setTitle("Dodatkowe Usługi");
                    servicesDialog.setHeaderText("Wybierz, z których usług korzysta klient.");

                    // Set dialog buttons for additional services
                    ButtonType addServicesButtonType = new ButtonType("Dodaj", ButtonBar.ButtonData.OK_DONE);
                    servicesDialog.getDialogPane().getButtonTypes().addAll(addServicesButtonType, ButtonType.CANCEL);

                    // Create checkboxes for each additional service
                    CheckBox ulgaCheckBox = new CheckBox("Ulga");
                    CheckBox rowerCheckBox = new CheckBox("Rower");
                    CheckBox eBikeCheckBox = new CheckBox("E-Bike");
                    CheckBox noclegPrzedCheckBox = new CheckBox("Nocleg Przed");
                    CheckBox noclegPoCheckBox = new CheckBox("Nocleg Po");
                    CheckBox hbCheckBox = new CheckBox("HB");

                    VBox servicesContent = new VBox(10);
                    servicesContent.getChildren().addAll(
                            ulgaCheckBox, rowerCheckBox, eBikeCheckBox,
                            noclegPrzedCheckBox, noclegPoCheckBox, hbCheckBox
                    );

                    servicesDialog.getDialogPane().setContent(servicesContent);
                    Platform.runLater(() -> ulgaCheckBox.requestFocus());

                    // Handle the additional services dialog result
                    servicesDialog.setResultConverter(dialogButtonServices -> {
                        if (dialogButtonServices == addServicesButtonType) {
                            boolean ulga = ulgaCheckBox.isSelected();
                            boolean rower = rowerCheckBox.isSelected();
                            boolean eBike = eBikeCheckBox.isSelected();
                            boolean noclegPrzed = noclegPrzedCheckBox.isSelected();
                            boolean noclegPo = noclegPoCheckBox.isSelected();
                            boolean hb = hbCheckBox.isSelected();

                            // Create a new Klienci (client) object and set its properties
                            Klienci klient = new Klienci();
                            klient.setImie(tempImie);
                            klient.setNazwisko(tempNazwisko);
                            klient.setWycieczka(tempWycieczka);
                            klient.setPokoj(selectedPokoj);
                            klient.setUlga(ulga);
                            klient.setRower(rower);
                            klient.setEBike(eBike);
                            klient.setNoclegPrzed(noclegPrzed);
                            klient.setNoclegPo(noclegPo);
                            klient.setHb(hb);
                            klient.setTypPokoju(selectedPokoj.getTypPokoju());
                            klient.setDoZaplaty(BigDecimal.valueOf(0));

                            // Calculate the payable amount based on room type and selected services
                            Ceny ceny = klient.getWycieczka().getTypWycieczki().getCeny();

                            switch (selectedPokoj.getIlMiejsc()) {
                                case 1:
                                    klient.setDoZaplaty(ceny.getPok_1().add(klient.getDoZaplaty()));
                                    break;
                                case 2:
                                    klient.setDoZaplaty(ceny.getPok_2().add(klient.getDoZaplaty()));
                                    break;
                                case 3:
                                    klient.setDoZaplaty(ceny.getPok_3().add(klient.getDoZaplaty()));
                                    break;
                                case 4:
                                    klient.setDoZaplaty(ceny.getPok_4().add(klient.getDoZaplaty()));
                                    break;
                            }

                            if (klient.getUlga()) {
                                Double val = klient.getDoZaplaty().intValue() * (ceny.getUlga_dziecko() - 100) / 100.0;
                                klient.setDoZaplaty(BigDecimal.valueOf(val));
                            }

                            if (klient.getRower()) {
                                klient.setDoZaplaty(klient.getDoZaplaty().add(ceny.getRower()));
                            }

                            if (klient.getEBike()) {
                                klient.setDoZaplaty(klient.getDoZaplaty().add(ceny.getE_bike()));
                            }

                            if (klient.getNoclegPo()) {
                                klient.setDoZaplaty(klient.getDoZaplaty().add(ceny.getDodatkowa_noc()));
                            }

                            if (klient.getNoclegPrzed()) {
                                klient.setDoZaplaty(klient.getDoZaplaty().add(ceny.getDodatkowa_noc()));
                            }

                            if (klient.getHb()) {
                                klient.setDoZaplaty(klient.getDoZaplaty().add(ceny.getHb()));
                            }

                            // Save the client to the database
                            try {
                                klienciService.add(klient);
                                leftButton.onClick();
                                showAlert(Alert.AlertType.INFORMATION, "Sukces", "Klient został dodany pomyślnie.");
                            } catch (WrongLetterException ex) {
                                showAlert(Alert.AlertType.ERROR, "Błąd", "Wystąpił problem: " + ex.getMessage());
                            } catch (Exception ex) {
                                showAlert(Alert.AlertType.ERROR, "Błąd", "Wystąpił nieoczekiwany problem: " + ex.getMessage());
                            }
                            return null;
                        }
                        return null;
                    });

                    // Show the third dialog (additional services)
                    servicesDialog.showAndWait();
                    return null;
                }
                return null;
            });

            // Show the second dialog (room selection)
            roomDialog.showAndWait();
        }
    }

    /**
     * Displays an alert dialog with the specified type, title, and message.
     *
     * <p>
     * This method ensures that the alert is displayed on the JavaFX Application Thread using {@link Platform#runLater(Runnable)}.
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
