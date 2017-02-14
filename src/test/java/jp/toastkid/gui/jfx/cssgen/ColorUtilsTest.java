package jp.toastkid.gui.jfx.cssgen;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import javafx.scene.paint.Color;

/**
 * {@link Utilities}'s test cases.
 *
 * @author Toast kid
 *
 */
public class ColorUtilsTest {

    /**
     * Test of {@link ColorUtils#makeRgbStr(Color)}.
     */
    @Test
    public void test_makeRgbStr() {
        assertEquals("0.000000, 0.000000, 0.000000", ColorUtils.makeRgbStr(Color.BLACK));
    }

    /**
     * Test of {@link ColorUtils#toRgbCode(Color)}.
     */
    @Test
    public void test_toRgbCode() {
        assertEquals("#000000", ColorUtils.toRgbCode(Color.BLACK));
    }

}
