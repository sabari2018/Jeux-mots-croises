package sadou.clement.controleur;

import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import sadou.clement.model.ChargerGrille;
import sadou.clement.model.MotsCroises;

import java.text.DecimalFormat;

public class ControleurVueGrille {

    /**
     * Numero de la Grille
     */
    private int numGrille;

    /**
     * Tableau 2d qui permet de memoriser les cases et permet le deplacement.
     */
    private TextField [][] casesDeLaGrille;

    private float caseDispo;
    private float caseSaisie;

    /**
     * Case courante
     */
    private TextField caseCourante;

    /**
     * Pourcentage de remplicage de la grille
     */
    @FXML
    private Label tauxRemplicage;

    @FXML
    private GridPane monGridPane;

    private MotsCroises motsCroisesTP6;

    /**
     * Direction : 1 -> Horizontal, 0 -> Verticale
     */
    private int direction;

    public ControleurVueGrille(int numGrille){
        this.numGrille = numGrille;
        this.direction = 1;
        this.caseDispo = 0;
        this.caseSaisie = 0;
    }


    @FXML
    private void initialize(){

       charger(this.numGrille);


       for (Node n : monGridPane.getChildren()){
           if (n instanceof TextField) {
                TextField tf = (TextField) n;
                int lig = ((int) n.getProperties().get("gridpane-row")) + 1;
                int col = ((int) n.getProperties().get("gridpane-column")) + 1;

                motsCroisesTP6.propositionProperty(lig, col).bindBidirectional(tf.textProperty());

                casesDeLaGrille[lig -1][col -1] = tf;
                tf.addEventHandler(KeyEvent.KEY_TYPED, maxCaractereCase(1));

                String texte = recupererInfoBulles(lig, col);
                if (texte != null)
                    tf.setTooltip(new Tooltip(texte));

                tf.setOnMouseClicked(this::clicCase);
                tf.setOnKeyPressed(this::saisieCase);
            }
        }



        caseCourante.setOnKeyTyped(event ->{
            int maxCharacters = 1;
            if(caseCourante.getText().length() > maxCharacters) event.consume();
        });
    }

