package sample;

import org.sqlite.core.DB;

import java.sql.*;
import java.time.LocalDate;
import java.util.Locale;

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
 * 1.5 - Updated createUserEntry and insertIntoUserTable to take hash and salt rather than password. Updated
 *       getPasswordFromUsername to getHashFromUsername. Added getSaltFromUsername. Updated userColumns enum to include
 *       hash and salt. Updated insertIntoUserTable to use PreparedStatement so that data is not converted to String
 *       before input to database, important for hash and salt which are put into BLOB type on SQLite side meaning they
 *       will retain the datatype passed in as.
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
            USER_ID, FIRST_NAME, LAST_NAME, DOB, HEIGHT, SEX, USERNAME, HASH, SALT, EMAIL
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
     * Method to create a pseudo atomic action of inserting a user into the user table and creating that users initial
     * weight value, we need ot do this as a method as it will likely be the method that the main registration form
     * links to
     *
     * @param user An object representing a user, containing data needed to create a database entry
     * @param hash A hashed representation of the user's password
     * @param salt The salt used to to generate the user's password hash
     */
    public void createUserEntry(User user, byte[] hash, byte[] salt){

        //First we want to run the insertIntoUserTable method, this will return true if the action completed successful
        // and as such, we can make this an atomic action
        insertIntoUserTable(user, hash, salt);

        System.out.println("User Added To Database Successfully");

        //If the aforementioned method returns true we need to log the userID that it created, we need to do this
        //because we did not explicitly state the value we are giving the userID and as such need to calculate it
        int userID = getUserIDFromUsername(user.getUsername());

        //Using the userID previously calculated, we will create the initial entry for the users weight
        insertWeightValue(userID, user.getWeight(), LocalDate.now());

        System.out.println("User Initial Weight Entry Added To Database Successfully");

        //Once this has all been completed, we have successfully created a user entry in the database
    }

    /**
     * Method to add a User to the database, this takes information from a filled user class to generate a table entry
     *
     * @param user An object representing a user, containing data needed to create a database entry
     * @param hash A hashed representation of the user's password
     * @param salt The salt used to to generate the user's password hash
     */
    public void insertIntoUserTable(User user, byte[] hash, byte[] salt) {
        System.out.println(user.getEmail());
        String sql =
                "INSERT INTO user (first_name, last_name, dob, height, sex, username, hash, salt, email)" +
                "VALUES(?,?,?,?,?,?,?,?,?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1,user.getFirstname());
            pstmt.setString(2, user.getSurname());
            pstmt.setString(3, user.getDob().toString());
            pstmt.setFloat(4, user.getHeight());
            pstmt.setString(5, user.getSex());
            pstmt.setString(6, user.getUsername());
            pstmt.setBytes(7, hash);
            pstmt.setBytes(8, salt);
            pstmt.setString(9, user.getEmail());

            pstmt.executeUpdate();
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
     * Method to retrieve the hashed representation of a user's password from the database.
     *
     * @param username A user's unique username
     * @return A hashed representation of that user's password
     * @throws SQLException if the connection or query to the database fails
     */
    public byte[] getHashFromUsername(String username) throws SQLException {
        byte[] hash = null;

        String sql = "SELECT hash FROM user WHERE username = '" + username + "'";

        try (Statement stmt  = this.conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql))
        {
            while (rs.next())
            {
                hash = rs.getBytes("hash");
            }
        }
        catch (SQLException e)
        {
            System.out.println("Could Not Find User");
            throw new SQLException();
        }

        return hash;
    }

    /**
     * Method to retrieve the salt used to hash a user's password from the database.
     *
     * @param username A user's unique username
     * @return The salt used for that user's password
     * @throws SQLException if the connection or query to the database fails
     */
    public byte[] getSaltFromUsername(String username) throws SQLException {
        byte[] salt = null;

        String sql = "SELECT salt FROM user WHERE username = '" + username + "'";

        try (Statement stmt  = this.conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql))
        {
            while (rs.next())
            {
                salt = rs.getBytes("salt");
            }
        }
        catch (SQLException e)
        {
            System.out.println("Could Not Find User");
            throw new SQLException();
        }

        return salt;
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
            stmt.executeUpdate(sql);
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }

    }

    public void addTokenEntry(String tokenVal, long timestamp){
        String sql = "INSERT INTO regTokens (tokenVal, timeDelay) VALUES" +
                "('" + tokenVal + "', '" + timestamp+1800 + "')";

        try {
            Statement stmt  = this.conn.createStatement();
            stmt.executeUpdate(sql);
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }

    public ResultSet getTokenResult(String tokenVal) throws SQLException {
        String sql = "SELECT tokenVal, timeDelay FROM regTokens WHERE tokenVal = '" + tokenVal + "'";

        ResultSet ret = null;

        Statement stmt  = this.conn.createStatement();

        return stmt.executeQuery(sql);

    }

    /**
     * Method to retrieve nutrition items from the database. It will return the first item where the start of
     * the item's name matches the input.
     *
     * @param itemName Takes in the a name to be searched in the database.
     *
     */
    public NutritionItem getNutritionItem(String itemName)  {
        String searchName = itemName + '%';
        String sql = "SELECT * FROM food WHERE name LIKE '" + searchName + "'";

        NutritionItem nutritionItem = new NutritionItem();

        try (Statement stmt  = this.conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)) {

            nutritionItem = new NutritionItem(rs.getString("name"),
                    rs.getFloat("kcal"), rs.getFloat("protein"),
                    rs.getFloat("fat"), rs.getFloat("carbs"),
                    rs.getFloat("sugar"), rs.getFloat("fibre"),
                    rs.getFloat("cholesterol"));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return nutritionItem;
    }

    /**
     * Method to retrieve exercise items from the database. It will return the first item where the start of
     * the item's name matches the input.
     *
     * @param itemName Takes in the a name to be searched in the database.
     *
     */
    public ExerciseItem getExerciseItem(String itemName)  {
        String searchName = itemName + '%';
        String sql = "SELECT * FROM exercise WHERE name LIKE '" + searchName + "'";

        ExerciseItem exerciseItem = new ExerciseItem();

        try (Statement stmt  = this.conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)) {

            exerciseItem = new ExerciseItem(rs.getString("name"),
                    rs.getInt("burn_rate"));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return exerciseItem;
    }

    public void deleteToken(String token){
        String sql = "DELETE FROM regTokens WHERE tokenVal = '" + token + "'";

        try {
            Statement stmt  = this.conn.createStatement();
            stmt.executeQuery(sql);
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }

    public void editValue(String table, String column, String valToUpdateTo, String username){
    String sql = "UPDATE " + table.toUpperCase(Locale.ROOT) +
            " SET " + column.toUpperCase(Locale.ROOT) + " = " + valToUpdateTo +
            " WHERE username = " + username + "'";

    //TODO error handling
}

    public void editValue(String table, String column, int valToUpdateTo, String username){
        String sql = "UPDATE " + table.toUpperCase(Locale.ROOT) +
                " SET " + column.toUpperCase(Locale.ROOT) + " = " + valToUpdateTo +
                " WHERE username = " + username + "'";
    }

    public User createUserObjectFromUsername(String username){
        String sql = "SELECT * FROM user WHERE username = '" + username + "'";

        User user = null;

        try (Statement stmt  = this.conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql))
        {
            user = new User(rs.getString("first_name"),
                    rs.getString("last_name"),
                    User.Sex.valueOf(rs.getString("sex").toUpperCase(Locale.ROOT)),
                    LocalDate.parse(rs.getString("dob")),
                    rs.getString("email"),
                    rs.getString("username")
            );
        }
        catch (SQLException e)
        {
            System.out.println("Could Not Find User");
        }

        return user;
    }
}
