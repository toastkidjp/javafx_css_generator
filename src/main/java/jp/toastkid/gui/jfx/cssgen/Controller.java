package jp.toastkid.gui.jfx.cssgen;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;

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

    /** sample view. */
    @FXML
    private TableView<Person> table;

    /** sample view. */
    @FXML
    private TableColumn<Person, String> personId;

    /** sample view. */
    @FXML
    private TableColumn<Person, String> personName;

    /** sample view. */
    @FXML
    private TableColumn<Person, String> isActive;

    /** Opacity value slider. */
    @FXML
    private Slider opacity;

    /** Opacity value indicator. */
    @FXML
    private Label  opacityValue;

    /** CSS file name. */
    @FXML
    private TextField fileName;

    /** for use apply stylesheet. */
    private Stage stage;

    /**
     * save current state css as another file name.
     */
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
     *
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

        final Map<String, String> params = new HashMap<>();
        params.put("main_rgb",     Utilities.makeRgbStr(main));
        params.put("sub_rgb",      Utilities.makeRgbStr(sub));
        params.put("main",         Utilities.toRgbCode(main));
        params.put("sub",          Utilities.toRgbCode(sub));
        params.put("main_dark",    Utilities.toRgbCode(main.darker()));
        params.put("text",         Utilities.toRgbCode(Color.BLACK));
        params.put("text_focused", Utilities.toRgbCode(Color.WHITE));
        params.put("input",        Utilities.toRgbCode(input));
        params.put("opacity",      Double.toString(opacity.getValue()));

        try {
            save(Utilities.bindArgs("base.css", params));
        } catch (final IOException e) {
            e.printStackTrace();
        }
        setStyle("generated.css");
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
        //System.exit(0);
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

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            final Path path = Paths.get(TEMP_FILE_PATH);
            if (!Files.exists(path)) {
                return;
            }
            try {
                Files.delete(path);
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }));
    }

}