    /**
     * Fonction qui permet d'empecher la Saisie de plusieurs lettres dans une case
     * @param num
     */
    private EventHandler<KeyEvent> maxCaractereCase(final Integer num){
        return new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {

                TextField tf = (TextField) keyEvent.getSource();
                if (tf.getText().length() >= num)
                    keyEvent.consume();
            }
        };
    }




    /**
     * Charger une grille lue depuis la base de donnees
     * @param numGrille numero de la grille
     */
    private void charger(int numGrille){

        ChargerGrille chargerGrille = new ChargerGrille();
        motsCroisesTP6 = chargerGrille.extraireGrille(numGrille);

        int ligne = motsCroisesTP6.getHauteur();
        int col = motsCroisesTP6.getLargeur();
        TextField modele = (TextField) monGridPane.getChildren().get(0);

        casesDeLaGrille = new TextField[ligne][col];

        caseCourante = modele;
        
        monGridPane.getChildren().clear();

        for (int i=1; i<= ligne; i++){
            for (int j=1; j<= col; j++) {
                if (!motsCroisesTP6.estCaseNoire(i, j)) {
                    TextField nouveau = new TextField();
                    nouveau.setPrefWidth(modele.getPrefWidth());
                    nouveau.setPrefHeight(modele.getPrefHeight());

                    caseDispo +=1;

                    for (Object cle : modele.getProperties().keySet()) {
                        nouveau.getProperties().get(cle);
                    }
                    monGridPane.add(nouveau, j - 1, i - 1);
                }
            }
        }

    }

    /**
     * Mises à jours le label (Pourcentage) sur la vue
     */
    private void miseAjourPourcentageLabel(){

        float taux = (caseSaisie/caseDispo)*100;
        this.tauxRemplicage.setText("Taux de remplicage : " + new DecimalFormat("0.00").format(taux) + " %");
    }

    private String recupererInfoBulles(int lig, int col){

        String info = null, horz, vert;
        horz = motsCroisesTP6.getDefinition(lig,col,true);
        vert = motsCroisesTP6.getDefinition(lig,col,false);

        if (horz != null && vert != null)
            info = horz +"/" + vert;
        else if (horz != null && vert == null)
            info = horz + "/null";
        else if (vert != null && horz == null)
            info = "null/" + vert;
        else
            info = "null/null";
        return info;
    }

    @FXML
    public void clicCase(MouseEvent e) {
        if (e.getButton() == MouseButton.PRIMARY) {
            TextField case1 = (TextField) e.getSource();

            int lig = ((int) case1.getProperties().get("gridpane-row")) + 1;
            int col = ((int) case1.getProperties().get("gridpane-column")) + 1;

            if (e.getClickCount() == 1){
                caseCourante = case1;
                caseCourante.getStyleClass().add("case-courante");
            }
            else if (e.getClickCount() == 2) {
                motsCroisesTP6.reveler(lig, col);
                case1.getStyleClass().add("case-solution-ok");
                deplacerCurseur(lig-1,col-1);
                caseCourante.requestFocus();
                this.caseSaisie +=1;
                this.miseAjourPourcentageLabel();
            }

        }
    }

    /**
     * Fonction qui renvoie VRAI si y'a le numero de colonne en parametre ne sort pas du tableau
     * @param col Colonne
     * @return VRAI si la condition est respecter ou FAUX si non
     */
    private boolean testDirectionHorizontalValide(int col){
        return col >= 0 && col <= this.casesDeLaGrille[0].length -1;
    }

    /**
     * Fonction qui renvoie VRAI si y'a le numero de de ligne en parametre ne sort pas du tableau
     * @param lig ligne
     * @return VRAI si la condition est respecter ou FAUX si non
     */
    private boolean testDirectionVerticalValide(int lig){

        return lig >= 0 &&lig <= this.casesDeLaGrille.length -1;
    }

    @FXML
    public void saisieCase(KeyEvent e){

        TextField case1 = (TextField) e.getSource();
        int lig = ((int) case1.getProperties().get("gridpane-row"));
        int col = ((int) case1.getProperties().get("gridpane-column"));


        if (e.getCode() == KeyCode.ENTER){

            if (!case1.getText().equals("")) {
                char solution = motsCroisesTP6.getSolution(lig + 1, col + 1);
                char proposition = case1.getText().charAt(0);


                int compare = Character.compare(solution, proposition);
                if (compare == 0)
                    case1.getStyleClass().add("case-solution-ok");
                else
                    case1.getStyleClass().remove("case-solution-ok");

                if (this.direction == 2)
                    this.direction = 1;
                else if (this.direction == 3)
                    this.direction = 0;

                if (this.deplacerCurseur(lig, col)) {
                    this.caseSaisie += 1;
                    this.miseAjourPourcentageLabel();
                }

                caseCourante.requestFocus();
            }

        }
        else if (e.getCode() == KeyCode.DOWN){
            this.direction = 0;
            this.deplacerCurseur(lig,col);
            caseCourante.requestFocus();
        }
        else if (e.getCode() == KeyCode.RIGHT){
            this.direction = 1;
            this.deplacerCurseur(lig,col);
            caseCourante.requestFocus();
        }
        else if (e.getCode() == KeyCode.BACK_SPACE){
            case1.setText("");
            this.caseSaisie -= 1;
            if(this.caseSaisie < 0)
                this.caseSaisie = 0;
            this.miseAjourPourcentageLabel();

            caseCourante.getStyleClass().remove("case-solution-ok");
            if (col -1 >=0 && this.casesDeLaGrille[lig][col-1] != null) {
                caseCourante = this.casesDeLaGrille[lig][col - 1];
                caseCourante.requestFocus();
                caseCourante.getStyleClass().add("case-courante");
            }
        }
        else if (e.getCode() == KeyCode.LEFT){
            this.direction = 2;
            this.deplacerCurseur(lig,col);
            caseCourante.requestFocus();
        }
        else if (e.getCode() == KeyCode.UP){
            this.direction = 3;
            this.deplacerCurseur(lig,col);
            caseCourante.requestFocus();
        }

    }



    /**
     * Fonction qui permet de deplacer le curseur sur la case suivante en fonction de la direction memorisee
     * 0 -> Verticale (droite)
     * 1 -> Horizontale (en bas)
     * 2 -> A gauche
     * 3 -> En Haut
     * @param lig ligne
     * @param col colonne
     */
    private boolean deplacerCurseur(int lig, int col){
        if (this.direction == 1 && testDirectionHorizontalValide(col + 1) && this.casesDeLaGrille[lig][col+1] != null) {
            caseCourante = this.casesDeLaGrille[lig][col + 1];
            caseCourante.getStyleClass().add("case-courante");
            return true;
        }
        else if (this.direction == 0 && testDirectionVerticalValide(lig+1) && this.casesDeLaGrille[lig+1][col] != null) {
            caseCourante = this.casesDeLaGrille[lig + 1][col];
            caseCourante.getStyleClass().add("case-courante");
            return true;
        }

        else if (this.direction == 2 && testDirectionHorizontalValide(col -1) && this.casesDeLaGrille[lig][col-1] != null) {
            caseCourante = this.casesDeLaGrille[lig][col - 1];
            caseCourante.getStyleClass().add("case-courante");
            return true;
        }

        else if (this.direction == 3 && testDirectionVerticalValide(lig-1) && this.casesDeLaGrille[lig-1][col] != null) {
            caseCourante = this.casesDeLaGrille[lig - 1][col];
            caseCourante.getStyleClass().add("case-courante");
            return true;
        }

        return false;
    }
}
