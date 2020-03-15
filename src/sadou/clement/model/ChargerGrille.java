package sadou.clement.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe qui permet d'extraire une grille de la base de données
 */
public class ChargerGrille {

    private Connection connection;
    private Map<Integer, String> grilleDispo;

    public ChargerGrille(){
        try{
            connection = connecterBD();
            this.grilleDispo = this.grillesDisponibles();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    /**
     * Connexion à la base de données
     * @return Connexion
     * @throws SQLException
     */
    public static Connection connecterBD() throws SQLException {

            Connection connection;
            //connection = DriverManager.getConnection("jdbc:mysql://mysql.istic.univ-rennes1.fr:3306/base_bousse","user_cdepond","barrydepond");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bd_motcroises","root","S@bari14");
            return connection;

    }

    /**
     * @return Une table qui contient les Grilles disponibles dans la base de données
     */
    public Map<Integer, String> grillesDisponibles(){
        Map<Integer, String> disponible = new HashMap<>();

        String sql = "select * from TP5_GRILLE";
        try {
            Statement req = getConnection().createStatement();
            ResultSet resultSet = req.executeQuery(sql);
            while (resultSet.next())
            {
                disponible.put(
                           resultSet.getInt("num_grille"),
                           resultSet.getString("nom_grille") +","+
                           resultSet.getString("largeur") +","+
                           resultSet.getString("hauteur")  +","+
                           resultSet.getString("controle"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return disponible;
    }

    /**
     * @param numGrille numero de la grille
     * @return Retourne la grille dont le numero est passé en parametre.
     */
    public MotsCroises extraireGrille(int numGrille){


        String mots = this.grilleDispo.get(numGrille);

        String[]  motsCroise = mots.split(",");
        int largeur =  Integer.parseInt(motsCroise[1]);
        int hauteur =  Integer.parseInt(motsCroise[2]);

        MotsCroises motsCroises = new MotsCroises(hauteur,largeur);

        for (int i=1; i<= motsCroises.getHauteur(); i++){
            for(int j=1; j<= motsCroises.getLargeur(); j++){
                motsCroises.setCaseNoire(i,j,true);
            }
        }


        //Acces a la base de donnees
        String sql = "SELECT * FROM TP5_MOT WHERE num_grille = ?";
        try {
            PreparedStatement preSt = getConnection().prepareStatement(sql);
            preSt.setInt(1, numGrille);
            ResultSet resultSet = preSt.executeQuery();

            int ligne, colonne, horizotal;
            String definition;


            while (resultSet.next()){

                ligne = resultSet.getInt("ligne");
                colonne = resultSet.getInt("colonne");
                horizotal = resultSet.getInt("horizontal");
                definition = resultSet.getString("definition");

                motsCroises.setCaseNoire(ligne,colonne, false);

                motsCroises.setDefinition(ligne, colonne,horizotal == 1,definition);

                String solution = resultSet.getString("solution");

                int inx=0;
               if (horizotal == 1){
                   int taille = colonne + solution.length(); // changer taille en position
                   for (int j = colonne; j < taille; j++){

                       motsCroises.setCaseNoire(ligne,j, false);
                       motsCroises.setSolution(ligne, j, solution.charAt(inx));
                       inx +=1;
                   }
               }else {
                   int taille = ligne + solution.length();
                   for (int i= ligne; i< taille; i++){

                       motsCroises.setCaseNoire(i,colonne, false);
                       motsCroises.setSolution(i,colonne, solution.charAt(inx));
                       inx += 1;
                   }
               }
            }

        }catch (SQLException e){
            e.printStackTrace();
        }

        return motsCroises;
    }

    public static List<Integer> getGrilleNumbers(){

        List<Integer> liste = new ArrayList<>();

        String sql = "SELECT num_grille FROM TP5_GRILLE";
        try {
            Statement req = connecterBD().createStatement();
            ResultSet result = req.executeQuery(sql);
            while (result.next()){
                liste.add(result.getInt("num_grille"));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        return liste;
    }
}
