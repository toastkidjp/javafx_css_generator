package jp.toastkid.gui.jfx.cssgen;

import javafx.scene.paint.Color;

/**
 * Color Utilities.
 *
 * @author Toast kid
 *
 */
public class ColorUtils {

    /**
     * Deny make instance.
     */
    private ColorUtils() {
        // NOP.
    }

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

}
