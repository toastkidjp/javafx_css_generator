package jp.toastkid.gui.jfx.cssgen;

import java.io.IOException;

import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * {@link Controller}'s test cases.
 *
 * @author Toast kid
 *
 */
public class ControllerTest extends ApplicationTest {

    /** Test object. */
    private Controller controller;

    /**
     * Test of {@link Controller#initialize(java.net.URL, java.util.ResourceBundle)}.
     */
    @Test
    public void test() {
        // NOP
    }

    @Override
    public void start(Stage stage) throws Exception {
        try {
            final FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("main.fxml"));
            final Parent load = loader.load();
            final Scene scene = new Scene(load);
            stage.setScene(scene);
            controller = (Controller) loader.getController();
            controller.setStage(stage);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

}
