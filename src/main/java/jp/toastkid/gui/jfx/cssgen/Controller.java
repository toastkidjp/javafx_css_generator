package jp.toastkid.gui.jfx.cssgen;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.collections.impl.factory.Maps;

import com.jfoenix.controls.JFXTextField;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Controller.
 * @author Toast kid
 *
 */
public class Controller implements Initializable {

    /** temp file path. */
    private static final String TEMP_FILE_PATH = "generated.css";

    /** firstColor. */
    @FXML
    private ColorPicker mainColor;

    /** secondColor. */
    @FXML
    private ColorPicker subColor;

    /** sample view. */
    @FXML
    private TreeView<String> tree;

    /** for use apply stylesheet. */
    private Stage stage;

    @FXML
    private void saveAs() {
        final Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        final JFXTextField input = new JFXTextField();
        a.getDialogPane().getChildren().add(input);
        a.showAndWait();
        final String text = input.getText();
        if (StringUtils.isBlank(text)) {
            return;
        }
        try {
            final Path source = Paths.get(TEMP_FILE_PATH);
            if (!Files.exists(source)) {
                change();
            }
            final Path path = Paths.get(text);
            if (!Files.exists(path)) {
                Files.createFile(path);
            }
            Files.copy(source, path);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * store messages to text file.
     * .
     * @param content
     */
    @FXML
    private void save(final String content) {
        try {
            Files.write(Paths.get(TEMP_FILE_PATH), content.getBytes());
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * color change.
     */
    @FXML
    private void change() {
        final Color main = mainColor.getValue();
        final Color sub  = subColor.getValue();

        final Map<String, String> params = Maps.mutable.empty();
        params.put("main_rgb",     makeRgbStr(main));
        params.put("sub_rgb",      makeRgbStr(sub));
        params.put("main",         toRgbCode(main));
        params.put("sub",          toRgbCode(sub));
        params.put("text",         toRgbCode(Color.BLACK));
        params.put("text_focused", toRgbCode(Color.WHITE));

        try {
            save(Presenter.bindArgs("base.css", params));
        } catch (final IOException e) {
            e.printStackTrace();
        }
        setStyle("generated.css");
    }

    /**
     * make rgb string.
     * @param color Color object
     * @return ex) 255.0, 255.0, 255.0
     */
    private String makeRgbStr(final Color main) {
        return String.format("%f, %f, %f",
                main.getRed() * 255.0, main.getGreen() * 255.0, main.getBlue() * 255.0);
    }

    /**
     * color to #XXXXXX code.
     * @param color
     * @return RGB code
     * @see <a href="http://stackoverflow.com/questions/17925318/
     *how-to-get-hex-web-string-from-javafx-colorpicker-color">
     * How to get hex web String from JavaFX ColorPicker color?</a>
     */
    private static String toRgbCode(final Color color) {
        return String.format( "#%02X%02X%02X",
            (int)( color.getRed()   * 255 ),
            (int)( color.getGreen() * 255 ),
            (int)( color.getBlue()  * 255 ) );
    }

    /**
     * set stylesheet.
     * @param styleName
     */
    private void setStyle(final String styleName) {
        final Path path = Paths.get(styleName);
        if (!Files.exists(path)) {
            return;
        }
        final ObservableList<String> stylesheets = stage.getScene().getStylesheets();
        if (stylesheets != null) {
            stylesheets.clear();
        }
        Application.setUserAgentStylesheet("MODENA");
        stylesheets.add(path.toUri().toString());
    }

    /**
     * pass Stage object.
     * @param stage
     */
    protected void setStage(final Stage stage) {
        this.stage = stage;
    }

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        final TreeItem<String> value = new TreeItem<String>("Root");
        value.setExpanded(true);
        value.getChildren().addAll(
                new TreeItem<String>("Item 1"),
                new TreeItem<String>("Item 2"),
                new TreeItem<String>("Item 3")
            );
        tree.setRoot(value);
    }

}
