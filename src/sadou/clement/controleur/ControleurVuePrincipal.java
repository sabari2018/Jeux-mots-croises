package sadou.clement.controleur;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ControleurVuePrincipal {

    @FXML
    private Button btnQuitter;
    @FXML
    private Button btnQ;

    @FXML
    private MenuItem menuChoixAleatoire;

    @FXML
    private MenuItem menuChoixManuelle;

    @FXML
    private MenuItem menuQuitter;

    @FXML
    private VBox vbox;

    @FXML
    private void initialize(){

        menuChoixAleatoire.setOnAction((e) -> {
            clickMenuAlea(e);
        });
        menuChoixManuelle.setOnAction((e) -> {
            clickMenuChoix(e);
        });
        menuQuitter.setOnAction((e) -> {
            clickMenuQuitter(e);
        });

        btnQ.setOnAction((e)->{
            clickMenuQuitter(e);
        });

    }

    @FXML
    private void clickMenuAlea(ActionEvent e){
        int choix = (int) (1 + Math.random()*10);
        lancerGrille(choix);
    }

    @FXML
    private void clickMenuChoix(ActionEvent e){
        int choix = ouvirDialogue();
        lancerGrille(choix);
    }

    @FXML
    private void clickMenuQuitter(ActionEvent e){
        Stage stage = (Stage) btnQuitter.getScene().getWindow();
        stage.close();
    }

    /**
     * Lance une boite de dialogue qui demande à l'utilisateur de selectionner le numero de la grille
     * @return le numero choisi
     */
    private int ouvirDialogue(){

        List<Integer> numbers = new ArrayList<>();
        for (int i=1; i<12; i++){
            numbers.add(i);
        }
        ChoiceDialog<Integer> dialog = new ChoiceDialog<>(1, numbers);

        dialog.setTitle("Choix de la grille");
        dialog.setHeaderText("");
        dialog.setContentText("Entrer le numero de la grille");

        Optional<Integer> choix = dialog.showAndWait();
        return choix.orElse(1);
    }


    /**
     * Lance la vue qui corespond au numero de la grille
     * @param numGrille Numero de la grille
     */
    private void lancerGrille(int numGrille){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../vue/VueGrille.fxml"));
        ControleurVueGrille controler = new ControleurVueGrille(numGrille);
        loader.setController(controler);
        try {
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Mots croises - SADOU-CLEMENT");
            stage.setScene(scene);

            stage.setResizable(false);
            stage.sizeToScene();

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
