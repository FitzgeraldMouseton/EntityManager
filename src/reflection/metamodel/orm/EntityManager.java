package reflection.metamodel.orm;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

public interface EntityManager<T> {

    void persist(T t) throws SQLException, IllegalAccessException;

    T find(Class<T> clss, Object l) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;
}
