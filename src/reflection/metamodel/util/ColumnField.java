package reflection.metamodel.util;

import reflection.metamodel.annotations.Column;

import java.lang.reflect.Field;

public class ColumnField {

    private final Field field;
    private final Column column;

    public ColumnField(Field field) {
        this.field = field;
        this.column = field.getAnnotation(Column.class);
    }

    public Field getField() {
        return field;
    }

    public String getName() {
        return column.name();
    }

    public Class<?> getType() {
        return field.getType();
    }
}
