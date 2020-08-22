package reflection.metamodel.orm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Реализация AbstractEntityManager для H2.
 * @param <T>
 */

public class H2EntityManager<T> extends AbstractEntityManager<T> {

    @Override
    public Connection buildConnection() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:h2:/Users/scholar/IdeaProjects/pluralsight/lambda-expressions-JosePamaurd/db-files/db-pluralsight",
                "sa", "");
    }
}
