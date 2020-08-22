package reflection.metamodel.util;

import reflection.metamodel.annotations.PrimaryKey;

import java.lang.reflect.Field;

public class PrimaryKeyField {

    private final Field field;
    private final PrimaryKey primaryKey;

    public PrimaryKeyField(Field field) {
        this.field = field;
        primaryKey = field.getAnnotation(PrimaryKey.class);
    }

    public Field getField() {
        return field;
    }

    public Class<?> getType() {
        return field.getType();
    }

    public String getName() {
        return primaryKey.name();
    }
}
