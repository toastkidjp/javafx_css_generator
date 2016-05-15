package jp.toastkid.gui.jfx.cssgen;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.collections.impl.factory.Maps;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import jp.toastkid.gui.jfx.cssgen.model.Person;

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

    /** input color. */
    @FXML
    private ColorPicker inputColor;

    /** sample view. */
    @FXML
    private TreeView<String> tree;

    @FXML
    private TableView<Person> table;

    @FXML
    private TableColumn<Person, String> personId;
    @FXML
    private TableColumn<Person, String> personName;
    @FXML
    private TableColumn<Person, String> isActive;

    @FXML
    private Slider opacity;
    @FXML
    private Label  opacityValue;

    @FXML
    private TextField fileName;

    /** for use apply stylesheet. */
    private Stage stage;

    @FXML
    private void saveAs() {
        final String text = fileName.getText();
        if (StringUtils.isBlank(text)) {
            return;
        }
        try {
            final Path source = Paths.get(TEMP_FILE_PATH);
            if (!Files.exists(source)) {
                change();
            }
            final Path path = Paths.get(text.endsWith(".css") ? text : text + ".css");
            Files.copy(source, path, StandardCopyOption.REPLACE_EXISTING);
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
        final Color main  = mainColor.getValue();
        final Color sub   = subColor.getValue();
        final Color input = inputColor.getValue();

        final Map<String, String> params = Maps.mutable.empty();
        params.put("main_rgb",     makeRgbStr(main));
        params.put("sub_rgb",      makeRgbStr(sub));
        params.put("main",         toRgbCode(main));
        params.put("sub",          toRgbCode(sub));
        params.put("main_dark",    toRgbCode(main.darker()));
        params.put("text",         toRgbCode(Color.BLACK));
        params.put("text_focused", toRgbCode(Color.WHITE));
        params.put("input",        toRgbCode(input));
        params.put("opacity",      Double.toString(opacity.getValue()));

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
     * back to default mode.
     */
    @FXML
    private void backToDefault() {
        final ObservableList<String> stylesheets = stage.getScene().getStylesheets();
        if (stylesheets != null) {
            stylesheets.clear();
        }
        Application.setUserAgentStylesheet("MODENA");
        mainColor.setValue(Color.WHITE);
        subColor.setValue(Color.WHITE);
        inputColor.setValue(Color.WHITE);
    }

    /**
     * pass Stage object.
     * @param stage
     */
    protected void setStage(final Stage stage) {
        this.stage = stage;
    }

    /**
     * close this app.
     */
    @FXML
    private void close() {
        this.stage.close();
        System.exit(0);
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

        personId.setCellValueFactory(new PropertyValueFactory<Person, String>("id"));
        personName.setCellValueFactory(new PropertyValueFactory<Person, String>("name"));
        isActive.setCellValueFactory(new PropertyValueFactory<Person, String>("active"));

        table.getItems().addAll(
                new Person.Builder().setId(1L).setName("Alice").setActive(true).build(),
                new Person.Builder().setId(1L).setName("Bob").setActive(false).build(),
                new Person.Builder().setId(1L).setName("Charlie").setActive(true).build()
                );

        opacity.setValue(1.0d);
        opacityValue.textProperty().bind(opacity.valueProperty().asString());
    }

}
