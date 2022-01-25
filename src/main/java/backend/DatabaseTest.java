package backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Работа с базами данных
 */
public class DatabaseTest {

    public String databaseUrl = "jdbc:postgresql://10.15.3.39:5432/DEVELOP_CR_PG";
    public String databaseUser = "SYSDBA";
    public String databasePassword = "masterkey";
    public String databaseDriver = "org.postgresql.Driver";
    public Connection connection;

    public void Connect() {
        System.out.print("Connecting to DB: Url=[" + databaseUrl + "],"
                + " User=[" + databaseUser + "],"
                + " Password=[" + databasePassword + "]...");
        try {
            connection = DriverManager
                    .getConnection(databaseUrl, databaseUser, databasePassword);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Connect Ok");
    }

    public void Select1() {
        // Простой цикл по запросу
        System.out.println("Простой цикл по запросу");
        try {
            PreparedStatement ps = connection.prepareStatement(""
                    + " SELECT document_id, "
                    + "        document_name "
                    + " FROM   document "
                    + " WHERE  document_id >  ? "
                    + " ORDER BY 1");
            ps.setInt(1, 1);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Integer document_id = rs.getInt("Document_id");
                String document_name = rs.getString("Document_name");
                System.out.println(document_id + " " + document_name);
            }
            ps.close();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    public void top1(){

    }
}
