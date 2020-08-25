package reflection.metamodel;

import reflection.metamodel.beanmanager.BeanManager;
import reflection.metamodel.model.Person;
import reflection.metamodel.orm.EntityManager;
import reflection.metamodel.orm.ManagedEntityManager;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

public class ReadingObjects {

    public static void main(String[] args) throws SQLException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {

        BeanManager beanManager = BeanManager.getInstance();
        EntityManager<Person> entityManager = beanManager.getInstance(ManagedEntityManager.class);

        Person karl = entityManager.find(Person.class, 1L);
        Person friedrich = entityManager.find(Person.class, 2L);
        Person vladimir = entityManager.find(Person.class, 3L);
        Person iosif = entityManager.find(Person.class, 4L);

        System.out.println(karl.toString());
        System.out.println(friedrich.toString());
        System.out.println(vladimir.toString());
        System.out.println(iosif.toString());
    }
}
