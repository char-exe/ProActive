package sample;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Contains ways to interact with the backend database of the ProActive app, contains a number of
 * enums which contain first each table in the database, then each column in that table, for example
 * dbTables.foodColumns.KCAL is the column for kcal inside of the table food
 *
 * Initial creation was with heavy reference to https://www.sqlitetutorial.net/sqlite-java/select/ though the class
 * has changed significantly since its inception.
 *
 * Data for the food table can be found at
 * https://www.gov.uk/government/publications/composition-of-foods-integrated-dataset-cofid.
 * as per page 37 of the pdf available for download, it is available on an Open Government Licence, details of which
 * can be found at https://www.nationalarchives.gov.uk/doc/open-government-licence/version/3/
 *
 * Data for the daily_intake table can be found at
 * https://assets.publishing.service.gov.uk/government/uploads/system/uploads/attachment_data/file/618167/government_dietary_recommendations.pdf
 * as per page 2 of the document, it is available on an Open Government Licence, details of which can be found at
 * https://www.nationalarchives.gov.uk/doc/open-government-licence/version/3/
 *
 * @author Samuel Scarfe
 * @author Owen Tasker
 * @author Charlie Jones
 * @author Evan Clayton
 *
 * @version 1.16
 *
 * 1.0 - Initial handler created, methods with ability to select all information from a table added
 * 1.1 - Added methods to insert a user to a table as well as add their weight with supporting methods such as finding
 *       the userID based on a username
 * 1.2 - Refactored code to a more OOP approach, created the constructor and created a method to (as much as possible)
 *       make the creation of a user an atomic action of creating the user and creating an initial weight entry, this
 *       reinforces the principle of DRY in that instead of connecting to the database every query, we only connect
 *       once at the initial creation, this improves response time significantly
 * 1.3 - Added enums for all db tables and columns, this means that we can limit the values passed into methods to
 *       ensure type safety
 * 1.4 - Added Javadoc comments for all methods
 * 1.5 - Updated createUserEntry and insertIntoUserTable to take hash and salt rather than password. Updated
 *       getPasswordFromUsername to getHashFromUsername. Added getSaltFromUsername. Updated userColumns enum to include
 *       hash and salt. Updated insertIntoUserTable to use PreparedStatement so that data is not converted to String
 *       before input to database, important for hash and salt which are put into BLOB type on SQLite side meaning they
 *       will retain the datatype passed in as.
 * 1.6 - Updated such that database string no longer needs to be passed as an argument. Added method for getting intake
 *       data for a user from the database. Added method for getting minutes spent data from database. Added method
 *       for getting burn rate in calories per minute and calories burned data for user. Added method for getting
 *       weight entries for a user from the database.
 * 1.7 - Minor refactor to enforce the Singleton pattern.
 * 1.8 - Added Javadoc for outstanding methods
 * 1.9 - Added methods to assist with activity logging.
 * 1.95 - Added methods to assist with water intake logging and information retrieval.
 * 1.10 - Rewrote getBurnedEntries and getIntakeEntries as JOIN statements to fix exception originating from having
 *        nested ResultSets.
 * 1.11 - Implemented adding and updating goals.
 *
 * 1.12 - Added methods for adding elements to exercise and food tables, used for custom item creation.
 *
 * 1.13 - Implemented automatic goal generation.
 *
 * 1.14 - Added method to return all groups in which the user is in.
 *
 * 1.15 - Added exceptions. Some slight refactoring to remove unused methods and add private helper methods.
 * 1.16 - Added required methods for group goal functionality. These include refreshing, inserting and selecting group
 *        goals. Additionally individual goal methods have been updated to support group_id inclusion where appropriate.
 */
public class DatabaseHandler {
    private static final DatabaseHandler INSTANCE = new DatabaseHandler();
    private static final String CONNECTION = "jdbc:sqlite:proactive.db";
    private Connection conn;

