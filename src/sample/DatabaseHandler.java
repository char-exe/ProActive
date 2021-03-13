package sample;

import java.sql.*;
import java.time.LocalDate;
import java.time.Month;

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

    public boolean insertIntoUserTable(String firstName, String lastName, LocalDate dob, float height, String sex,
                                    String username, String password) {
        String sql =
                "INSERT INTO user (first_name, last_name, dob, height, sex, username, password)" +
                "VALUES('" + firstName + "', '" + lastName + "','" + dob.toString() +
                "','" + height + "','" + sex + "','" + username + "','" + password + "')";

        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql))
        {
            System.out.println("User Inserted Successfully");
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public int getuserIDFromUsername(String username){
        int userID = -1;

        String sql = "SELECT user_id FROM user WHERE username = '" + username + "'";

        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql))
        {
            while (rs.next())
            {
                userID = rs.getInt("user_id");
            }
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return userID;
    }

    public void insertWeightValue(int userID, float weight, LocalDate date){
        String sql =
                "INSERT INTO weight_entry (user_id, weight, date)" +
                        "VALUES('" + userID + "', '" + weight + "','" + date.toString() + "')";

        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql))
        {
            System.out.println("Weight Entry successfully inserted");
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }

    }

    public void createUserEntry(DatabaseHandler dh, User user){

        //First we want to run the insertIntoUserTable method, this will return true if the action completed successful
        // and as such, we can make this an atomic action
        dh.insertIntoUserTable(user.getFirstname(), user.getSurname(), user.getDob(), user.getHeight(),
            user.getSex(), user.getUsername(), "testPassword");

        System.out.println("User Added To Database Successfully");

        //If the aforementioned method returns true we need to log the userID that it created, we need to do this
        //because we did not explicitly state the value we are giving the userID and as such need to calculate it
        int userID = getuserIDFromUsername(user.getUsername());

        //Using the userID previously calculated, we will create the initial entry for the users weight
        insertWeightValue(userID, user.getWeight(), LocalDate.now());

        System.out.println("User Initial Weight Entry Added To Database Successfully");

        //Once this has all been completed, we have successfully created a user entry in the database
    }



    public static void main(String[] args)
    {


    }
}
