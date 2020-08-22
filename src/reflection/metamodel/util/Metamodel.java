package reflection.metamodel.util;

import reflection.metamodel.annotations.Column;
import reflection.metamodel.annotations.PrimaryKey;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Metamodel {

    private final Class<?> clss;

    private Metamodel(Class<?> clss) {
        this.clss = clss;
    }

    public static Metamodel of(Class<?> clss) {
        return new Metamodel(clss);
    }

    public PrimaryKeyField getPrimaryKeyField() {
        final Field[] fields = clss.getDeclaredFields();
        for (Field field : fields) {
            final PrimaryKey annotation = field.getAnnotation(PrimaryKey.class);
            if (annotation != null) {
                return new PrimaryKeyField(field);
            }
        }
        throw new IllegalArgumentException("No primary key in class: " + clss.getSimpleName());
    }

    public List<ColumnField> getColumns() {
        List<ColumnField> columnFields = new ArrayList<>();
        final Field[] fields = clss.getDeclaredFields();
        for (Field field : fields) {
            final Column annotation = field.getAnnotation(Column.class);
            if (annotation != null) {
                columnFields.add(new ColumnField(field));
            }
        }
        return columnFields;
    }

    public String buildInsertRequest() {
        // insert into Person (id, name, age) values (?, ?, ?)

        // Получаем выражение в первых скобках
        String columnElements = getColumnNames();
        // Получаем выражение во вторых скобках
        final String questionMarkElements = getQuestionMarkElements();

        return "insert into " + this.clss.getSimpleName() + " (" + columnElements + ") values (" + questionMarkElements + ")";
    }

    public String buildSelectRequest() {
        // select id, name, age from Person where id = ?
        String sql = "select " + getColumnNames() + " from " + clss.getSimpleName() + " where " + getPrimaryKeyField().getName() + " = ?";
        System.out.println(sql);
        return sql;
    }

    private String getQuestionMarkElements() {
        int numberOfColumns = getColumns().size() + 1;
        return IntStream.range(0, numberOfColumns).mapToObj(i -> "?").collect(Collectors.joining(", "));
    }

    private String getColumnNames() {
        String primaryKeyColumnName = getPrimaryKeyField().getName();
        List<String> columnNames =
                getColumns().stream().map(ColumnField::getName).collect(Collectors.toList());
        columnNames.add(0, primaryKeyColumnName);
        return String.join(", ", columnNames);
    }
}
