package reflection.metamodel.provider;

import reflection.metamodel.annotations.Provides;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class H2ConnectionProvider {

    // Аннотация @Provides в данном случае нужна, чтобы указать, что именно этот ментод надо будет вызывать через рефлексию,
    // чтобы создать Connection
    @Provides
    public Connection buildConnection() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:h2:/Users/scholar/IdeaProjects/pluralsight/lambda-expressions-JosePamaurd/db-files/db-pluralsight",
                "sa", "");
    }
}
