package jp.toastkid.gui.jfx.cssgen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.scene.paint.Color;

/**
 * Presenter.
 * @author Toast kid
 *
 */
public class Utilities {

    /** replace regex. */
    private static final Pattern PARAM_TEMPLATE_PATTERN
        = Pattern.compile("\\$\\{(.+?)\\}", Pattern.DOTALL);


    /**
     * make rgb string.
     * @param color Color object
     * @return ex) 255.0, 255.0, 255.0
     */
    public static String makeRgbStr(final Color main) {
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
    public static String toRgbCode(final Color color) {
        return String.format( "#%02X%02X%02X",
            (int)( color.getRed()   * 255 ),
            (int)( color.getGreen() * 255 ),
            (int)( color.getBlue()  * 255 ) );
    }

    /**
     * read resource with parameter.
     * @param pathToTemplate
     * @param params
     * @return
     * @throws IOException
     */
    public static final String bindArgs(
            final String pathToTemplate,
            final Map<String, String> params
            ) throws IOException {
        final String lineSeparator = System.lineSeparator();
        final List<String> templates = readLinesFromStream(pathToTemplate);
        final StringBuilder convertedText = new StringBuilder();
        templates.forEach(template -> {
            if (template.contains("${")) {
                Matcher matcher = PARAM_TEMPLATE_PATTERN.matcher(template);
                while (matcher.find()) {
                    final String key = matcher.group(1);
                    if (!params.containsKey(key)) {
                        continue;
                    }
                    String value = params.get(key);
                    if (value == null) {
                        continue;
                    }
                    if (value.contains("$") && !value.contains("\\$")) {
                        value = value.replace("$", "\\$");
                    }
                    //System.out.println("value = " + value);
                    template = matcher.replaceFirst(value);
                    matcher = PARAM_TEMPLATE_PATTERN.matcher(template);
                }
            }
            convertedText.append(template).append(lineSeparator);
        });
        return convertedText.toString();
    }

    /**
     * read resource's content from stream.
     * @param filePath
     * @return
     */
    public static List<String> readLinesFromStream(final String filePath) {
        final List<String> resSet = new ArrayList<String>(100);
        final InputStream in = Utilities.class.getClassLoader().getResourceAsStream(filePath);
        try (final BufferedReader fileReader
                = new BufferedReader(new InputStreamReader(in , StandardCharsets.UTF_8));) {
            String str = fileReader.readLine();
            while (str != null) {
                resSet.add(str);
                str = fileReader.readLine();
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return resSet;
    }
}
