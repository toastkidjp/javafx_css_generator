package jp.toastkid.gui.jfx.cssgen;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Noodle timer.
 * @author Toast kid
 *
 */
public class Main extends Application {

    /** Application title. */
    private static final String TITLE     = "JavaFX CSS Generator";

    /** fxml. */
    private static final String FXML_PATH = "main.fxml";

    /** Stage. */
    private Stage stage;

    @Override
    public void start(final Stage arg0) throws Exception {
        stage = new Stage(StageStyle.DECORATED);

        try {
            final FXMLLoader loader
                = new FXMLLoader(getClass().getClassLoader().getResource(FXML_PATH));
            final Parent load = loader.load();
            final Scene scene = new Scene(load);
            stage.setScene(scene);
            final Controller controller = (Controller) loader.getController();
            controller.setStage(stage);
        } catch (final IOException e) {
            e.printStackTrace();
        }

        stage.setResizable(true);
        stage.setTitle(TITLE);
        stage.show();
    }

    /**
     *
     * @param args
     */
    public static void main(final String[] args) {
        Application.launch(Main.class);
    }
}
