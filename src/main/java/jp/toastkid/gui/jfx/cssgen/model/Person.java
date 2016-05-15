package jp.toastkid.gui.jfx.cssgen.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Sample POJO class.
 * @author Toast kid
 *
 */
public class Person {

    private final long    id;

    private final String  name;

    private final boolean active;

    public static class Builder {

        private long    id;

        private String  name;

        private boolean active;

        public Builder setId(final long id) {
            this.id = id;
            return this;
        }

        public Builder setName(final String name) {
            this.name = name;
            return this;
        }

        public Builder setActive(final boolean active) {
            this.active = active;
            return this;
        }

        public Person build() {
            return new Person(this);
        }
    }

    private Person(final Builder b) {
        this.id     = b.id;
        this.name   = b.name;
        this.active = b.active;
    }

    public LongProperty idProperty() {
        return new SimpleLongProperty(id);
    }

    public StringProperty nameProperty() {
        return new SimpleStringProperty(name);
    }

    public BooleanProperty activeProperty() {
        return new SimpleBooleanProperty(active);
    }
}
