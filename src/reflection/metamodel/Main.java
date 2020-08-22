package reflection.metamodel;

import reflection.metamodel.model.Person;
import reflection.metamodel.util.ColumnField;
import reflection.metamodel.util.Metamodel;
import reflection.metamodel.util.PrimaryKeyField;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        Metamodel metamodel = Metamodel.of(Person.class);

        PrimaryKeyField primaryKeyField = metamodel.getPrimaryKeyField();
        List<ColumnField> columnFields = metamodel.getColumns();

        System.out.println("Primary key name = " + primaryKeyField.getName() + ", type = " + primaryKeyField.getType().getSimpleName());
        columnFields.forEach(column -> System.out.println("Column name = " + column.getName() + ", type = " + column.getType().getSimpleName()));

    }
}
