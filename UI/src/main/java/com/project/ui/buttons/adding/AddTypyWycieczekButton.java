package com.project.ui.buttons.adding;

import com.project.springbootjavafx.exceptions.DuplicatedEntityExceptionn;
import com.project.springbootjavafx.models.Ceny;
import com.project.springbootjavafx.models.TypyWycieczek;
import com.project.springbootjavafx.models.MiastaWycieczek;
import com.project.springbootjavafx.models.Miasta;
import com.project.springbootjavafx.services.CenyService;
import com.project.springbootjavafx.services.MiastaService;
import com.project.springbootjavafx.services.TypyWycieczekService;
import com.project.springbootjavafx.services.MiastaWycieczekService;
import com.project.ui.SpringContextHolder;
import com.project.ui.buttons.CustomLeftButton;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code AddTypyWycieczekButton} class extends {@link Button} and provides the functionality
 * to add a new trip type (TypyWycieczek) into the system, along with its associated pricing information (Ceny)
 * and the assignment of cities for each night of the trip (MiastaWycieczek).
 *
 * <p>
 * When this button is clicked, a dialog is displayed to collect the new trip type's information:
 * the trip type (as a String) and the number of nights. If the user input is valid, a new {@code TypyWycieczek}
 * object is created and added via the {@link TypyWycieczekService}. After successfully adding the trip type,
 * two additional dialogs are automatically opened:
 * </p>
 * <ol>
 *   <li>
 *     The first additional dialog (opened via {@link #openAddCenyDialog(TypyWycieczek)}) collects pricing information
 *     for the new trip type. The user must provide room prices (for Pokój 1 through Pokój 4), a discount percentage
 *     for children, and prices for additional services such as bicycle, E-Bike, extra night, and half board.
 *   </li>
 *   <li>
 *     The second additional dialog (opened via {@link #openAddMiastaWycieczkiDialog(TypyWycieczek)}) allows the user to assign
 *     a city (Miasta) to each night of the trip. For each night (as determined by the number of nights in the trip type),
 *     a ComboBox is displayed for selecting a city. The selections are used to create {@code MiastaWycieczek} objects.
 *   </li>
 * </ol>
 *
 * <p>
 * The required services ({@link TypyWycieczekService}, {@link CenyService}, {@link MiastaService}, and
 * {@link MiastaWycieczekService}) are retrieved from the Spring context via {@link SpringContextHolder}.
 * The {@code leftButton} reference is used to refresh the view after changes are made.
 * </p>
 */
public class AddTypyWycieczekButton extends Button {

    private final CustomLeftButton<?, ?> leftButton;
    private final TypyWycieczekService typyService;
    private final CenyService cenyService;
    private final MiastaService miastaService;
    private final MiastaWycieczekService miastaWycieczekService;

    /**
     * Constructs a new {@code AddTypyWycieczekButton} with the specified button text and a reference to
     * a left sidebar button.
     *
     * @param name       the text to display on the button
     * @param leftButton the left sidebar button providing context and services for trip type operations
     */
    public AddTypyWycieczekButton(String name, CustomLeftButton<?, ?> leftButton) {
        super(name);
        this.leftButton = leftButton;
        this.typyService = SpringContextHolder.getContext().getBean(TypyWycieczekService.class);
        this.cenyService = SpringContextHolder.getContext().getBean(CenyService.class);
        this.miastaService = SpringContextHolder.getContext().getBean(MiastaService.class);
        this.miastaWycieczekService = SpringContextHolder.getContext().getBean(MiastaWycieczekService.class);
        this.setOnAction(e -> openAddTypyDialog());
    }

    /**
     * Opens a dialog to add a new trip type.
     *
     * <p>
     * This method displays a dialog that collects the trip type (as a string) and the number of nights.
     * If the input is valid, a new {@code TypyWycieczek} object is created and added via
     * {@link TypyWycieczekService}. Upon successful addition, it refreshes the left sidebar view by invoking
     * {@code onClick()} on the provided leftButton, and then opens two further dialogs:
     * </p>
     * <ol>
     *   <li>
     *     One to collect pricing information for the new trip type (via {@link #openAddCenyDialog(TypyWycieczek)}).
     *   </li>
     *   <li>
     *     Another to assign cities for each night of the trip (via {@link #openAddMiastaWycieczkiDialog(TypyWycieczek)}).
     *   </li>
     * </ol>
     */
    public void openAddTypyDialog() {
        Dialog<TypyWycieczek> dialog = new Dialog<>();
        dialog.setTitle("Dodaj Typ Wycieczki");
        dialog.setHeaderText("Wprowadź informacje o nowym typie wycieczki");

        // Set dialog buttons.
        ButtonType addButtonType = new ButtonType("Dodaj", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        // Create grid for input fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // Create text fields.
        TextField typField = new TextField();
        typField.setPromptText("Typ Wycieczki");

        TextField liczbaNocyField = new TextField();
        liczbaNocyField.setPromptText("Liczba Nocy");

        // Add labels and text fields to the grid.
        grid.add(new Label("Typ Wycieczki:"), 0, 0);
        grid.add(typField, 1, 0);
        grid.add(new Label("Liczba Nocy:"), 0, 1);
        grid.add(liczbaNocyField, 1, 1);

        Platform.runLater(typField::requestFocus);
        dialog.getDialogPane().setContent(grid);

        // Convert dialog result to a TypyWycieczek object.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                String typ = typField.getText().trim();
                String liczbaNocyStr = liczbaNocyField.getText().trim();
                if (typ.isEmpty() || liczbaNocyStr.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Błąd", "Wszystkie pola muszą być wypełnione.");
                    return null;
                }
                try {
                    Integer liczbaNocy = Integer.parseInt(liczbaNocyStr);
                    TypyWycieczek typWycieczki = new TypyWycieczek();
                    typWycieczki.setTyp(typ);
                    typWycieczki.setLiczba_nocy(liczbaNocy);
                    return typWycieczki;
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "Błąd", "Liczba nocy musi być liczbą.");
                    return null;
                }
            }
            return null;
        });

        // Process dialog result.
        dialog.showAndWait().ifPresent(typWycieczki -> {
            try {
                typyService.add(typWycieczki); // Save the new trip type.
                leftButton.onClick(); // Refresh the view.
                openAddCenyDialog(typWycieczki); // Open dialog to add pricing.
                openAddMiastaWycieczkiDialog(typWycieczki); // Open dialog to assign cities.
            } catch (DuplicatedEntityExceptionn e) {
                showAlert(Alert.AlertType.ERROR, "Błąd", e.getMessage());
            }
        });
    }

    /**
     * Opens a dialog to add pricing information for the given trip type.
     *
     * <p>
     * The dialog prompts the user to enter prices for room categories (Pokój 1 to Pokój 4), a discount percentage for children,
     * and prices for additional services such as bicycle, E-Bike, an extra night, and half board.
     * The provided values are used to create a {@link Ceny} object, which is then saved via {@link CenyService}.
     * </p>
     *
     * @param typWycieczki the trip type for which pricing is being set
     */
    private void openAddCenyDialog(TypyWycieczek typWycieczki) {
        Dialog<Ceny> dialog = new Dialog<>();
        dialog.setTitle("Dodaj Ceny dla Typu Wycieczki: " + typWycieczki.getTyp());
        dialog.setHeaderText("Wprowadź ceny dla nowego typu wycieczki");

        ButtonType addButtonType = new ButtonType("Dodaj Ceny", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // Create text fields for pricing input.
        TextField pok1Field = new TextField();
        pok1Field.setPromptText("Pokój 1");
        TextField pok2Field = new TextField();
        pok2Field.setPromptText("Pokój 2");
        TextField pok3Field = new TextField();
        pok3Field.setPromptText("Pokój 3");
        TextField pok4Field = new TextField();
        pok4Field.setPromptText("Pokój 4");
        TextField ulgaDzieckoField = new TextField();
        ulgaDzieckoField.setPromptText("Ulga Dziecko (%)");
        TextField rowerField = new TextField();
        rowerField.setPromptText("Rower");
        TextField eBikeField = new TextField();
        eBikeField.setPromptText("E-Bike");
        TextField dodatkowaNocField = new TextField();
        dodatkowaNocField.setPromptText("Dodatkowa Noc");
        TextField hbField = new TextField();
        hbField.setPromptText("HB");

        // Add labels and fields to the grid.
        grid.add(new Label("Pokój 1:"), 0, 0);
        grid.add(pok1Field, 1, 0);
        grid.add(new Label("Pokój 2:"), 0, 1);
        grid.add(pok2Field, 1, 1);
        grid.add(new Label("Pokój 3:"), 0, 2);
        grid.add(pok3Field, 1, 2);
        grid.add(new Label("Pokój 4:"), 0, 3);
        grid.add(pok4Field, 1, 3);
        grid.add(new Label("Ulga Dziecko (%):"), 0, 4);
        grid.add(ulgaDzieckoField, 1, 4);
        grid.add(new Label("Rower:"), 0, 5);
        grid.add(rowerField, 1, 5);
        grid.add(new Label("E-Bike:"), 0, 6);
        grid.add(eBikeField, 1, 6);
        grid.add(new Label("Dodatkowa Noc:"), 0, 7);
        grid.add(dodatkowaNocField, 1, 7);
        grid.add(new Label("HB:"), 0, 8);
        grid.add(hbField, 1, 8);

        dialog.getDialogPane().setContent(grid);
        Platform.runLater(pok1Field::requestFocus);

        // Convert the dialog result to a Ceny object.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                try {
                    BigDecimal pok1 = new BigDecimal(pok1Field.getText().trim());
                    BigDecimal pok2 = new BigDecimal(pok2Field.getText().trim());
                    BigDecimal pok3 = new BigDecimal(pok3Field.getText().trim());
                    BigDecimal pok4 = new BigDecimal(pok4Field.getText().trim());
                    Integer ulgaDziecko = Integer.parseInt(ulgaDzieckoField.getText().trim());
                    BigDecimal rower = new BigDecimal(rowerField.getText().trim());
                    BigDecimal eBike = new BigDecimal(eBikeField.getText().trim());
                    BigDecimal dodatkowaNoc = new BigDecimal(dodatkowaNocField.getText().trim());
                    BigDecimal hb = new BigDecimal(hbField.getText().trim());

                    Ceny ceny = new Ceny();
                    ceny.setTyp_wycieczki(typWycieczki);
                    ceny.setPok_1(pok1);
                    ceny.setPok_2(pok2);
                    ceny.setPok_3(pok3);
                    ceny.setPok_4(pok4);
                    ceny.setUlga_dziecko(ulgaDziecko);
                    ceny.setRower(rower);
                    ceny.setE_bike(eBike);
                    ceny.setDodatkowa_noc(dodatkowaNoc);
                    ceny.setHb(hb);

                    return ceny;
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "Błąd", "Wszystkie pola muszą zawierać prawidłowe wartości.");
                    return null;
                }
            }
            return null;
        });

        // Process the result.
        dialog.showAndWait().ifPresent(ceny -> {
            try {
                cenyService.add(ceny); // Save pricing information.
                showAlert(Alert.AlertType.INFORMATION, "Sukces", "Ceny zostały dodane.");
                leftButton.onClick(); // Refresh the view.
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Błąd", "Nie udało się dodać cen: " + e.getMessage());
            }
        });
    }

    /**
     * Opens a dialog to assign cities for each night of the trip for the given trip type.
     *
     * <p>
     * This method displays a dialog in which, for each night of the trip (determined by
     * {@link TypyWycieczek#getLiczba_nocy()}), a ComboBox is provided for the user to select a city
     * ({@link Miasta}). The selected cities are used to create a list of {@link MiastaWycieczek} objects,
     * each corresponding to a night of the trip.
     * </p>
     *
     * @param typWycieczki the trip type for which cities are being assigned
     */
    private void openAddMiastaWycieczkiDialog(TypyWycieczek typWycieczki) {
        Dialog<List<MiastaWycieczek>> dialog = new Dialog<>();
        dialog.setTitle("Dodaj Miasta do Wycieczki");
        dialog.setHeaderText("Przypisz miasta do poszczególnych nocy wycieczki.");

        ButtonType addButtonType = new ButtonType("Dodaj Miasta", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        // Retrieve available cities.
        List<Miasta> dostępneMiasta = miastaService.getAll();

        if (dostępneMiasta.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Błąd", "Brak dostępnych miast do przypisania.");
            return;
        }

        // Create a list of ComboBoxes for city selection.
        List<ComboBox<Miasta>> comboBoxList = new ArrayList<>();

        // Create a ComboBox for each night.
        for (int i = 1; i <= typWycieczki.getLiczba_nocy(); i++) {
            Label label = new Label("Miasto na noc " + i + ":");
            ComboBox<Miasta> comboBox = new ComboBox<>();
            comboBox.setItems(FXCollections.observableArrayList(dostępneMiasta));
            comboBox.setPromptText("Wybierz Miasto");
            grid.add(label, 0, i - 1);
            grid.add(comboBox, 1, i - 1);
            comboBoxList.add(comboBox);
        }

        dialog.getDialogPane().setContent(grid);

        // Set focus on the first ComboBox.
        Platform.runLater(() -> {
            if (!comboBoxList.isEmpty()) {
                comboBoxList.get(0).requestFocus();
            }
        });

        // Convert dialog result to a list of MiastaWycieczek objects.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                List<MiastaWycieczek> miastaWycieczekList = new ArrayList<>();
                for (int i = 0; i < comboBoxList.size(); i++) {
                    ComboBox<Miasta> comboBox = comboBoxList.get(i);
                    Miasta wybraneMiasto = comboBox.getValue();
                    if (wybraneMiasto == null) {
                        showAlert(Alert.AlertType.ERROR, "Błąd", "Wszystkie pola muszą być wypełnione.");
                        return null;
                    }
                    MiastaWycieczek miastaWycieczek = new MiastaWycieczek();
                    miastaWycieczek.setTypyWycieczek(typWycieczki);
                    miastaWycieczek.setMiasta(wybraneMiasto);
                    miastaWycieczek.setNumerNocy(i + 1);
                    miastaWycieczekList.add(miastaWycieczek);
                }
                return miastaWycieczekList;
            }
            return null;
        });

        // Process dialog result.
        dialog.showAndWait().ifPresent(miastaWycieczekList -> {
            for (MiastaWycieczek miastaWycieczek : miastaWycieczekList) {
                miastaWycieczekService.add(miastaWycieczek);
            }
            showAlert(Alert.AlertType.INFORMATION, "Sukces", "Miasta zostały przypisane pomyślnie.");
        });
    }

    /**
     * Displays an alert dialog with the specified alert type, title, and message.
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
