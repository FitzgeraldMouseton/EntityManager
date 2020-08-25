package reflection.metamodel;

import reflection.metamodel.beanmanager.BeanManager;
import reflection.metamodel.model.Person;
import reflection.metamodel.orm.EntityManager;
import reflection.metamodel.orm.ManagedEntityManager;

import java.sql.SQLException;

public class WritingObjects {

    public static void main(String[] args) throws SQLException, IllegalAccessException {

        BeanManager beanManager = BeanManager.getInstance();
        EntityManager<Person> entityManager = beanManager.getInstance(ManagedEntityManager.class);

        Person karl = new Person("Karl", 64);
        Person friedrich = new Person("Friedrich", 71);
        Person vladimir = new Person("Vladimir", 47);
        Person iosif = new Person("Iosif", 74);

        System.out.println(karl.toString());
        System.out.println(friedrich.toString());
        System.out.println(vladimir.toString());
        System.out.println(iosif.toString());

        entityManager.persist(karl);
        entityManager.persist(friedrich);
        entityManager.persist(vladimir);
        entityManager.persist(iosif);

        System.out.println(karl.toString());
        System.out.println(friedrich.toString());
        System.out.println(vladimir.toString());
        System.out.println(iosif.toString());
    }
}
