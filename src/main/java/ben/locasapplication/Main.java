package ben.locasapplication;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/interfaces/SignIn.fxml")));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            Image icon = new Image(getClass().getResourceAsStream("/icons/iconLocas.png"));
            primaryStage.getIcons().add(icon);
            primaryStage.setTitle("Connexion");
            primaryStage.show();
        }catch (Exception e){
            e.printStackTrace();

        }

    }

    public static void main(String[] args) {
        launch();
    }
}