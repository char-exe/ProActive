package sample;

/**
 * Class to hold a nutrition item, stores the name, caloric, protein, fat, carb, sugar, fibre and cholesterol levels
 *
 * @author ??
 *
 * @version 1.0
 */
public class NutritionItem {
    private String name;
    private double kcal, protein, fat, carbs, sugar, fibre, cholesterol;

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
     * Default constructor
     */
    public NutritionItem() {
        this.name = "DefaultName";
        this.kcal = 0.0;
        this.protein = 0.0;
        this.fat = 0.0;
        this.carbs = 0.0;
        this.sugar = 0.0;
        this.fibre = 0.0;
        this.cholesterol = 0.0;
    }

    /**
     * Getter for name
     *
     * @return gets the name of the nutrition item
     */
    public String getName() { return name; }

    /**
     * Getter for Kcal
     *
     * @return gets the kcal content of the nutrition item
     */
    public double getKcal() { return kcal; }

    /**
     * Getter for Protein
     *
     * @return gets the protein content of the nutrition item
     */
    public double getProtein() { return protein; }

    /**
     * Getter for Fat
     *
     * @return gets the fat content of the nutrition item
     */
    public double getFat() { return fat; }

    /**
     * Getter for Carbs
     *
     * @return gets the carbohydrate content of the nutrition item
     */
    public double getCarbs() { return carbs; }

    /**
     * Getter for sugar
     *
     * @return gets the sugar content of the nutrition item
     */
    public double getSugar() { return sugar; }

    /**
     * Getter for Fibre
     *
     * @return gets the fibre content of the nutrition item
     */
    public double getFibre() { return fibre; }

    /**
     * Getter for Cholesterol
     *
     * @return gets the cholesterol content of the nutrition item
     */
    public double getCholesterol() { return cholesterol; }


}
