package com.project.ui.buttons;

import com.project.springbootjavafx.models.*;
import com.project.springbootjavafx.services.KlienciService;
import com.project.springbootjavafx.services.PokojeService;
import com.project.ui.MainContent;
import com.project.ui.SpringContextHolder;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.*;

public class ListaHoteliShowButton extends Button{

    private final PokojeService pokojeService;
    private final KlienciService klienciService;
    private final MainContent mainContent;
    
    
    public ListaHoteliShowButton(String name){
        super(name);

        this.pokojeService = SpringContextHolder.getContext().getBean(PokojeService.class);
        this.klienciService = SpringContextHolder.getContext().getBean(KlienciService.class);
        this.mainContent = SpringContextHolder.getContext().getBean(MainContent.class);
        
        this.setOnAction(e -> onClick());
    }

    private void onClick(){

        Pokoje pokoj = wybierzPokoj();

        if(pokoj == null) return;

        BorderPane borderPane = new BorderPane();
        borderPane.setStyle("-fx-background-color: #fff");

        // Ustawienie nazwy wycieczki

        VBox clientsVBox = new VBox(10);
        Label labelWycieczka = new Label(pokoj.getWycieczka().toString());
        labelWycieczka.setStyle("-fx-font-size: 25px; -fx-font-weight: bold;");

        Label infoLabel = new Label("(Dodatkowe noce są w tym samym hotelu co pierwsza/ostatni noc wycieczki)");
        infoLabel.setStyle("-fx-font-size: 10px;");

        clientsVBox.getChildren().addAll(labelWycieczka, infoLabel);

        // Ustwianie klientow
        List<Klienci> klienci = klienciService.getKlienciPokoju(pokoj);


        for (Klienci klient : klienci) {

            String dopiska = "";
            if(klient.getNoclegPrzed()) dopiska = "(Nocleg przed) ";

            if(klient.getNoclegPo()) dopiska += "(Nocleg po)";


            Label labelKlient = new Label("Klient: " + klient.getImie() + " " + klient.getNazwisko() + " " + dopiska);
            labelKlient.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");
            clientsVBox.getChildren().add(labelKlient);
        }

        clientsVBox.setAlignment(Pos.TOP_LEFT);
        BorderPane.setAlignment(clientsVBox, Pos.TOP_LEFT);
        borderPane.setTop(clientsVBox);


        // Ustawianie listy hotel
        List<ListyHoteli> listaHoteli = pokojeService.getListyHoteli(pokoj);

        LocalDate dataNocy = pokoj.getWycieczka().getPoczatek();


        VBox hoteleVBox = new VBox(10);
        for(int i = 0; i < listaHoteli.size(); i++){
            MiastaWycieczek miastoWycieczki = listaHoteli.get(i).getMiastoWycieczki();

            Label listaLabel = new Label(Integer.toString(i + 1) + ".   " + dataNocy.plusDays(i).toString()
                    + "   " + miastoWycieczki.getMiasta() + "   " + listaHoteli.get(i).getHotel().getNazwa());

            listaLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: blue;");
            hoteleVBox.getChildren().add(listaLabel);
        }

        hoteleVBox.setAlignment(Pos.CENTER_LEFT);
        BorderPane.setAlignment(hoteleVBox, Pos.CENTER_LEFT);
        borderPane.setCenter(hoteleVBox);


        mainContent.updateForPane(borderPane);
    }

    private Pokoje wybierzPokoj(){

        Dialog<Pokoje> dialog = new Dialog<>();

        dialog.setTitle("Wybierz pokój");
        dialog.setHeaderText("Podaj Id pokoju którego listę hoteli chcesz wyświetlić");

        ButtonType addButtonType = new ButtonType("Dalej", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));


        TextField pokojID = new TextField();
        pokojID.setPromptText("ID pokoju");


        grid.add(new Label("ID pokoju:"), 0, 1);
        grid.add(pokojID, 1, 1);


        dialog.getDialogPane().setContent(grid);

        Platform.runLater(pokojID::requestFocus);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                String ID = pokojID.getText().trim();

                try {
                    return pokojeService.getById(Integer.valueOf(ID));
                } catch (NoSuchElementException e) {
                    showAlert(Alert.AlertType.ERROR, "Brak obiektu", "Nie ma pokoju o takim ID");
                }
            }
            return null;
        });

        Optional<Pokoje> result = dialog.showAndWait();
        if(result.isPresent()) {

            if (!result.get().getListaHoteli()) {
                showAlert(Alert.AlertType.ERROR, "Brak listy hoteli", "Najpierw wybierz hotele dla pokoju");
                return null;
            }

            if(result.get().getIlKlientow() < result.get().getIlMiejsc()){
                showAlert(Alert.AlertType.INFORMATION, "Uwaga", "Na ten moment ten pokój nie jest zapełniony");
            }
        }



        return result.orElse(null);
    }

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
