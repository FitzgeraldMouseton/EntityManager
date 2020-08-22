package reflection.metamodel.orm;

import reflection.metamodel.util.ColumnField;
import reflection.metamodel.util.Metamodel;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.concurrent.atomic.AtomicLong;

public abstract class AbstractEntityManager<T> implements EntityManager<T> {

    // Все реализации jpa, включая Hibernate предоставляют генерацию id
    private final AtomicLong idGenerator = new AtomicLong(0L);

    @Override
    public void persist(T t) throws SQLException, IllegalAccessException {

        Metamodel metamodel = Metamodel.of(t.getClass());
        String sql = metamodel.buildInsertRequest();
        try (PreparedStatement statement = prepareStatementWith(sql).andParameters(t);) {
            statement.executeUpdate();
        }
    }

    @Override
    public T find(Class<T> clss, Object primaryKey) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Metamodel metamodel = Metamodel.of(clss);
        String sql = metamodel.buildSelectRequest();
        try (PreparedStatement statement = prepareStatementWith(sql).andPrimaryKey(primaryKey);
            final ResultSet resultSet = statement.executeQuery();) {
            return buildInstanceFrom(clss, resultSet);
        }
    }

    private PreparedStatementWrapper prepareStatementWith(String sql) throws SQLException {
        Connection connection = buildConnection();
        final PreparedStatement preparedStatement = connection.prepareStatement(sql);
        return new PreparedStatementWrapper(preparedStatement);

    }

    public abstract Connection buildConnection() throws SQLException;

    private T buildInstanceFrom(Class<T> clss, ResultSet resultSet) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, SQLException {
        Metamodel metamodel = Metamodel.of(clss);
        final T t = clss.getConstructor().newInstance();

        // Заполним поле primaryKey
        final Field primaryKeyField = metamodel.getPrimaryKeyField().getField();
        final String primaryKeyName = primaryKeyField.getName();
        final Class<?> primaryKeyType = primaryKeyField.getType();
        resultSet.next();
        if (primaryKeyType == long.class) {
            // Т.к. в ДБ id хранится в виде int - getInt
            final long primaryKey = resultSet.getInt(primaryKeyName);
            primaryKeyField.setAccessible(true);
            primaryKeyField.set(t, primaryKey);
        }

        // Заполним колонки
        for (ColumnField columnField: metamodel.getColumns()) {
            final Field field = columnField.getField();
            final String fieldName = field.getName();
            final Class<?> fieldType = field.getType();
            field.setAccessible(true);
            if (fieldType == int.class) {
                final int value = resultSet.getInt(fieldName);
                field.setInt(t, value);
            } else if (fieldType == String.class) {
                final String value = resultSet.getString(fieldName);
                field.set(t, value);
            }
        }
        return t;
    }

    private class PreparedStatementWrapper {

        private final PreparedStatement statement;

        public PreparedStatementWrapper(PreparedStatement statement) {
            this.statement = statement;
        }

        private PreparedStatement andParameters(T t) throws SQLException, IllegalAccessException {
            Metamodel metamodel = Metamodel.of(t.getClass());
            Class<?> primaryKeyType = metamodel.getPrimaryKeyField().getType();
            // PreparedStatement может работать с примитивами, строками и датами, и нужно добавить проверки на все эти типы,
            // если мы хотим создать общий EntityManager, но тут для краткости возьмем только long
            if (primaryKeyType == long.class) {
                final long id = idGenerator.incrementAndGet();
                // Устанавливаем сохраняеому объекту соответствующий id
                final Field field = metamodel.getPrimaryKeyField().getField();
                field.setAccessible(true);
                field.set(t, id);
                // Добавляем нужную информацию в statement
                statement.setLong(1, id);
            }

            for (int columnIndex = 0; columnIndex < metamodel.getColumns().size(); columnIndex++) {
                ColumnField columnField = metamodel.getColumns().get(columnIndex);
                final Class<?> fieldType = columnField.getType();
                final Field field = columnField.getField();
                field.setAccessible(true);
                final Object value = field.get(t);
                // Тут используется тот же паттер, что и для primaryKey несколькими строчками выше - проверяем тип объекта, и указываем,
                // что хотим делать с объектом такого типа
                if (fieldType == int.class) {
                    // 2 - т.к. нулевой колонки нет, а 1 - для primaryKey
                    statement.setInt(columnIndex + 2, (int) value);
                } else if (fieldType == String.class) {
                    statement.setString(columnIndex + 2, String.valueOf(value));
                }
            }
            return statement;
        }

        private PreparedStatement andPrimaryKey(Object primaryKey) throws SQLException {
            // Т.к. в качестве аргумента принимается Object, будут применен автобоксинг, следовательно нужно
            // проверять не long.class, а Long.class
            System.out.println(primaryKey.getClass());
            if (primaryKey.getClass() == Long.class) {
                statement.setLong(1, (long) primaryKey);
            }
            return statement;
        }
    }
}
