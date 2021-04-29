package sample;


//https://assets.publishing.service.gov.uk/government/uploads/system/uploads/attachment_data/file/618167/government_dietary_recommendations.pdf

import java.sql.SQLException;
import java.util.ArrayList;

public class GoalGenerator {

    private User user;
    private DatabaseHandler db;

    public GoalGenerator(User user) {
        this.user = user;
        this.db = DatabaseHandler.getInstance();
    }

    public void generateGoals() {
        ArrayList<SystemGoal> systemGoals = new ArrayList<>();

        //generate day to day goals
        //generate fitness goals
            //
    }

    public void updateDaytoDay() {
        try {
            db.updateDayToDayGoals(user);
        }
        catch (SQLException e) {
            System.out.println("Day to day update failed!");
        }
    }

    public ArrayList<SystemGoal> updateGoals() {
        updateDaytoDay();

        try {
            return db.selectSystemGoals(user);
        }
        catch (SQLException e) {
            System.out.println("Goal retrieval failed");
        }
        return new ArrayList<>();
    }
}
