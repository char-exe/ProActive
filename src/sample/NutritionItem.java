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

    /**
     * Constructor for nutrition item
     *
     * @param name Takes in the name of the new nutrition item
     * @param kcal Takes in the caloric content of the nutrition item
     * @param protein Takes in the protein content of the nutrition item
     * @param fat Takes in the fat content of the nutrition item
     * @param carbs Takes in the carbohydrate content of the nutrition item
     * @param sugar Takes in the sugar content of the nutrition item
     * @param fibre Takes in the fibre content of the nutrition item
     * @param cholesterol Takes in the cholesterol content of the nutrition item
     */
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

    /**
     * Getter for name
     *
     * @return gets the name of the nutrition item
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for Kcal
     *
     * @return gets the kcal content of the nutrition item
     */
    public double getKcal() {
        return kcal;
    }

    /**
     * Getter for Protein
     *
     * @return gets the protein content of the nutrition item
     */
    public double getProtein() {
        return protein;
    }

    /**
     * Getter for Fat
     *
     * @return gets the fat content of the nutrition item
     */
    public double getFat() {
        return fat;
    }

    /**
     * Getter for Carbs
     *
     * @return gets the carbohydrate content of the nutrition item
     */
    public double getCarbs() {
        return carbs;
    }

    /**
     * Getter for sugar
     *
     * @return gets the sugar content of the nutrition item
     */
    public double getSugar() {
        return sugar;
    }

    /**
     * Getter for Fibre
     *
     * @return gets the fibre content of the nutrition item
     */
    public double getFibre() {
        return fibre;
    }

    /**
     * Getter for Cholesterol
     *
     * @return gets the cholesterol content of the nutrition item
     */
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
