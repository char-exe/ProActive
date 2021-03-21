package sample;

import java.sql.*;
import java.time.LocalDate;

/**
 * Contains ways to interact with the backend database of the ProActive app, contains a number of
 * enums which contain first each table in the database, then each column in that table, for example
 * dbTables.foodColumns.KCAL is the column for kcal inside of the table food
 *
 * Initial creation was with heavy reference to https://www.sqlitetutorial.net/sqlite-java/select/ though the class
 * has changed significantly since its inception.
 *
 * @author Samuel Scarfe
 * @author Owen Tasker
 *
 * @version 1.4
 *
 * 1.0 - Initial handler created, methods with ability to select all information from a table added
 *
 * 1.1 - Added methods to insert a user to a table as well as add their weight with supporting methods such as finding
 *       the userID based on a username
 *
 * 1.2 - Refactored code to a more OOP approach, created the constructor and created a method to (as much as possible)
 *       make the creation of a user an atomic action of creating the user and creating an initial weight entry, this
 *       reinforces the principle of DRY in that instead of connecting to the database every query, we only connect
 *       once at the initial creation, this improves response time significantly
 *
 * 1.3 - Added enums for all db tables and columns, this means that we can limit the values passed into methods to
 *       ensure type safety
 *
 * 1.4 - Added Javadoc comments for all methods
 *
 */
public class DatabaseHandler
{
    public enum dbTables{
        ACTIVITY, EXERCISE, FOOD, MEAL, USER, WEIGHT_ENTRY;

        public enum activityColumns {
            ACTIVITY_ID, EXERCISE_ID, USER_ID, DURATION
        }
        public enum exerciseColumns {
            ID, NAME, BURN_RATE
        }
        public enum foodColumns{
            ID, NAME, KCAL, PROTEIN, FAT, CARBS, SUGAR, FIBRE, CHOLESTEROL
        }
        public enum mealColumns{
            MEAL_ID, MEAL_CATEGORY, FOOD_ID, USER_ID, DATE, QUANTITY
        }
        public enum userColumns{
            USER_ID, FIRST_NAME, LAST_NAME, DOB, HEIGHT, SEX, USERNAME, PASSWORD
        }
        public enum weightEntryColumns{
            ENTRY_ID, USER_ID, WEIGHT, DATE
        }
    }

    private Connection conn;

    /**
     * Only accepted constructor for the database handler, takes in the path of the database and dynamically
     * connects to that in order to avoid having to connect every time this object runs a SQL method
     *
     * @param database Path to the database
     */
    public DatabaseHandler(String database){
        try{
            conn = DriverManager.getConnection(database);
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Method to select all information from a table, returns a ResultSet which will contain the contents of
     * aforementioned table
     *
     * @param table takes in a dbTable enum, this is to ensure type safety
     *
     * @return Returns a ResultSet which will contain a large amount of information which can be analyzed by another
     *         process
     */
    public ResultSet selectAllFromTable(dbTables table){

        String sql = "SELECT * FROM " + table.toString().toLowerCase();

        try {
             Statement stmt  = this.conn.createStatement();
             return stmt.executeQuery(sql);
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Method to add a User to the database, this takes information from a filled user class to generate a table entry
     *
     * @param user      Takes in a user object which will be used to fill out details for the INSERT statement
     * @param password  Takes in a String representation of a password, this is required as it is not stored within
     *                  the User class, we do this to reduce the amount of time the password is stored in a front
     *                  end location
     */
    public void insertIntoUserTable(User user, String password) {
        String sql =
                "INSERT INTO user (first_name, last_name, dob, height, sex, username, password)" +
                "VALUES('" + user.getFirstname() + "', '" + user.getSurname() + "','" + user.getDob().toString() +
                        "','" + user.getHeight() + "','" + user.getSex() + "','" + user.getUsername() +
                        "','" + password + "')";

        try {
             Statement stmt  = this.conn.createStatement();
             stmt.executeQuery(sql);
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Method to find the userID of a user based on their username, we need this because we do not manually assign
     * userID's, that is done by the database and is not stored within a user object
     *
     * @param username Takes in a users username
     *
     * @return returns an int which represents the users userID, this can then be used within other database
     *         methods to insert data that require a userID as a foreign key
     */
    public int getUserIDFromUsername(String username){
        int userID = -1;

        String sql = "SELECT user_id FROM user WHERE username = '" + username + "'";

        try (Statement stmt  = this.conn.createStatement();
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

    /**
     * Method to check that a username exists, if it does, return the password related to that username,
     * otherwise throw an SQLException
     *
     * @throws SQLException throws if the username does not exist
     */
    public String getPassFromUsername(String username) throws SQLException {
        String pass = null;

        String sql = "SELECT username, password FROM user WHERE username = '" + username + "'";

        try (Statement stmt  = this.conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql))
        {
            while (rs.next())
            {
                pass = rs.getString("password");
            }
        }
        catch (SQLException e)
        {
            System.out.println("Could Not Find User");
            throw new SQLException();
        }

        return pass;
    }

    public boolean checkUserNameUnique(String username){
        String sql = "SELECT * FROM user WHERE username = '" + username + "'";

        try {
            Statement stmt = this.conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (!rs.next()) {
                System.out.println("Username is unique");
                return true;
            } else {
                return false;
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return false;
    }

    /**
     * Method to insert a weight value into the weight_entry database table
     *
     * @param userID Takes in a users unique ID given to them by the DB, this will act as a foreign key and link
     *               the user class to the weight_entry class
     * @param weight Takes in the current weight of the user in kg
     * @param date   Takes in the time in which the user wants to set this date to, this will most likely always be
     *               LocalDate.now()
     */
    public void insertWeightValue(int userID, float weight, LocalDate date){
        String sql = "INSERT INTO weight_entry (user_id, weight, date)" +
                     "VALUES('" + userID + "', '" + weight + "','" + date.toString() + "')";

        try {
            Statement stmt  = conn.createStatement();
            stmt.executeQuery(sql);
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }

    }

    /**
     * Method to create a pseudo atomic action of inserting a user into the user table and creating that users initial
     * weight value, we need ot do this as a method as it will likely be the method that the main registration form
     * links to
     *
     * @param user Takes in a User object, this will be used in all methods called by this method.
     */
    public void createUserEntry(User user, String password){

        //First we want to run the insertIntoUserTable method, this will return true if the action completed successful
        // and as such, we can make this an atomic action
        insertIntoUserTable(user, password);

        System.out.println("User Added To Database Successfully");

        //If the aforementioned method returns true we need to log the userID that it created, we need to do this
        //because we did not explicitly state the value we are giving the userID and as such need to calculate it
        int userID = getUserIDFromUsername(user.getUsername());

        //Using the userID previously calculated, we will create the initial entry for the users weight
        insertWeightValue(userID, user.getWeight(), LocalDate.now());

        System.out.println("User Initial Weight Entry Added To Database Successfully");

        //Once this has all been completed, we have successfully created a user entry in the database
    }

    public static void main(String[] args) {
        DatabaseHandler dh = new DatabaseHandler("jdbc:sqlite:proactive.db");

        ResultSet userTable = dh.selectAllFromTable(dbTables.USER);

        try{
            while (userTable.next()){
                System.out.println(userTable.getString(dbTables.userColumns.USERNAME.toString().toLowerCase()));
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }

    }
}