    /**
     * Private default constructor. Enforces the Singleton pattern.
     */
    private DatabaseHandler() {
        try {
            conn = DriverManager.getConnection(CONNECTION);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Static method to get the single DatabaseHandler instance.
     *
     * @return the DatabaseHandler instance.
     */
    public static DatabaseHandler getInstance() {
        return INSTANCE;
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
    public void createUserEntry(User user, byte[] hash, byte[] salt) throws SQLException {
        if (user == null) {
            throw new NullPointerException();
        }
        if (hash == null) {
            throw new NullPointerException();
        }
        if (salt == null) {
            throw new NullPointerException();
        }

        //First we want to run the insertIntoUserTable method, this will return true if the action completed successful
        // and as such, we can make this an atomic action
        insertIntoUserTable(user, hash, salt);

        //If the aforementioned method returns true we need to log the userID that it created, we need to do this
        //because we did not explicitly state the value we are giving the userID and as such need to calculate it.
        //Using the userID previously calculated, we will create the initial entry for the users weight
        insertWeightValue(user.getUsername(), user.getWeight(), LocalDate.now());

        //Once this has all been completed, we have successfully created a user entry in the database
    }

    /**
     * Private method to add a User to the database, this takes information from a filled user class to generate a table entry
     *
     * @param user An object representing a user, containing data needed to create a database entry
     * @param hash A hashed representation of the user's password
     * @param salt The salt used to to generate the user's password hash
     */
    private void insertIntoUserTable(User user, byte[] hash, byte[] salt) {
        if (user == null) {
            throw new NullPointerException();
        }
        if (hash == null) {
            throw new NullPointerException();
        }
        if (salt == null) {
            throw new NullPointerException();
        }

        String sql =
                "INSERT INTO user (first_name, last_name, dob, height, sex, username, hash, salt, email)" +
                "VALUES(?,?,?,?,?,?,?,?,?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
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
        catch (SQLException e) {
            e.printStackTrace();
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
    public int getUserIDFromUsername(String username) {
        if (username == null) {
            throw new NullPointerException();
        }

        int userID = -1;

        String sql = "SELECT user_id FROM user WHERE username = '" + username + "'";

        try (Statement stmt  = this.conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)) {
            while (rs.next()) {
                userID = rs.getInt("user_id");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return userID;
    }

    /**
     * Method to get a user's height from their userID.
     *
     * @param userID the User's unique ID.
     * @return the User's height.
     */
    public float getHeightFromUserID(int userID) {
        if (userID < 0) {
            throw new IllegalArgumentException();
        }

        float userHeight = -1;

        String sql = "SELECT height FROM user WHERE user_id= " + userID + "";

        try (Statement stmt  = this.conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)) {
            while (rs.next()) {
                userHeight = rs.getFloat("height");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return userHeight;

    }

    /**
     * Method to find the username of a user based on their UserID
     *
     * @param userID Takes in a users userID
     *
     * @return returns an String which represents the users username
     */
    public String getUsernameFromUserID(int userID) {
        if (userID < 0) {
            throw new IllegalArgumentException();
        }

        String username = "";

        String sql = "SELECT username FROM user WHERE user_id = " + userID;

        try (Statement stmt  = this.conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql))
        {
            while (rs.next())
            {
                username = rs.getString("username");
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return username;
    }

    /**
     * Method to retrieve the hashed representation of a user's password from the database.
     *
     * @param username A user's unique username
     * @return A hashed representation of that user's password
     * @throws SQLException if the connection or query to the database fails
     */
    public byte[] getHashFromUsername(String username) throws SQLException {
        if (username == null) {
            throw new NullPointerException();
        }

        byte[] hash = null;

        String sql = "SELECT hash FROM user WHERE username = '" + username + "'";

        try (Statement stmt  = this.conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)) {
            while (rs.next()) {
                hash = rs.getBytes("hash");
            }
        }
        catch (SQLException e) {
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
        if (username == null) {
            throw new NullPointerException();
        }

        byte[] salt = null;

        String sql = "SELECT salt FROM user WHERE username = '" + username + "'";

        try (Statement stmt  = this.conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)) {
            while (rs.next()) {
                salt = rs.getBytes("salt");
            }
        }
        catch (SQLException e) {
            throw new SQLException();
        }

        return salt;
    }

    /**
     * Method to compare a provided username with all usernames currently stored in the database and return true if
     * username is unique
     *
     * @param username A username provided by the user of the application
     * @return true if username is unique, false otherwise
     */
    public boolean checkUserNameUnique(String username){
        if (username == null) {
            throw new NullPointerException();
        }

        String sql = "SELECT * FROM user WHERE username = '" + username + "'";

        try {
            Statement stmt = this.conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (!rs.next()) {
                return true;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Method to check whether an email submitted already features in the user database.
     *
     * @param email the email to be checked.
     * @return a boolean representing whether the email features in the database.
     */
    public boolean checkEmailUnique(String email){
        if (email == null) {
            throw new NullPointerException();
        }

        String sql = "SELECT * FROM user WHERE email = '" + email + "'";

        try {
            Statement stmt = this.conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (!rs.next()) {
                return false;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * Method to insert a weight value into the weight_entry database table
     *
     * @param username The user's username.
     * @param weight   Takes in the current weight of the user in kg
     * @param date     Takes in the time in which the user wants to set this date to.
     * @throws SQLException when a database error occurs.
     */
    public void insertWeightValue(String username, float weight, LocalDate date) throws SQLException {
        if (username == null) {
            throw new NullPointerException();
        }
        if (weight < 1) {
            throw new IllegalArgumentException();
        }
        if (date == null) {
            throw new NullPointerException();
        }
        if (date.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException();
        }

        String sql = "INSERT INTO weight_entry (user_id, weight, date_of)" +
                "VALUES('" + getUserIDFromUsername(username) + "', '" + weight + "','" + date.toString() + "')";

        Statement stmt  = conn.createStatement();
        stmt.executeUpdate(sql);
    }

    /**
     * Method to add a token entry to the regtokens table in the database
     *
     * @param tokenVal Takes in the unique token that was randomly generated by the {@link TokenHandler} class
     */
    public void addTokenEntry(String tokenVal) {
        if (tokenVal == null) {
            throw new NullPointerException();
        }

        LocalDateTime now = LocalDateTime.now();

        String sql = "INSERT INTO regTokens (tokenVal, sent_time) VALUES" +
                "('" + tokenVal + "', '" + now.toString() + "')";

        try {
            Statement stmt  = this.conn.createStatement();
            stmt.executeUpdate(sql);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to get information about a token, this information contains both the token value and the expiration time
     * for that token
     *
     * @param tokenVal The token that will be pulled from the database
     *
     * @return the sent time of the token as a LocalDateTime
     *
     * @throws SQLException this occurs if there is an error in the executing of the SQL statement, often this will
     *                      manifest due to incorrectly formatted code or if the database is not available at the time
     *                      of sql execution
     */
    public LocalDateTime getTokenResult(String tokenVal) throws SQLException {
        if (tokenVal == null) {
            throw new NullPointerException();
        }

        String sql = "SELECT tokenVal, sent_time FROM regTokens WHERE tokenVal = '" + tokenVal + "'";

        Statement stmt  = this.conn.createStatement();

        ResultSet rs = stmt.executeQuery(sql);

        if (rs.isBeforeFirst()) {
            return LocalDateTime.parse(rs.getString("sent_time"));
        }
        return null;
    }

    /**
     * Method to retrieve nutrition items from the database. It will return the first item where the start of
     * the item's name matches the input.
     *
     * @param itemName Takes in the a name to be searched in the database.
     *
     */
    public NutritionItem getNutritionItem(String itemName)  {
        if (itemName == null) {
            throw new NullPointerException();
        }

        String searchName = itemName + '%';
        String sql = "SELECT * FROM food WHERE name LIKE '" + searchName + "'";

        NutritionItem nutritionItem = null;

        try (Statement stmt  = this.conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)) {
            nutritionItem = new NutritionItem(
                    rs.getString("name"),          rs.getDouble("kcal"),
                    rs.getDouble("protein_g"),     rs.getDouble("fat_g"),
                    rs.getDouble("carbs_g"),       rs.getDouble("sugar_g"),
                    rs.getDouble("fibre_g"),       rs.getDouble("cholesterol_mg"),
                    rs.getDouble("sodium_mg"),     rs.getDouble("potassium_mg"),
                    rs.getDouble("calcium_mg"),    rs.getDouble("magnesium_mg"),
                    rs.getDouble("phosphorus_mg"), rs.getDouble("iron_mg"),
                    rs.getDouble("copper_mg"),     rs.getDouble("zinc_mg"),
                    rs.getDouble("chloride_mg"),   rs.getDouble("selenium_ug"),
                    rs.getDouble("iodine_ug"),     rs.getDouble("vit_a_ug"),
                    rs.getDouble("vit_d_ug"),      rs.getDouble("thiamin_mg"),
                    rs.getDouble("riboflavin_mg"), rs.getDouble("niacin_mg"),
                    rs.getDouble("vit_b6_mg"),     rs.getDouble("vit_b12_ug"),
                    rs.getDouble("folate_ug"),     rs.getDouble("vit_c_mg")
            );
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return nutritionItem;
    }

    /**
     * Method to get a NutritionItem based on a foodID.
     *
     * @param foodID the NutritionItem's unique ID.
     * @return a NutritionItem represented by the passed ID.
     */
    public NutritionItem getNutritionItem(int foodID)  {
        if (foodID < 0) {
            throw new IllegalArgumentException();
        }
        String sql = "SELECT * FROM food WHERE id = " + foodID;

        NutritionItem nutritionItem = null;

        try (Statement stmt  = this.conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)) {
            nutritionItem = new NutritionItem(
                    rs.getString("name"),          rs.getDouble("kcal"),
                    rs.getDouble("protein_g"),     rs.getDouble("fat_g"),
                    rs.getDouble("carbs_g"),       rs.getDouble("sugar_g"),
                    rs.getDouble("fibre_g"),       rs.getDouble("cholesterol_mg"),
                    rs.getDouble("sodium_mg"),     rs.getDouble("potassium_mg"),
                    rs.getDouble("calcium_mg"),    rs.getDouble("magnesium_mg"),
                    rs.getDouble("phosphorus_mg"), rs.getDouble("iron_mg"),
                    rs.getDouble("copper_mg"),     rs.getDouble("zinc_mg"),
                    rs.getDouble("chloride_mg"),   rs.getDouble("selenium_ug"),
                    rs.getDouble("iodine_ug"),     rs.getDouble("vit_a_ug"),
                    rs.getDouble("vit_d_ug"),      rs.getDouble("thiamin_mg"),
                    rs.getDouble("riboflavin_mg"), rs.getDouble("niacin_mg"),
                    rs.getDouble("vit_b6_mg"),     rs.getDouble("vit_b12_ug"),
                    rs.getDouble("folate_ug"),     rs.getDouble("vit_c_mg")
            );
        }
        catch (SQLException e) {
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
    public ExerciseItem getExerciseItem(String itemName) {
        if (itemName == null) {
            throw new NullPointerException();
        }

        String searchName = itemName + '%';
        String sql = "SELECT * FROM exercise WHERE name LIKE '" + searchName + "'";

        ExerciseItem exerciseItem = null;

        try (Statement stmt  = this.conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)) {

            exerciseItem = new ExerciseItem(rs.getString("name"),
                                            rs.getInt("burn_rate"));

        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return exerciseItem;
    }

    /**
     * Method to delete tokens from the regtokens table, this occurs when a token has been used to ensure we minimize
     * database bloat
     *
     * @param token takes in a token value
     */
    public void deleteToken(String token) {
        if (token == null) {
            throw new NullPointerException();
        }

        String sql = "DELETE FROM regTokens WHERE tokenVal = '" + token + "'";

        try {
            Statement stmt  = this.conn.createStatement();
            stmt.executeQuery(sql);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to dynamically update a SQL table, it takes in 4 different parameters which allows this method to modify
     * any table that has a username as a unique key
     *
     * @param table This specifies the table this method will be editing
     * @param column This specifies the column in the above table this method will be editing
     * @param valToUpdateTo This specifies the value that this method will use to overwrite existing data with
     * @param identifyingColumn This specifies the column by which the record/s to edit will be found
     * @param identifyingValue This specifies the value by which the record/s to edit will be found
     */
    public void editValue(
            String table, String column, String valToUpdateTo, String identifyingColumn, String identifyingValue
    ) throws SQLException {
        if (table == null) {
            throw new NullPointerException("Table cannot be null");
        }
        if (column == null) {
            throw new NullPointerException("Column cannot be null");
        }
        if (valToUpdateTo == null) {
            throw new NullPointerException("Value to update to cannot be null");
        }
        if (identifyingColumn == null) {
            throw new NullPointerException("Identifying column cannot be null");
        }
        if (identifyingValue == null) {
            throw new NullPointerException("Identifying value cannoy be null");
        }

        String sql = "UPDATE " + table.toUpperCase(Locale.ROOT) +
                     " SET " + column.toUpperCase(Locale.ROOT) + " = '" + valToUpdateTo +
                     "' WHERE " + identifyingColumn + " = '" + identifyingValue + "'";

        Statement stmt  = this.conn.createStatement();
        stmt.executeUpdate(sql);
    }

    /**
     * Method to dynamically update a SQL table, it takes in 4 different parameters which allows this method to modify
     * any table that has a username as a unique key, overloaded to accept an integer value in valToUpdate
     *
     * @param table This specifies the table this method will be editing
     * @param column This specifies the column in the above table this method will be editing
     * @param valToUpdateTo This specifies the value that this method will use to overwrite existing data with
     * @param identifyingColumn This specifies the column by which the record/s to edit will be found
     * @param identifyingValue This specifies the value by which the record/s to edit will be found
     */
    public void editValue(
            String table, String column, int valToUpdateTo, String identifyingColumn, String identifyingValue)
            throws SQLException {
        String strValToUpdateTo = Integer.toString(valToUpdateTo);

        editValue(table, column, strValToUpdateTo, identifyingColumn, identifyingValue);
    }

    /**
     * Method to build a user object based on the username provided
     *
     * @param username username to query the database for
     * @return returns a complete user object for use as a persistent user throughout application
     */
    public User createUserObjectFromUsername(String username) throws SQLException {
        if (username == null) {
            throw new NullPointerException();
        }

        String sql = "SELECT first_name, last_name, sex, dob, email, username " +
                     "FROM user WHERE username = '" + username + "'";

        User user;

        try (Statement stmt  = this.conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)) {

            user = new User(rs.getString("first_name"),
                    rs.getString("last_name"),
                    User.Sex.valueOf(rs.getString("sex").toUpperCase(Locale.ROOT)),
                    LocalDate.parse(rs.getString("dob")),
                    rs.getString("email"),
                    rs.getString("username")
            );
        }

        return user;
    }

    /**
     * Method to get the past seven days of caloric intake for the user in a Map with keys as String representations
     * of each day's date.
     *
     * @param username The user's username.
     * @param latest the lastest date to be queried.
     * @return A Map of String representation of a date against calories intaken.
     */
    public HashMap<String, Double> getIntakeEntries(String username, LocalDate latest) {
        if (username == null) {
            throw new NullPointerException();
        }
        if (latest == null) {
            throw new NullPointerException();
        }

        HashMap<String, Double> entries = new HashMap<>();
        LocalDate prevWeek = latest.minusDays(6);

        String sql = "SELECT date_of, quantity, kcal FROM meal INNER JOIN food ON meal.food_id = food.id " +
                     "WHERE user_id = '" + getUserIDFromUsername(username) + "' AND date_of BETWEEN '" +
                      prevWeek.toString() + "' AND '" + latest.toString() + "'";

        try (Statement stmt  = this.conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String date = rs.getString("date_of");
                int quantity = rs.getInt("quantity");
                double kcal = rs.getDouble("kcal");

                if (entries.containsKey(date)) {
                    entries.put(date, entries.get(date) + (quantity * kcal/100));
                }
                else {
                    entries.put(date, (quantity * kcal)/100);
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return entries;
    }

    /**
     * Method to get the past seven days of minutes spent exercising for the user in a Map with keys as String
     * representations of each day's date.
     *
     * @param username The user's username.
     * @param latest the lastest date to be queried.
     * @return A Map of String representation of a date against minutes spent exercising.
     */
    public HashMap<String, Integer> getSpentEntries(String username, LocalDate latest) {
        if (username == null) {
            throw new NullPointerException();
        }
        if (latest == null) {
            throw new NullPointerException();
        }

        HashMap<String, Integer> entries = new HashMap<>();
        LocalDate prevWeek = latest.minusDays(6);

        String sql = "SELECT date_of, duration FROM activity WHERE user_id = '" +
                      getUserIDFromUsername(username) + "' AND date_of BETWEEN '" + prevWeek.toString() +
                     "' AND '" + latest.toString() + "'";

        try (Statement stmt  = this.conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String date = rs.getString("date_of");

                if (entries.containsKey(date)) {
                    entries.put(date, entries.get(date) + rs.getInt("duration"));
                }
                else {
                    entries.put(date, rs.getInt("duration"));
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return entries;
    }

    /**
     * Method to get the past seven days of calories burned for the user in a Map with keys as String representations
     * of each day's date.
     *
     * @param username The user's username.
     * @param latest the lastest date to be queried.
     * @return A Map of String representation of a date against calories burned.
     */
    public HashMap<String, Float> getBurnedEntries(String username, LocalDate latest) {
        if (username == null) {
            throw new NullPointerException();
        }
        if (latest != null && latest.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException();
        }

        HashMap<String, Float> entries = new HashMap<>();

        if(latest != null) {

            LocalDate prevWeek = latest.minusDays(6);

            String sql = "SELECT date_of, duration, burn_rate FROM activity INNER JOIN exercise " +
                    "ON activity.exercise_id = exercise.id WHERE user_id = '" + getUserIDFromUsername(username) +
                    "' AND date_of BETWEEN '" + prevWeek.toString() + "' AND '" + latest.toString() + "'";

            try (Statement stmt = this.conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    String date = rs.getString("date_of");
                    float burnRate = rs.getFloat("burn_rate");

                    if (entries.containsKey(date)) {
                        entries.put(date, entries.get(date) + rs.getInt("duration") * burnRate / 30);
                    } else {
                        entries.put(date, rs.getInt("duration") * burnRate / 30);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } else {

            String sql = "SELECT date_of, duration, burn_rate, name FROM activity INNER JOIN exercise " +
                    "ON activity.exercise_id = exercise.id WHERE user_id = '" + getUserIDFromUsername(username) + "'";

            try (Statement stmt = this.conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    String date = rs.getString("date_of");
                    float burnRate = rs.getFloat("burn_rate");
                    String name = rs.getString("name");

                    if (entries.containsKey(date)) {
                        entries.put(date, entries.get(date) + rs.getInt("duration") * burnRate / 30);
                    } else {
                        entries.put(date, rs.getInt("duration") * burnRate / 30);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        return entries;
    }

    /**
     * Method to get the past seven days of calories burned for the user in a Map with keys as String representations
     * of each day's date.
     *
     * @param username The user's username.
     * @return A Map of String representation of a date against calories burned.
     */
    public HashMap<String, List<List<String>>> getBurnedEntries(String username) {
        if (username == null) {
            throw new NullPointerException();
        }

        HashMap<String, List<List<String>>> entries = new HashMap<>();

        String sql = "SELECT date_of, duration, burn_rate, name FROM activity INNER JOIN exercise " +
                "ON activity.exercise_id = exercise.id WHERE user_id = '" + getUserIDFromUsername(username) + "'";

        try (Statement stmt = this.conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String date = rs.getString("date_of");
                float burnRate = rs.getFloat("burn_rate");
                String name = rs.getString("name");
                int duartion = rs.getInt("duration");

                if (entries.containsKey(date)) {
                    List<List<String>> list = entries.get(date);

                    List<String> entry = new ArrayList<>();

                    entry.add(name);

                    entry.add(String.valueOf(duartion));

                    entry.add(String.valueOf(duartion * burnRate / 30));

                    list.add(entry);

                    entries.put(date, list);

                } else {
                    List<List<String>> list = new ArrayList<>();

                    List<String> entry = new ArrayList<>();

                    entry.add(name);

                    entry.add(String.valueOf(duartion));

                    entry.add(String.valueOf(duartion * burnRate / 30));

                    list.add(entry);

                    entries.put(date, list);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return entries;

    }

    /**
     * Method to get the past seven days of weight entries for the user in a Map with keys as String representations
     * of each day's date.
     *
     * @param username The user's username.
     * @param latest the lastest date to be queried.
     * @return A Map of String representation of a date against weight entries.
     */
    public HashMap<String, Integer> getWeightEntries(String username, LocalDate latest) {
        if (username == null) {
            throw new NullPointerException();
        }
        if (latest != null && latest.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException();
        }

        HashMap<String, Integer> entries = new HashMap<>();

        if(latest != null) {

            LocalDate prevWeek = latest.minusDays(6);

            String sql = "SELECT date_of, weight FROM weight_entry WHERE user_id = '" +
                    getUserIDFromUsername(username) + "' AND date_of BETWEEN '" + prevWeek.toString() +
                    "' AND '" + latest.toString() + "'";

            try (Statement stmt = this.conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    String date = rs.getString("date_of");

                    entries.put(date, rs.getInt("weight"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } else {

            String sql = "SELECT date_of, weight FROM weight_entry WHERE user_id = '" +
                    getUserIDFromUsername(username) + "'";

            try (Statement stmt = this.conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    String date = rs.getString("date_of");

                    entries.put(date, rs.getInt("weight"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        return entries;
    }

    /**
     * Method to get a user's food entries from between today and a given passed date.
     *
     * @param username the user's username.
     * @param latest the earliest date to be queried.
     * @return a HashMap of String dates of consumption against NutritionItems.
     */
    public HashMap<String, ArrayList<NutritionItem>> getNutrientEntries(String username, LocalDate latest){
        if (username == null) {
            throw new NullPointerException();
        }
        if (latest != null && latest.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException();
        }

        HashMap<String, ArrayList<NutritionItem>> result = new HashMap<>();
        HashMap<String, Integer> entries = new HashMap<>();

        if(latest != null) {
            LocalDate prevWeek = latest.minusDays(6);

            String sql = "SELECT date_of, food_id FROM meal WHERE user_id = '" +
                    getUserIDFromUsername(username) + "' AND date_of BETWEEN '" + prevWeek.toString() +
                    "' AND '" + latest.toString() + "'";

            try (Statement stmt = this.conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    String date = rs.getString("date_of");

                    entries.put(date, rs.getInt("food_id"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            for (String date : entries.keySet()) {
                if (result.containsKey(date)) {
                    ArrayList<NutritionItem> items = result.get(date);
                    items.add(getNutritionItem(entries.get(date)));
                    result.put(date, items);
                } else {
                    ArrayList<NutritionItem> item = new ArrayList<>();
                    item.add(getNutritionItem(entries.get(date)));
                    result.put(date, item);
                }
            }

        } else {

            String sql = "SELECT date_of, food_id FROM meal WHERE user_id = '" +
                    getUserIDFromUsername(username) + "'";

            try (Statement stmt = this.conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    String date = rs.getString("date_of");

                    entries.put(date, rs.getInt("food_id"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            for (String date : entries.keySet()) {
                if (result.containsKey(date)) {
                    ArrayList<NutritionItem> items = result.get(date);
                    items.add(getNutritionItem(entries.get(date)));
                    result.put(date, items);
                } else {
                    ArrayList<NutritionItem> item = new ArrayList<>();
                    item.add(getNutritionItem(entries.get(date)));
                    result.put(date, item);
                }
            }

        }

        return result;
    }

    /**
     * Method to get a list of all exercise names in the database.
     *
     * @return an ArrayList containing every exercise name as a String.
     */
    public ArrayList<String> getExerciseNames() {
        ArrayList<String> exercises = new ArrayList<>();

        String sql = "SELECT name FROM exercise";

        try (Statement stmt  = this.conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)) {
            while (rs.next()) {
                exercises.add(rs.getString("name"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return exercises;
    }

    /**
     * A method to get a list of all food names in the database.
     *
     * @return an ArrayList containing every food name as a String.
     */
    public ArrayList<String> getFoodNames() {
        ArrayList<String> foods = new ArrayList<>();

        String sql = "SELECT name FROM food";

        try (Statement stmt  = this.conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)) {
            while (rs.next()) {
                foods.add(rs.getString("name"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return foods;
    }

    /**
     * Method to get an exercise_id for an exercise given by name.
     *
     * @param name the name of the exercise.
     * @return the exercise_id.
     */
    public int getExerciseId(String name)  {
        if (name == null) {
            throw new NullPointerException();
        }

        String sql = "SELECT id FROM exercise WHERE name LIKE '" + name + "'";
        int exerciseId = -1;

        try (Statement stmt  = this.conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)) {
            while (rs.next()) {
                exerciseId = rs.getInt("id");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return exerciseId;
    }

    /**
     * Method to insert an exercise entry for a user into the activity table.
     *
     * @param username the user's username.
     * @param exercise the exercise undertaken.
     * @param duration the duration undertaken for.
     * @throws SQLException when a database access error occurs.
     */
    public void insertExercise(String username, String exercise, int duration) throws SQLException {
        if (username == null) {
            throw new NullPointerException();
        }
        if (exercise == null) {
            throw new NullPointerException();
        }
        if (duration < 1) {
            throw new IllegalArgumentException();
        }

        int exerciseId = getExerciseId(exercise);

        if (exerciseId == -1) {
            throw new IllegalStateException();
        }

        String sql = "INSERT INTO activity (exercise_id, user_id, duration, date_of)" +
                     "VALUES('" + exerciseId  + "', '" + getUserIDFromUsername(username) +
                     "','" + duration + "','" + LocalDate.now().toString() + "')";

        Statement stmt  = conn.createStatement();
        stmt.executeUpdate(sql);
    }

    /**
     * Method to get an food_id for a food given by name.
     *
     * @param name the name of the food.
     * @return the food_id.
     */
    public int getFoodId(String name)  {
        if (name == null) {
            throw new NullPointerException();
        }

        String sql = "SELECT id FROM food WHERE name LIKE '" + name + "'";
        int foodId = -1;

        try (Statement stmt  = this.conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)) {

            foodId = rs.getInt("id");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return foodId;
    }

    /**
     * Method to insert a meal entry into the meal table for a user.
     *
     * @param username the user's username.
     * @param meal     the meal at which this food was consumed.
     * @param food     the food consumed.
     * @param quantity the quantity consumed.
     * @param date     the date of consumption.
     */
    public void addFoodEntry(String username, String meal, String food, int quantity, LocalDate date)
    throws SQLException {
        if (username == null) {
            throw new NullPointerException();
        }
        if (meal == null) {
            throw new NullPointerException();
        }
        if (food == null) {
            throw new NullPointerException();
        }
        if (quantity < 1) {
            throw new IllegalArgumentException();
        }
        if (date == null) {
            throw new NullPointerException();
        }
        if (date.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException();
        }

        String sql = "INSERT INTO meal (meal_category, food_id, user_id, date_of, quantity)" +
                     "VALUES('" + meal  + "', '" + getFoodId(food) + "','" + getUserIDFromUsername(username) +
                     "','" + date.toString() + "','" + quantity + "')";

        Statement stmt  = conn.createStatement();
        stmt.executeUpdate(sql);
    }

    /**
     * Adds a new goal to the database for a user.
     *
     * @param username the user's username.
     * @param goal the user's new goal.
     */
    public void insertGoal(String username, UserGoal goal) {
        if (username == null) {
            throw new NullPointerException();
        }
        if (goal == null) {
            throw new NullPointerException();
        }

        int groupId = 0;

        if (goal instanceof GroupGoal) {
            groupId = ((GroupGoal)goal).getGroupId();
        }

        String sql = "INSERT INTO goal (user_id, target, unit, progress, end_date, group_id) VALUES('" +
                getUserIDFromUsername(username) + "','" + goal.getTarget() + "','" + goal.getUnit().toString() +
                "','" + goal.getProgress() + "','" + goal.getEndDate().toString() + "', '" + groupId + "')";

        try {
            Statement stmt = this.conn.createStatement();
            stmt.executeUpdate(sql);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves all of a user's goals from the database.
     *
     * @param username the user's username.
     * @return the user's goals in an ArrayList.
     */
    public ArrayList<UserGoal> selectGoals(String username) {
        if (username == null) {
            throw new NullPointerException();
        }

        String sql = "SELECT target, unit, progress, end_date, group_id FROM goal WHERE user_id = " +
                getUserIDFromUsername(username);

        ArrayList<UserGoal> goals = new ArrayList<>();

        try (Statement stmt = this.conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                float target = rs.getFloat("target");
                Goal.Unit unit = Goal.Unit.valueOf(rs.getString("unit"));
                float progress = rs.getInt("progress");
                LocalDate endDate = LocalDate.parse(rs.getString("end_date"));
                int group_id = rs.getInt("group_id");

                if (group_id == 0) {
                    goals.add(new IndividualGoal(target, unit, endDate, progress));
                }
                else {
                    goals.add(new GroupGoal(target, unit, endDate, progress, group_id));
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return goals;
    }

    /**
     * Updates a user's goal in the database.
     *
     * @param username the user's username.
     * @param goal the updated goal to be updated in the database.
     */
    public void updateGoal(String username, UserGoal goal, float amount) {
        if (username == null) {
            throw new NullPointerException();
        }
        if (goal == null) {
            throw new NullPointerException();
        }
        if (amount < 0) {
            throw new IllegalArgumentException();
        }

        float target = goal.getTarget();
        String unit = goal.getUnit().toString();
        String endDate = goal.getEndDate().toString();
        float newProgress = goal.getProgress();
        float previousProgress = newProgress - amount;

        String sql = "UPDATE goal SET progress = " + newProgress + " WHERE user_id = '" +
                getUserIDFromUsername(username) + "' AND target = '" + target + "' AND unit = '" + unit +
                "' AND progress = '" + previousProgress + "' AND end_date = '" + endDate + "'";

        try {
            Statement stmt = this.conn.createStatement();
            stmt.executeUpdate(sql);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a token to the groupInvTable
     *
     * @param username the user's username.
     */
    public void addInvToken(String tokenVal, String groupName, String username) {
        if (tokenVal == null) {
            throw new NullPointerException();
        }
        if (groupName == null) {
            throw new NullPointerException();
        }
        if (username == null) {
            throw new NullPointerException();
        }

        //Get userID from the username
        int userID = getUserIDFromUsername(username);
        int groupID = getGroupIDFromName(groupName);

        //Get the time 36 hours after token creation
        LocalDateTime time = LocalDateTime.now();
        time = time.plusHours(36);

        String sql = "INSERT INTO groupInvTable (tokenVal, expiry_time, groupID, userID) VALUES ('" + tokenVal +"', '" + time + "', " + groupID + ", " + userID + ");";
        try {
            Statement stmt = this.conn.createStatement();
            stmt.executeUpdate(sql);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Method to retrieve water intake for user on a certain day, if no entry is found method returns 0
     *
     * @param username user's username
     * @param date the date in which how much water intake has been recorded
     * @return number of cups of water (250ml) consumed on that day
     */
    public int getWaterIntakeInCups(String username, LocalDate date) {
        if (username == null) {
            throw new NullPointerException();
        }
        if (date == null) {
            throw new NullPointerException();
        }
        if (date.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException();
        }

        int userID = getUserIDFromUsername(username);
        int noCups = 0;

        String sql = "SELECT quantity FROM meal WHERE user_id = '" + userID + "' AND date_of = '" + date +
                "' AND food_id = " + getFoodId("Water");

        try (Statement stmt = this.conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if(rs.next()) {
                noCups = rs.getInt("quantity") / 250;

            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return noCups;
    }

    /**
     * Method to get the role of a user in a group
     *
     * @param group Takes the group that we are checking the user against
     * @param username Takes the user we are checking the role of
     *
     * @return Returns a String representing the role of the user
     */
    public String getGroupRoleFromUsername(String group, String username){
        if (group == null) {
            throw new NullPointerException();
        }
        if (username == null) {
            throw new NullPointerException();
        }

        int groupID = getGroupIDFromName(group);
        int userID = getUserIDFromUsername(username);
        String role = null;

        String sql = "SELECT Group_Role FROM group_membership WHERE User_Id = " + userID + " AND Group_Id = " + groupID;

        try (Statement stmt = this.conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql))
        {
            while (rs.next()) {
                role = rs.getString("Group_Role");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return role;
    }

    /**
     * Method to set the amount of water intake on a given day and store it into the meal table.
     * It firstly checks if the user ID exists, then checks if there is already an entry into the database for the given
     * day, if there is it will update the record and if there isn't, a new row will be inserted.
     *
     * @param username user's username
     * @param date the date in which how much water intake has been recorded
     * @param noCups number of cups of water (250ml) consumed on that day
     */
    public void setWaterIntake(String username, LocalDate date, int noCups) {
        if (username == null) {
            throw new NullPointerException();
        }
        if (date == null) {
            throw new NullPointerException();
        }
        if (date.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException();
        }
        if (noCups < 0) {
            throw new IllegalArgumentException();
        }

        int userID = getUserIDFromUsername(username);

        String check_if_entry_exists_sql = "SELECT COUNT(*) FROM meal WHERE user_id = '" + userID + "' AND date_of = '" + date + "'";

        try (Statement stmt = this.conn.createStatement();
             ResultSet rs = stmt.executeQuery(check_if_entry_exists_sql)) {

            int waterQuantity = noCups * 250;
            String sql;

            if (rs.getInt(1) != 0) {
                sql = "UPDATE meal SET quantity = " + waterQuantity + " WHERE user_id = '" + userID +
                        "' AND date_of = '" + date + "'";
            }
            else {
                sql = "INSERT INTO meal (meal_category, food_id, user_id, date_of, quantity)" +
                        "VALUES('Water', '" + getFoodId("Water") + "','" + userID + "','" + date.toString() + "','" +
                        waterQuantity + "')";
            }

            stmt.executeUpdate(sql);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to add a nutrition item to the food database, this will be used for custom nutrition item creation,
     * everything here is measured in terms of 1 gram due to how meals are created
     *
     * @param n Nutrition Item passed in, will use this to build the SQL query
     *
     * @throws SQLException Throws an SQLException whenever it is possible that an external error could interrupt
     *                      the running of an SQL statement
     */
    public void addNutritionItem(NutritionItem n) throws SQLException {
        if (n == null) {
            throw new NullPointerException();
        }

        String sql = "INSERT INTO food (name, kcal, protein_g, fat_g, carbs_g, sugar_g, fibre_g, cholesterol_mg, " +
                "sodium_mg, potassium_mg, calcium_mg, magnesium_mg, phosphorus_mg, iron_mg, copper_mg, zinc_mg, " +
                "chloride_mg, selenium_ug, iodine_ug, vit_a_ug, vit_d_ug, thiamin_mg, riboflavin_mg, niacin_mg, " +
                "vit_b6_mg, vit_b12_ug, folate_ug, vit_c_mg)" + "VALUES('" +
                n.getName()         + "', " + n.getKcal()          + ", " + n.getProteinG()     + ", " +
                n.getFatG()         + ", "  + n.getCarbsG()        + ", " + n.getSugarG()       + ", " +
                n.getFibreG()       + ", "  + n.getCholesterolMg() + ", " + n.getSodiumMg()     + ", " +
                n.getPotassiumMg()  + ", "  + n.getCalciumMg()     + ", " + n.getMagnesiumMg()  + ", " +
                n.getPhosphorusMg() + ", "  + n.getIronMg()        + ", " + n.getCopperMg()     + ", " +
                n.getZincMg()       + ", "  + n.getChlorideMg()    + ", " + n.getSeleniumUg()   + ", " +
                n.getIodineUg()     + ", "  + n.getVitAUg()        + ", " + n.getVitDUg()       + ", " +
                n.getThiaminMg()    + ", "  + n.getRiboflavinMg()  + ", " + n.getNiacinMg()     + ", " +
                n.getVitB6Mg()      + ", "  + n.getVitB12Ug()      + ", " + n.getFolateUg()     + ", " +
                n.getVitCMg()       + ")";

        Statement stmt  = conn.createStatement();
        stmt.executeUpdate(sql);
    }

    /**
     * Method to add an exercise item to the exercise database, this will be used for custom exercise item creation
     *
     * @param name Name of the exercise being added
     * @param burnRate caloric burn over a period of 30 minutes
     */
    public void addExerciseItem(String name, int burnRate) throws SQLException {
        if (name == null) {
            throw new NullPointerException();
        }
        if (burnRate < 0) {
            throw new IllegalArgumentException();
        }

        String sql = "INSERT INTO exercise (name, burn_rate) VALUES('" + name  + "', " + burnRate + ")";

        Statement stmt  = conn.createStatement();
        stmt.executeUpdate(sql);
    }

    /**
     * Method to select all SystemGoals for a particular username, end date, and category from the database.
     *
     * @param username the username of the user concerned.
     * @param endDate the end date to search up to
     * @param category the category of goal to search for, e.g. Day to Day
     * @return an ArrayList of SystemGoals
     */
    public ArrayList<SystemGoal> selectSystemGoals(String username, LocalDate endDate, SystemGoal.Category category) {
        if (username == null) {
            throw new NullPointerException();
        }
        if (endDate == null) {
            throw new NullPointerException();
        }
        if (category == null) {
            throw new NullPointerException();
        }

        String sql = "SELECT target, unit, end_date, update_period, category, accepted FROM system_goal WHERE " +
                "user_id = '" + getUserIDFromUsername(username) + "' AND end_date = '" + endDate +
                "' AND category = '" + category + "'";

        return getSystemGoals(sql);
    }

    /**
     * Method to select all SystemGoals for a particular username, end date, and update period, except those goals
     * with category day to day.
     *
     * @param username the username of the user concerned.
     * @param endDate the end date to search up to.
     * @return an ArrayList of SystemGoals.
     */
    public ArrayList<SystemGoal> selectDailyFitnessGoals(String username, LocalDate endDate) {
        if (username == null) {
            throw new NullPointerException();
        }
        if (endDate == null) {
            throw new NullPointerException();
        }

        String sql = "SELECT target, unit, end_date, update_period, category, accepted FROM system_goal WHERE " +
                "user_id = '" + getUserIDFromUsername(username) + "' AND end_date = '" + endDate +
                "' AND update_period = 'DAILY' AND category != 'DAY_TO_DAY'";

        return getSystemGoals(sql);
    }

    /**
     * Method to select all SystemGoals for a particular username, end date, and update period, except those goals
     * with category day to day.
     *
     * @param username the username of the user concerned.
     * @param endDate the end date to search up to.
     * @return an ArrayList of SystemGoals.
     */
    public ArrayList<SystemGoal> selectWeeklyFitnessGoals(String username, LocalDate endDate) {
        if (username == null) {
            throw new NullPointerException();
        }
        if (endDate == null) {
            throw new NullPointerException();
        }

        String sql = "SELECT target, unit, end_date, update_period, category, accepted FROM system_goal WHERE " +
                "user_id = '" + getUserIDFromUsername(username) + "' AND end_date <= '" + endDate +
                "'AND end_date > '" + endDate.minusDays(7) + "' AND update_period = 'WEEKLY' AND category != 'DAY_TO_DAY'";

        return getSystemGoals(sql);
    }

    /**
     * Private helper method to run various SQL statements for grabbing SystemGoals.
     *
     * @param sql the statement to run.
     * @return an ArrayList containing a User's SystemGoals meeting the required SQL statement.
     */
    private ArrayList<SystemGoal> getSystemGoals(String sql) {
        if (sql == null) {
            throw new NullPointerException();
        }

        ArrayList<SystemGoal> goals = new ArrayList<>();

        try (Statement stmt = this.conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                float target = rs.getFloat("target");
                Goal.Unit unit = Goal.Unit.valueOf(rs.getString("unit"));
                LocalDate date = LocalDate.parse(rs.getString("end_date"));
                SystemGoal.UpdatePeriod updatePeriod = SystemGoal.UpdatePeriod.valueOf(rs.getString("update_period"));
                SystemGoal.Category cat = SystemGoal.Category.valueOf(rs.getString("category"));
                boolean accepted = rs.getBoolean("accepted");

                goals.add(new SystemGoal(target, unit, date, updatePeriod, cat, accepted));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return goals;
    }

    /**
     * Retrieves the recommended daily intake for a given unit, sex, and age.
     *
     * @param unit the unit of the target, e.g. protein
     * @param sex the sex to query, male or female
     * @param age the age to query
     * @return a float representing the intake target for this user
     */
    public float getRecommendedIntake(Goal.Unit unit, int age, String sex) {
        if (unit == null) {
            throw new NullPointerException();
        }
        if (age < 0) {
            throw new IllegalArgumentException();
        }
        if (sex == null) {
            throw new NullPointerException();
        }

        String sql = "SELECT amount FROM daily_intake WHERE unit = '" + unit + "' AND gender = '" + sex +
                "' AND min_age <= '" + age + "' AND max_age >= '" + age + "'";

        float amount = -1;

        try (Statement stmt = this.conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                amount = rs.getFloat("amount");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return amount;
    }

    /**
     * Method to get a goal of each fitness unit completed by a user with the maximum target value within a given
     * date range.
     *
     * @param username the username of the user
     * @param earliest the earliest date to query
     * @return an ArrayList of SystemGoals representing the max achieved targets for their respective units.
     */
    public ArrayList<Goal.Unit> selectCompletedGoals(String username, LocalDate earliest) {
        if (username == null) {
            throw new NullPointerException();
        }
        if (earliest == null) {
            throw new NullPointerException();
        }

        ArrayList<Goal.Unit> completedGoals = new ArrayList<>();

        String sql = "SELECT DISTINCT unit FROM goal WHERE user_id = '" +
                getUserIDFromUsername(username)  + "' AND end_date >= '" + earliest + "' AND progress >= target " +
                "GROUP BY unit HAVING unit NOT IN ('" +
                Goal.Unit.CALORIES.toString() +    "', '" + Goal.Unit.PROTEIN.toString() +    "', '" +
                Goal.Unit.BURNED.toString() +      "', '" + Goal.Unit.CARBS.toString() +      "', '" +
                Goal.Unit.FIBRE.toString() +       "', '" + Goal.Unit.SODIUM.toString() +     "', '" +
                Goal.Unit.POTASSIUM.toString() +   "', '" + Goal.Unit.CALCIUM.toString() +    "', '" +
                Goal.Unit.MAGNESIUM.toString() +   "', '" + Goal.Unit.PHOSPHORUS.toString() + "', '" +
                Goal.Unit.IRON.toString() +        "', '" + Goal.Unit.COPPER.toString() +     "', '" +
                Goal.Unit.ZINC.toString() +        "', '" + Goal.Unit.CHLORIDE.toString() +   "', '" +
                Goal.Unit.SELENIUM.toString() +    "', '" + Goal.Unit.IODINE.toString() +     "', '" +
                Goal.Unit.VITAMIN_A.toString() +   "', '" + Goal.Unit.VITAMIN_D.toString() +  "', '" +
                Goal.Unit.THIAMIN.toString() +     "', '" + Goal.Unit.RIBOFLAVIN.toString() + "', '" +
                Goal.Unit.NIACIN.toString() +      "', '" + Goal.Unit.VITAMIN_B6.toString() + "', '" +
                Goal.Unit.VITAMIN_B12.toString() + "', '" + Goal.Unit.FOLATE.toString() +     "', '" +
                Goal.Unit.VITAMIN_C.toString() +   "') ";



        try (Statement stmt = this.conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Goal.Unit unit = Goal.Unit.valueOf(rs.getString("unit"));

                completedGoals.add(unit);
            }

        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return completedGoals;
    }

    /**
     * Method to get a User's email from their username.
     *
     * @param username the User's username.
     * @return the User's email.
     */
    public String getEmailFromUsername(String username){
        if (username == null) {
            throw new NullPointerException();
        }
        String email = null;

        String sql = "SELECT email FROM user WHERE username = '" + username + "'";

        try (Statement stmt = this.conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                email = rs.getString("email");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return email;
    }

    /**
     * Method to calculate a user's average work rate in a particular unit over a given time period.
     *
     * @param username the username of the user.
     * @param unit the unit to be queried
     * @param daysEarlier the earliest date to be queried
     * @return a float representing the user's average work rate in the unit over the date range
     */
    public float selectAverageWorkRate(String username, Goal.Unit unit, int daysEarlier) {
        if (username == null) {
            throw new NullPointerException();
        }
        if (unit == null) {
            throw new NullPointerException();
        }
        if (daysEarlier < 0) {
            throw new IllegalArgumentException();
        }

        String sql = "SELECT SUM(progress) as progress FROM goal WHERE user_id = '" + getUserIDFromUsername(username) +
                "' AND unit = '" + unit + "' AND end_date >= '" + LocalDate.now().minusDays(daysEarlier) +
                "' GROUP BY unit";

        float workRate = -1;

        try (Statement stmt = this.conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                workRate = rs.getFloat("progress");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return workRate/daysEarlier;
    }

    /**
     * A method to refresh a user's SystemGoals in the database. Intended to be called every time they are updated such
     * that the database maintains parity with the application. Deletes the goals currently stored and then Inserts
     * the passed ArrayList.
     *
     * @param username the user's username
     * @param systemGoals the user's SystemGoals
     */
    public void refreshSystemGoals(String username, ArrayList<SystemGoal> systemGoals) {
        if (username == null) {
            throw new NullPointerException();
        }
        if (systemGoals == null) {
            throw new NullPointerException();
        }

        String sqlDel = "DELETE FROM system_goal WHERE user_id = '" + getUserIDFromUsername(username) + "'";

        try {
            Statement stmt = this.conn.createStatement();
            stmt.executeUpdate(sqlDel);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        for (SystemGoal systemGoal : systemGoals) {
            String sqlIns = "INSERT INTO system_goal(target, unit, end_date, update_period, user_id, category, accepted)" +
                    "VALUES(?,?,?,?,?,?,?)";

            try (PreparedStatement pstmt = conn.prepareStatement(sqlIns)){
                pstmt.setFloat(1,systemGoal.getTarget());
                pstmt.setString(2, systemGoal.getUnit().toString());
                pstmt.setString(3, systemGoal.getEndDate().toString());
                pstmt.setString(4, systemGoal.getUpdatePeriod().toString());
                pstmt.setInt(5, getUserIDFromUsername(username));
                pstmt.setString(6, systemGoal.getCategory().toString());
                pstmt.setBoolean(7, systemGoal.isAccepted());

                pstmt.executeUpdate();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * Method to update the end date of a given goal for a given user in the database to today's date.
     *
     * @param username the user's username.
     * @param goal the goal to updated.
     */
    public void quitGoalInDatabase(String username, UserGoal goal) {
        if (username == null) {
            throw new NullPointerException();
        }
        if (goal == null) {
            throw new NullPointerException();
        }

        String sql = "UPDATE goal SET end_date = '" + LocalDate.now().toString() + "' WHERE user_id = '" +
                getUserIDFromUsername(username) + "' AND target = '" + goal.getTarget() + "' AND unit = '" +
                goal.getUnit() + "' AND progress = '" + goal.getProgress() + "' AND end_date = '" +
                goal.getEndDate().toString() + "'";

        try {
            Statement stmt = this.conn.createStatement();
            stmt.executeUpdate(sql);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to find the username of a user based on their email address. This is used in password recovery.
     *
     * @param email Takes in an email address.
     *
     * @return returns a string of the username linked to the given email.
     */
    public String getUsernameFromEmail(String email) {
        if (email == null) {
            throw new NullPointerException();
        }

        String username = "Could not find Username";

        String sql = "SELECT username FROM user WHERE email = '" + email + "'";

        try (Statement stmt  = this.conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)) {
            while (rs.next()) {
                username = rs.getString("username");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return username;
    }

    /**
     * Inserts a recovery code for a forgotten password into the database with a 30 min expiry time.
     * @param userId ID of the user the code is for.
     * @param token The unique token used for the recovery code.
     */
    public void insertRecoveryCode (int userId, String token) {
        if (userId < 0) {
            throw new IllegalArgumentException();
        }
        if (token == null) {
            throw new NullPointerException();
        }

        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime expiryTime = currentTime.plusMinutes(30);//set time for 30 minutes from now
        String expiryTimeString = expiryTime.toString();

        String sql = "INSERT INTO passwordRecoveryCodes(userID, recoveryCode, expiryTime) VALUES('" +
                userId + "','" + token + "','" + expiryTimeString + "')";
        try {
            Statement stmt = this.conn.createStatement();
            stmt.executeUpdate(sql);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to check if a recovery code is present and valid.
     *
     * @param token The unique token that is used as the recovery code.
     * @return Returns true if valid, else returns false.
     */
    public boolean checkRecoveryCode (String token) {
        if (token == null) {
            throw new NullPointerException();
        }

        String sql = "SELECT expiryTime FROM passwordRecoveryCodes WHERE " + "recoveryCode = '" + token + "'";
        try (Statement stmt = this.conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                String expiry_time_string = rs.getString("expiryTime");
                LocalDateTime expiryTime = LocalDateTime.parse(expiry_time_string);
                return LocalDateTime.now().isBefore(expiryTime);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Method to get the userID a recovery code was for.
     *
     * @param token The unique token that is used as the recovery code.
     * @return The userID associated with the token or -1 if there was an error retrieving the userID
     */
    public int getUserIDFromRecoveryCode (String token) {
        if (token == null) {
            throw new NullPointerException();
        }

        String sql = "SELECT userID FROM passwordRecoveryCodes WHERE " + "recoveryCode = '" + token + "'";
        try (Statement stmt = this.conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt("userID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Method to create a group in the group table.
     * @param name the name of the group.
     * @param username the username of the user creating the group.
     * @return a boolean value representing whether the group was successfully created.
     */
    public boolean createGroup(String name, String username){
        String checkIfExistsSQL = "SELECT COUNT(*) FROM group_table WHERE Group_Name = '" + name + "'";

        boolean groupNameTaken = true;

        try (Statement stmt = this.conn.createStatement();
             ResultSet rs = stmt.executeQuery(checkIfExistsSQL)) {

            if(rs.getInt(1) == 0) {
                groupNameTaken = false;
            }
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        if(!groupNameTaken){

            try (Statement stmt = this.conn.createStatement()) {

                stmt.executeUpdate("INSERT INTO group_table(Group_Name) VALUES('" + name + "')");

                ResultSet rs = stmt.executeQuery(
                        "SELECT Group_Id FROM group_table WHERE Group_Name = '" + name + "'"
                );

                int groupId = rs.getInt("Group_Id");

                stmt.executeUpdate(
                        "INSERT INTO group_membership(User_Id, Group_Id, Group_Role) VALUES(" +
                                getUserIDFromUsername(username) + "," + groupId + ", 'Owner')"
                );

                return true;
            }
            catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        return !groupNameTaken;
    }

    /**
     * Method to get the groupID based on the groupName
     *
     * @param groupName Name of the group we are finding the ID of
     *
     * @return Returns the GroupID of the group we are checking
     */
    public int getGroupIDFromName(String groupName) {
        if (groupName == null) {
            throw new NullPointerException();
        }

        String sql = "SELECT group_id " +
                     "FROM group_table " +
                     "WHERE group_name = '" + groupName + "';";

        int groupID = -1;

        try (Statement stmt = this.conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                groupID = rs.getInt("group_id");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return groupID;
    }

    /**
     * Method to change a group members group_role to member
     *
     * @param userName  Takes the username of the user having their role changed
     * @param groupName Takes the name of the group the change is taking place in
     */
    public void removeAdmin(String userName, String groupName){
        if (userName == null) {
            throw new NullPointerException();
        }
        if (groupName == null) {
            throw new NullPointerException();
        }

        int groupID = getGroupIDFromName(groupName);
        int userID = getUserIDFromUsername(userName);


        String sql = "UPDATE group_membership" +
                     " SET group_role = 'Member'" +
                     " WHERE user_id = " + userID +
                     " AND group_id = " + groupID + ";";

        try{
             Statement stmt = this.conn.createStatement();
             stmt.executeUpdate(sql);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to change a group members group_role to admin
     *
     * @param userName  Takes the username of the user having their role changed
     * @param groupName Takes the name of the group the change is taking place in
     */
    public void addAdmin(String userName, String groupName) {
        if (userName == null) {
            throw new NullPointerException();
        }
        if (groupName == null) {
            throw new NullPointerException();
        }

        int groupID = getGroupIDFromName(groupName);
        int userID = getUserIDFromUsername(userName);

        String sql = "UPDATE group_membership" +
                    " SET group_role = 'Admin'" +
                    " WHERE user_id = " + userID +
                    " AND group_id = " + groupID + ";";

        try {
            Statement stmt = this.conn.createStatement();
            stmt.executeUpdate(sql);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to add a user to a group
     *
     * @param userName  userName of the user who is joining a group
     * @param groupName group the user is intending to join
     */
    public void joinGroup(String userName, String groupName){
        if (userName == null) {
            throw new NullPointerException();
        }
        if (groupName == null) {
            throw new NullPointerException();
        }

        int groupID = getGroupIDFromName(groupName);
        int userID = getUserIDFromUsername(userName);


        String sql = "INSERT INTO group_membership (User_Id, Group_Id, Group_Role) " +
                    " VALUES (" + userID + ", " + groupID  + ", 'Member');";

        try{
            Statement stmt = this.conn.createStatement();
            stmt.executeUpdate(sql);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to fetch a groups name based on its ID
     *
     * @param groupId ID of the group we are fetching
     *
     * @return Returns a String value representing the name of the Group
     */
    public String getGroupNameFromID(int groupId) {
        if (groupId < 0) {
            throw new IllegalArgumentException();
        }

        String groupName = null;

        String sql = "SELECT Group_Name FROM group_table WHERE Group_Id = " + groupId;
        try (Statement stmt = this.conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                groupName = rs.getString("Group_Name");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return groupName;
    }

    /**
     * Method to return a Group object based on the group username
     *
     * @param groupID ID of the group we are fetching
     *
     * @return Returns a fully constructed group
     */
    public Group getGroupObjectFromGroupId(int groupID) {
        if (groupID < 1) {
            throw new IllegalArgumentException();
        }

        return getGroup(groupID);

    }

    /**
     * Private helper method for getting Group objects from the database.
     *
     * @param groupID the unique ID of the Group.
     * @return an instance of the Group.
     */
    private Group getGroup(int groupID) {
        if (groupID < 1) {
            throw new IllegalArgumentException();
        }

        GroupOwner owner = null;
        Set<GroupAdmin> admins = new HashSet<>();
        Set<GroupMember> members = new HashSet<>();
        Group group = null;

        try {
            group = new Group(getGroupNameFromID(groupID));
        }
        catch (NullPointerException e) {
            return group;
        }

        String sql = "SELECT User_id, Group_Role FROM group_membership WHERE Group_Id = " + groupID;
        try (Statement stmt = this.conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int user_id = rs.getInt("User_id");
                String role = rs.getString("Group_Role");

                switch (role) {
                    case "Owner" -> owner = new GroupOwner(group, createUserObjectFromUsername(getUsernameFromUserID(user_id)));
                    case "Admin" -> admins.add(new GroupAdmin(group, createUserObjectFromUsername(getUsernameFromUserID(user_id))));
                    case "Member" -> members.add(new GroupMember(group, createUserObjectFromUsername(getUsernameFromUserID(user_id))));
                }

                group.setOwner(owner);
                group.setAdmins(admins);
                group.setMembers(members);
            }
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return group;
    }

    /**
     * Method to retrieve all of a User's joined Groups from the database.
     *
     * @param username the User's username.
     * @return an ArrayList containing the User's Groups.
     */
    public ArrayList<Group> getUserGroups(String username) {
        if (username == null) {
            throw new NullPointerException();
        }

        ArrayList<Group> groups = new ArrayList<>();

        int userID = getUserIDFromUsername(username);

        String sql = "SELECT Group_Id FROM group_membership WHERE User_Id = " + userID;

        try (Statement stmt = this.conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                groups.add(getGroupObjectFromGroupId(rs.getInt("Group_Id")));
            }
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return groups;
    }

    /**
     * Method to load all GroupGoals for a given Group.
     *
     * @param name the name of the Group.
     * @return the Group's GroupGoals as an ArrayList.
     */
    public ArrayList<GroupGoal> loadGroupGoals(String name) {
        if (name == null) {
            throw new NullPointerException();
        }

        String sql = "SELECT group_id, target, unit, end_date FROM group_goal WHERE group_id = " +
                getGroupIDFromName(name);

        ArrayList<GroupGoal> goals = new ArrayList<>();

        try (Statement stmt = this.conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {

                int groupId = rs.getInt("group_id");
                float target = rs.getFloat("target");
                Goal.Unit unit = Goal.Unit.valueOf(rs.getString("unit"));
                LocalDate endDate = LocalDate.parse(rs.getString("end_date"));

                goals.add(new GroupGoal(target, unit, endDate, groupId));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return goals;
    }

    /**
     * Method to get the name of a group by a particular invite code.
     *
     * @param tokenInput the code to be queried.
     * @return the name of the group corresponding to the invite code.
     */
    public String getGroupNameFromInv(String tokenInput) {
        if (tokenInput == null) {
            throw new NullPointerException();
        }

        String sql = "SELECT groupID FROM groupInvTable WHERE tokenVal = '" + tokenInput + "';";

        String groupName = null;

        try (Statement stmt = this.conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                groupName = DatabaseHandler.getInstance().getGroupNameFromID(rs.getInt("groupID"));
            }
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return groupName;
    }

    /**
     * Method to get a user ID from a group invite.
     *
     * @param tokenInput the group invite code to query.
     * @return the user ID corresponding to the invite code.
     */
    public int getUserIDFromInv(String tokenInput) {
        if (tokenInput == null) {
            throw new NullPointerException();
        }

        String sql = "SELECT userID FROM groupInvTable WHERE tokenVal = '" + tokenInput + "';";

        int userID = -1;

        try (Statement stmt = this.conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                userID = rs.getInt("userID");
            }
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return userID;
    }

    /**
     * Method to get the timeout value of a given group invite token.
     *
     * @param tokenInput the token to be queried.
     * @return a LocalDateTime object representing the expiry time of the token.
     */
    public LocalDateTime getTimeoutFromInv(String tokenInput){
        if (tokenInput == null) {
            throw new NullPointerException();
        }

        String sql = "SELECT expiry_time FROM groupInvTable WHERE tokenVal = '" + tokenInput + "';";

        LocalDateTime time = null;

        try (Statement stmt = this.conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                time = LocalDateTime.parse(rs.getString("expiry_time"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return time;
    }

    /**
     * Method to delete a group invite token from the database.
     *
     * @param tokenInput the token to be deleted.
     */
    public void deleteGroupInv(String tokenInput){
        if (tokenInput == null) {
            throw new NullPointerException();
        }

        String sql = "DELETE FROM groupInvTable WHERE tokenVal = '" + tokenInput + "';";


        try {
            Statement stmt = this.conn.createStatement();
            stmt.executeUpdate(sql);
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Method to check whether a given user is a member of a given group.
     *
     * @param userName the username of the user to be queried.
     * @param groupName the name of the group to be queried.
     * @return a boolean value representing whether the user is a member of the group.
     */
    public boolean isMemberOfGroup(String userName, String groupName) {
        if (userName == null) {
            throw new NullPointerException();
        }
        if (groupName == null) {
            throw new NullPointerException();
        }

        String sql = "SELECT * FROM group_membership WHERE User_Id = " + getUserIDFromUsername(userName)  +
                     " AND Group_Id = " +  getGroupIDFromName(groupName) + ";";

        boolean isMember = false;

        try (Statement stmt = this.conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
             isMember = rs.next();
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return isMember;
    }

    /**
     * Method to check whether a given invite token has passed its expiry time.
     *
     * @param tokenInput the token to be queried.
     * @return a boolean value representing whether the token has expired.
     */
    public boolean isInvExpired(String tokenInput) {
        if (tokenInput == null) {
            throw new NullPointerException();
        }

        String sql = "SELECT expiry_time FROM groupInvTable WHERE tokenVal = '" + tokenInput + "';";

        LocalDateTime time = null;

        try (Statement stmt = this.conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                time = LocalDateTime.parse(rs.getString("expiry_time"));
            }
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        assert time != null;
        return LocalDateTime.now().isAfter(time);

    }

    /**
     * Method to change the ownership of a given group to a given user.
     *
     * @param group the group whose ownership is to be changed.
     * @param user the user to be marked as the new owner.
     */
    public void changeGroupOwnership(Group group, User user) {
        if (group == null) {
            throw new NullPointerException();
        }
        if (user == null) {
            throw new NullPointerException();
        }

        String sql  = "UPDATE group_membership SET Group_Role = 'Admin' WHERE User_Id = " +
                       getUserIDFromUsername(group.getOwner().getUser().getUsername()) + " AND Group_Id = " +
                       getGroupIDFromName(group.getName());


        String sql2 = "UPDATE group_membership SET Group_Role = 'Owner' WHERE User_Id = " +
                       getUserIDFromUsername(user.getUsername()) + " AND Group_Id = " +
                       getGroupIDFromName(group.getName());

        try {
            Statement stmt = this.conn.createStatement();
            stmt.executeUpdate(sql);
            stmt.executeUpdate(sql2);
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Method to get a user's most recent weight.
     *
     * @param userID the user's unique ID.
     * @return the user's most recent weight value.
     */
    public float getMostRecentWeightValFromID(int userID) {
        if (userID < 0) {
            throw new IllegalArgumentException();
        }

        String sql = "SELECT weight FROM weight_entry WHERE user_id = " + userID + " ORDER BY entry_id DESC LIMIT 1;";

        float weight = -1;

        try (Statement stmt = this.conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                weight = rs.getFloat("weight");
            }
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return weight;
    }

    /**
     * Method to get a Group from a given group name
     * @param groupName the name of the group.
     * @return the Group matching the name.
     */
    public Group getGroupObjectFromGroupName(String groupName) {
        if (groupName == null) {
            throw new NullPointerException();
        }

        return getGroup(getGroupIDFromName(groupName));
    }

    /**
     * Method to get a list of all groups where a user has admin rights.
     *
     * @param username the user to be queried.
     * @return an ArrayList containing the names of all Groups in which this user is an admin or owner.
     */
    public ArrayList<String> getGroupsAdministrated(String username) {
        if (username == null) {
            throw new NullPointerException();
        }

        String sql = "SELECT Group_Name FROM group_table INNER JOIN group_membership ON group_membership.Group_Id = " +
                     "group_table.Group_Id WHERE group_membership.User_Id = '" + getUserIDFromUsername(username) + "' " +
                     "AND (group_membership.Group_Role = 'Owner' OR group_membership.Group_Role = 'Admin')";

        ArrayList<String> groupNames = new ArrayList<>();

        try (Statement stmt = this.conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                groupNames.add(rs.getString("Group_Name"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return groupNames;
    }

    /**
     * Method to add a GroupGoal to the database for a Group.
     *
     * @param groupName the name of the Group owning the GroupGoal.
     * @param goal the GroupGoal to be added.
     * @return a boolean value representing whether the goal was already owned by the group.
     */
    public boolean addGroupGoal(String groupName, GroupGoal goal) {
        if (groupName == null) {
            throw new NullPointerException();
        }
        if (goal == null) {
            throw new NullPointerException();
        }

        String sqlSel = "SELECT * FROM group_goal WHERE group_id = '" + getGroupIDFromName(groupName) + "' AND " +
                "target = '" + goal.getTarget() + "' AND unit = '" + goal.getUnit().toString() +
                "' AND end_date = '" + goal.getEndDate().toString() + "'";

        try (Statement stmtSel = this.conn.createStatement();
             ResultSet rs = stmtSel.executeQuery(sqlSel)) {
            if (rs.next()) {
                return false;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        
        String sqlIns = "INSERT INTO group_goal(group_id, target, unit, end_date) VALUES('" +
                getGroupIDFromName(groupName) + "', '" + goal.getTarget() + "', '" + goal.getUnit().toString() +
                "', '" + goal.getEndDate().toString() + "')";

        try {
            Statement stmt = this.conn.createStatement();
            stmt.executeUpdate(sqlIns);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
    }

    /**
     * Method to update a user's stored hash and salt values.
     *
     * @param userID the id of the user to update.
     * @param hash   their new hash.
     * @param salt   the salt used for that hash.
     */
    public void updatePassword(int userID, byte[] hash, byte[] salt) {
        if (userID < 0) {
            throw new IllegalArgumentException();
        }
        if (hash == null) {
            throw new NullPointerException();
        }
        if (salt == null) {
            throw new NullPointerException();
        }

        String sql = "UPDATE user SET hash = ?, salt = ? WHERE user_id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setBytes(1,hash);
            pstmt.setBytes(2, salt);
            pstmt.setInt(3, userID);

            pstmt.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Method to remove a user from a group.
     * @param userID the user's user ID.
     * @param groupID the group's group ID.
     */
    public void removeUserFromGroup(int userID, int groupID) {
        String sql = "DELETE FROM Group_Membership WHERE user_id = " + userID + " AND group_id = " + groupID + ";";
        String sql1= "DELETE FROM goal WHERE user_id = " + userID + " AND group_id = " + groupID + ";";

        try {
            Statement stmt = this.conn.createStatement();
            stmt.executeUpdate(sql);
            stmt.executeUpdate(sql1);
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    /**
     * Method to delete a group from the group table.
     * @param groupName the name of the group.
     */
    public void deleteGroup(String groupName) {
        int groupID = getGroupIDFromName(groupName);

        String sql =  "DELETE FROM group_goal WHERE group_id = " + groupID + ";";
        String sql1 = "DELETE FROM group_membership WHERE Group_Id = " + groupID + ";";
        String sql2 = "DELETE FROM group_table WHERE Group_Id = " + groupID + ";";
        String sql3 = "DELETE FROM groupInvTable WHERE groupID = " + groupID + ";";

        try {
            Statement stmt = this.conn.createStatement();
            stmt.executeUpdate(sql);
            stmt.executeUpdate(sql1);
            stmt.executeUpdate(sql2);
            stmt.executeUpdate(sql3);
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
