package sample;

import org.sqlite.core.DB;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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
 * @version 1.9
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
 * 1.6 - Updated such that database string no longer needs to be passed as an argument. Added method for getting intake
 *       data for a user from the database. Added method for getting minutes spent data from database. Added method
 *       for getting burn rate in calories per minute and calories burned data for user. Added method for getting
 *       weight entries for a user from the database.
 *
 * 1.7 - Minor refactor to enforce the Singleton pattern.
 *
 * 1.8 - Added Javadoc for outstanding methods
 *
 * 1.9 - Added methods to assist with activity logging.
 */
public class DatabaseHandler
{
    private static final DatabaseHandler INSTANCE = new DatabaseHandler();
    private static final String CONNECTION = "jdbc:sqlite:proactive.db";

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
            MEAL_ID, MEAL_CATEGORY, FOOD_ID, USER_ID, DATE_OF, QUANTITY
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
     * Private default constructor. Enforces the Singleton pattern.
     */
    private DatabaseHandler(){
        try{
            conn = DriverManager.getConnection(CONNECTION);
        } catch (SQLException e){
            System.out.println(e.getMessage());
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

    /**
     * Method to compare a provided username with all usernames currently stored in the database and return true if
     * username is unique
     *
     * @param username A username provided by the user of the application
     * @return true if username is unique, false otherwise
     */
    public boolean checkUserNameUnique(String username){
        String sql = "SELECT * FROM user WHERE username = '" + username + "'";

        try {
            Statement stmt = this.conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (!rs.next()) {
                return true;
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
        String sql = "INSERT INTO weight_entry (user_id, weight, date_of)" +
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

    /**
     * Method to insert a weight value into the weight_entry database table
     *
     * @param username The user's username.
     * @param weight   Takes in the current weight of the user in kg
     * @param date     Takes in the time in which the user wants to set this date to.
     * @throws SQLException when a database error occurs.
     */
    public void insertWeightValue(String username, float weight, LocalDate date) throws SQLException {
        String sql = "INSERT INTO weight_entry (user_id, weight, date_of)" +
                "VALUES('" + getUserIDFromUsername(username) + "', '" + weight + "','" + date.toString() + "')";

        Statement stmt  = conn.createStatement();
        stmt.executeUpdate(sql);

        System.out.println("Weight entry added for " + username);
    }

    /**
     * Method to add a token entry to the regtokens table in the database
     *
     * @param tokenVal Takes in the unique token that was randomly generated by the {@link TokenHandler} class
     * @param timestamp Takes in the current system time in milliseconds / 1000
     */
    public void addTokenEntry(String tokenVal, long timestamp){
        //TODO determine correct delay, currently we are allowing 1800 milliseconds, should this be 1800000?
        //TODO determine if we can limit this method to only 1 input and create the timestamp locally?
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

    /**
     * Method to get information about a token, this information contains both the token value and the expiration time
     * for that token
     *
     * @param tokenVal The token that will be pulled from the database
     *
     * @return Returns a resultset from the SQL query, if there are no responses available, a resultset will still be
     *         provided but will be empty, in this case, a null value will be returned
     *
     * @throws SQLException this occurs if there is an error in the executing of the SQL statement, often this will
     *                      manifest due to incorrectly formatted code or if the database is not available at the time
     *                      of sql execution
     */
    public ResultSet getTokenResult(String tokenVal) throws SQLException {
        String sql = "SELECT tokenVal, timeDelay FROM regTokens WHERE tokenVal = '" + tokenVal + "'";

        Statement stmt  = this.conn.createStatement();

        ResultSet rs = stmt.executeQuery(sql);

        if (rs.isBeforeFirst()){
            return rs;
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
        String searchName = itemName + '%';
        String sql = "SELECT * FROM food WHERE name LIKE '" + searchName + "'";

        NutritionItem nutritionItem = null;

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

        ExerciseItem exerciseItem = null;

        try (Statement stmt  = this.conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)) {

            exerciseItem = new ExerciseItem(rs.getString("name"),
                                            rs.getInt("burn_rate"));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return exerciseItem;
    }

    public float getBurnRate(int exerciseId) {
        String sql = "SELECT burn_rate FROM exercise WHERE id = '" + exerciseId + "'";
        float burnRate= 0;

        try (Statement stmt = this.conn.createStatement();
             ResultSet rs   = stmt.executeQuery(sql)) {

            burnRate =  rs.getInt("burn_rate");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return burnRate/30;
    }

    /**
     * Method to delete tokens from the regtokens table, this occurs when a token has been used to ensure we minimize
     * database bloat
     *
     * @param token takes in a token value
     */
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

    /**
     * Method to dynamically update a SQL table, it takes in 4 different parameters which allows this method to modify
     * any table that has a username as a unique key
     *
     * @param table This specifies the table this method will be editing
     * @param column This specifies the column in the above table this method will be editing
     * @param valToUpdateTo This specifies the value that this method will use to overwrite existing data with
     * @param username This specifies the username that will make up the 'WHERE' clause of the SQL Statement
     */
    public void editValue(String table, String column, String valToUpdateTo, String username){
    String sql = "UPDATE " + table.toUpperCase(Locale.ROOT) +
                 " SET " + column.toUpperCase(Locale.ROOT) + " = " + valToUpdateTo +
                 " WHERE username = '" + username + "'";

        try {
            Statement stmt  = this.conn.createStatement();
            stmt.executeUpdate(sql);
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }

    //TODO error handling
    }

    /**
     * Method to dynamically update a SQL table, it takes in 4 different parameters which allows this method to modify
     * any table that has a username as a unique key, overloaded to accept an integer value in valToUpdate
     *
     * @param table This specifies the table this method will be editing
     * @param column This specifies the column in the above table this method will be editing
     * @param valToUpdateTo This specifies the value that this method will use to overwrite existing data with
     * @param username This specifies the username that will make up the 'WHERE' clause of the SQL Statement
     */
    public void editValue(String table, String column, int valToUpdateTo, String username){
        String sql = "UPDATE " + table.toUpperCase(Locale.ROOT) +
                     " SET " + column.toUpperCase(Locale.ROOT) + " = " + valToUpdateTo +
                     " WHERE username = '" + username + "'";

        try {
            Statement stmt  = this.conn.createStatement();
            stmt.executeUpdate(sql);
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Method to build a user object based on the username provided
     *
     * @param username username to query the database for
     *
     * @return returns a complete user object for use as a persistent user throughout application
     */
    public User createUserObjectFromUsername(String username){
        //TODO modify so that only the specific information is pulled from the table - High Priority
        //TODO remove try/catch block and instead throw as exception
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

    /**
     * Method to get the past seven days of caloric intake for the user in a Map with keys as String representations
     * of each day's date.
     *
     * @param username The user's username.
     * @return A Map of String representation of a date against calories intaken.
     */
    public HashMap<String, Integer> getIntakeEntries(String username)
    {
        HashMap<String, Integer> entries = new HashMap<>();
        LocalDate today = LocalDate.now();
        LocalDate lastWeek = today.minusDays(6);

        String sql = "SELECT date_of, quantity FROM meal WHERE user_id = '" + getUserIDFromUsername(username) + "' " +
                     "AND date_of BETWEEN '" + lastWeek.toString() + "' AND '" + today.toString() + "'";

        try (Statement stmt  = this.conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql))
        {
            while (rs.next())
            {
                String date = rs.getString("date_of");
                if (entries.containsKey(date))
                {
                    entries.put(date, entries.get(date) + rs.getInt("quantity"));
                }
                else
                {
                    entries.put(date, rs.getInt("quantity"));
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
     * @return A Map of String representation of a date against minutes spent exercising.
     */
    public HashMap<String, Integer> getSpentEntries(String username)
    {
        HashMap<String, Integer> entries = new HashMap<>();
        LocalDate today = LocalDate.now();
        LocalDate lastWeek = today.minusDays(6);

        String sql = "SELECT date_of, duration FROM activity WHERE user_id = '" +
                getUserIDFromUsername(username) + "' " + "AND date_of BETWEEN '" + lastWeek.toString() +
                "' AND '" + today.toString() + "'";

        try (Statement stmt  = this.conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql))
        {
            while (rs.next())
            {
                String date = rs.getString("date_of");

                if (entries.containsKey(date))
                {
                    entries.put(date, entries.get(date) + rs.getInt("duration"));
                }
                else
                {
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
     * @return A Map of String representation of a date against calories burned.
     */
    public HashMap<String, Float> getBurnedEntries(String username)
    {
        HashMap<String, Float> entries = new HashMap<>();
        LocalDate today = LocalDate.now();
        LocalDate lastWeek = today.minusDays(6);

        String sql = "SELECT date_of, duration, exercise_id FROM activity WHERE user_id = '" +
                getUserIDFromUsername(username) + "' " + "AND date_of BETWEEN '" + lastWeek.toString() +
                "' AND '" + today.toString() + "'";

        try (Statement stmt  = this.conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql))
        {
            while (rs.next())
            {
                String date = rs.getString("date_of");
                int exerciseId = rs.getInt("exercise_id");

                if (entries.containsKey(date))
                {
                    entries.put(date, entries.get(date) + rs.getInt("duration") * getBurnRate(exerciseId));
                }
                else
                {
                    entries.put(date, rs.getInt("duration") * getBurnRate(exerciseId));
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return entries;
    }

    /**
     * Method to get the past seven days of weight entries for the user in a Map with keys as String representations
     * of each day's date.
     *
     * @param username The user's username.
     * @return A Map of String representation of a date against weight entries.
     */
    public HashMap<String, Integer> getWeightEntries(String username)
    {
        HashMap<String, Integer> entries = new HashMap<>();
        LocalDate today = LocalDate.now();
        LocalDate lastWeek = today.minusDays(6);

        String sql = "SELECT date_of, weight FROM weight_entry WHERE user_id = '" +
                getUserIDFromUsername(username) + "' " + "AND date_of BETWEEN '" + lastWeek.toString() +
                "' AND '" + today.toString() + "'";

        try (Statement stmt  = this.conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql))
        {
            while (rs.next())
            {
                String date = rs.getString("date_of");

                entries.put(date, rs.getInt("weight"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return entries;
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
             ResultSet rs    = stmt.executeQuery(sql))
        {
            while (rs.next())
            {
                exercises.add(rs.getString("name"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(exercises);
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
             ResultSet rs    = stmt.executeQuery(sql))
        {
            while (rs.next())
            {
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
        String sql = "SELECT id FROM exercise WHERE name LIKE '" + name + "'";
        int exerciseId = -1;

        try (Statement stmt  = this.conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)) {

            exerciseId = rs.getInt("id");

        } catch (SQLException e) {
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
    public void insertExercise(String username, String exercise, int duration) throws SQLException{

        String sql = "INSERT INTO activity (exercise_id, user_id, duration, date_of)" +
                "VALUES('" + getExerciseId(exercise)  + "', '" + getUserIDFromUsername(username) +
                        "','" + duration + "','" + LocalDate.now().toString() + "')";

        Statement stmt  = conn.createStatement();
        stmt.executeUpdate(sql);

        System.out.println("Added " + exercise + " to database for " + username);
    }

    /**
     * Method to get an food_id for a food given by name.
     *
     * @param name the name of the food.
     * @return the food_id.
     */
    public int getFoodId(String name)  {
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

        String sql = "INSERT INTO meal (meal_category, food_id, user_id, date_of, quantity)" +
                     "VALUES('" + meal  + "', '" + getFoodId(food) + "','" + getUserIDFromUsername(username) +
                     "','" + date.toString() + "','" + quantity + "')";

        Statement stmt  = conn.createStatement();
        stmt.executeUpdate(sql);
        System.out.println("Added " + food + " to database for " + username);
    }

    public double getKcal(String foodName) {
        String sql = "SELECT kcal FROM food WHERE name LIKE '" + foodName + "'";
        double kcal = -1;

        try (Statement stmt  = this.conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)) {

            kcal = rs.getDouble("kcal");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return kcal;
    }
}
