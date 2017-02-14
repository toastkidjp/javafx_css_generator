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
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;

import javafx.animation.Transition;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import jp.toastkid.gui.jfx.cssgen.model.Person;

/**
 * Controller.
 * @author Toast kid
 *
 */
public class Controller implements Initializable {

    /** temp file path. */
    private static final String TEMP_FILE_PATH = "generated.css";

    /** Save shortcut. */
    private static final KeyCombination APPLY_CONTENT
        = new KeyCodeCombination(KeyCode.ENTER, KeyCombination.CONTROL_DOWN);

    /** Save shortcut. */
    private static final KeyCombination SAVE_AS
        = new KeyCodeCombination(KeyCode.F12);

    /** Edit shortcut. */
    private static final KeyCombination SWITCH_EDITOR
        = new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN);

    /** Root pane. */
    @FXML
    private Pane root;

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

    /** CSS text area. */
    @FXML
    private CodeArea cssArea;

    /** for use apply stylesheet. */
    private Stage stage;

    /** Opening animation. */
    private Transition open;

    private Transition close;

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
        cssArea.replaceText(content);
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
        params.put("main_rgb",     ColorUtils.makeRgbStr(main));
        params.put("sub_rgb",      ColorUtils.makeRgbStr(sub));
        params.put("main",         ColorUtils.toRgbCode(main));
        params.put("sub",          ColorUtils.toRgbCode(sub));
        params.put("main_dark",    ColorUtils.toRgbCode(main.darker()));
        params.put("text",         ColorUtils.toRgbCode(Color.BLACK));
        params.put("text_focused", ColorUtils.toRgbCode(Color.WHITE));
        params.put("input",        ColorUtils.toRgbCode(input));
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
     * TODO
     * Pass Stage object.
     * @param stage
     */
    protected void setStage(final Stage stage) {
        this.stage = stage;
        initCodeArea();
        final ObservableMap<KeyCombination,Runnable> accelerators = stage.getScene().getAccelerators();
        accelerators.put(SAVE_AS,       this::saveAs);
        accelerators.put(SWITCH_EDITOR, this::switchEditor);
        accelerators.put(APPLY_CONTENT, this::applyContent);
    }

    /**
     * Switch appearance editor area.
     */
    private void switchEditor() {
        if (cssArea.isVisible()) {
            cssArea.setManaged(false);
            cssArea.setVisible(false);
            stage.setWidth(600.0);
            if (close == null) {
                close = makeSimpleElasticTransition(1000.0, 600.0);
                close.setCycleCount(1);
            }
            close.play();
            return;
        }
        if (open == null) {
            open = makeSimpleElasticTransition(600.0, 1000.0);;
            open.setCycleCount(1);
        }

        cssArea.setManaged(true);
        cssArea.setVisible(true);
        stage.setWidth(1000.0);
        open.play();
    }

    private Transition makeSimpleElasticTransition(
            final double startWidth,
            final double endWidth
            ) {
        return new Transition() {
            {
                setCycleDuration(Duration.millis(1000));
            }

            @Override
            protected void interpolate(double frac) {
                final double gap = endWidth - startWidth;
                stage.setWidth(startWidth + gap * frac);
            }

        };
    }

    private void applyContent() {
        save(cssArea.getText());
        setStyle("generated.css");
    }

    @SuppressWarnings("unchecked")
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

    /**
     * Initialize CSS' CodeArea.
     */
    private void initCodeArea() {
        cssArea.setParagraphGraphicFactory(LineNumberFactory.get(cssArea));
        cssArea.setManaged(false);
        cssArea.setVisible(false);
    }

    /**
     * close this app.
     */
    @FXML
    private void close() {
        this.stage.close();
        //System.exit(0);
    }

}
