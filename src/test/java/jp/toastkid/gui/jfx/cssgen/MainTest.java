package jp.toastkid.gui.jfx.cssgen;

import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import javafx.application.Platform;
import javafx.stage.Stage;

/**
 * {@link Main}'s test case.
 *
 * @author Toast kid
 *
 */
public class MainTest extends ApplicationTest {

    /** Stage. */
    private Stage stage;

    /** Main. */
    private Main main;

    /**
     * Test of {@link Main#start(Stage)}.
     */
    @Test
    public void test() {
        Platform.runLater(() -> {
             try {
                main.start(stage);
                main.stop();
            } catch (final Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void start(final Stage stage) throws Exception {
        this.stage = stage;
        main = new Main();
    }

}
