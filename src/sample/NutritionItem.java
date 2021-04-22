package sample;

/**
 * A class to represent a nutrition item in the application as it appears in the database.
 *
 * @author Evan Clayton?
 * @author Samuel Scarfe
 *
 * @version 1.1
 *
 * 1.0 - First working version.
 * 1.1 - Added Javadoc and exceptions. Removed no argument constructor. Wrote JUnit test class.
 */
public class NutritionItem {
    private String name;
    private double kcal;
    private double protein;
    private double fat;
    private double carbs;
    private double sugar;
    private double fibre;
    private double cholesterol;

    public NutritionItem(String name, double kcal, double protein, double fat,
                         double carbs, double sugar, double fibre, double cholesterol) {
        checkConstructorInputs(name, kcal, protein, fat, carbs, sugar, fibre, cholesterol);

        this.name = name;
        this.kcal = kcal;
        this.protein = protein;
        this.fat = fat;
        this.carbs = carbs;
        this.sugar = sugar;
        this.fibre = fibre;
        this.cholesterol = cholesterol;
    }

    //get methods
    public String getName() {
        return name;
    }

    public double getKcal() {
        return kcal;
    }

    public double getProtein() {
        return protein;
    }

    public double getFat() {
        return fat;
    }

    public double getCarbs() {
        return carbs;
    }

    public double getSugar() {
        return sugar;
    }

    public double getFibre() {
        return fibre;
    }

    public double getCholesterol() {
        return cholesterol;
    }

    private void checkConstructorInputs(String name, double kcal, double protein, double fat,
                                        double carbs, double sugar, double fibre, double cholesterol) {
        if (name == null) {
            throw new NullPointerException();
        }
        if (name.length() < 1) {
            throw new IllegalArgumentException();
        }
        if (kcal < 0) {
            throw new IllegalArgumentException();
        }
        if (protein < 0) {
            throw new IllegalArgumentException();
        }
        if (fat < 0) {
            throw new IllegalArgumentException();
        }
        if (carbs < 0) {
            throw new IllegalArgumentException();
        }
        if (sugar < 0) {
            throw new IllegalArgumentException();
        }
        if (fibre < 0) {
            throw new IllegalArgumentException();
        }
        if (cholesterol < 0) {
            throw new IllegalArgumentException();
        }
    }
}
