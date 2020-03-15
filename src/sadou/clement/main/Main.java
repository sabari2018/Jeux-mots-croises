package sadou.clement.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) {

        try
        {
            stage.setTitle("TP6 SADOU-CLEMENT");
            FXMLLoader loader = new FXMLLoader() ;
            loader.setLocation(getClass().getResource("../vue/VuePrincipal.fxml"));
            Parent root = (Parent) loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);

            stage.setResizable(false);
            stage.sizeToScene();


            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
