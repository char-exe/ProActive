package sample;

import java.sql.*;

// Code here has been written strictly for the purpose of connecting the database, it is not intended as the actual
// database handler. It has been lifted almost verbatim from https://www.sqlitetutorial.net/sqlite-java/select/, so
// may need to be referenced depending how much/what level of it remains when the actual handler is written.
public class DatabaseHandler
{
    private Connection connect()
    {
        String database = "jdbc:sqlite:proactive.db";
        Connection conn = null;

        try
        {
            conn = DriverManager.getConnection(database);
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }

        return conn;
    }

    public void selectAllExercises()
    {
        String sql = "SELECT id, name, burn_rate FROM exercise";

        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql))
        {
            while (rs.next())
            {
                System.out.println(rs.getInt("id") + ", " +
                                   rs.getString("name") + ", " +
                                   rs.getInt("burn_rate"));
            }
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args)
    {
        DatabaseHandler dh = new DatabaseHandler();
        dh.selectAllExercises();
    }
}
