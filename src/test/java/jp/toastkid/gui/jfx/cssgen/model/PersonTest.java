package jp.toastkid.gui.jfx.cssgen.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * {@link Person}'s test case.
 *
 * @author Toast kid
 *
 */
public class PersonTest {

    /** Test object. */
    private Person person;

    /**
     * Set up test object.
     */
    @Before
    public void setUp() {
        person = new Person.Builder().setName("John").setId(1).setActive(true).build();
    }

    /**
     * Test of {@link Person}.
     */
    @Test
    public void test() {
        assertEquals("John", person.nameProperty().get());
        assertEquals(1L, person.idProperty().get());
        assertTrue(person.activeProperty().get());
    }

}
