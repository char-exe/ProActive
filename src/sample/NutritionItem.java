package sample;

import java.util.HashMap;
import java.util.Map;

/**
 * A class to represent a nutrition item in the application as it appears in the database.
 *
 * @author Samuel Scarfe
 *
 * @version 1.3
 *
 * 1.0 - First working version.
 * 1.1 - Added Javadoc and exceptions. Removed no argument constructor. Wrote JUnit test class.
 * 1.2 - Added vitamins and minerals.
 * 1.3 - Implemented equals and hashCode to fix bug in LogActivityController whereby repeated additions of
 *       NutritionItems with the same name would not increment the counter.
 */
public class NutritionItem {
    private final String name;

    //Macronutrients
    private final double kcal;
    private final double proteinG;
    private final double fatG;
    private final double carbsG;
    private final double sugarG;
    private final double fibreG;
    private final double cholesterolMg;

    //Minerals
    private final double sodiumMg;
    private final double potassiumMg;
    private final double calciumMg;
    private final double magnesiumMg;
    private final double phosphorusMg;
    private final double ironMg;
    private final double copperMg;
    private final double zincMg;
    private final double chlorideMg;
    private final double seleniumUg;
    private final double iodineUg;

    //Vitamins
    private final double vitAUg;
    private final double vitDUg;
    private final double thiaminMg;
    private final double riboflavinMg;
    private final double niacinMg;
    private final double vitB6Mg;
    private final double vitB12Ug;
    private final double folateUg;
    private final double vitCMg;

    /**
     * Basic constructor for a NutritionItem. Initialises name, kcal, proteinG, fatG, carbsG, sugarG, fibreG, and
     * cholesterolMg to the passed values. Initialises all other instance variables to 0.
     *
     * @param name Takes in the name of the new nutrition item
     * @param kcal Takes in the caloric content of the nutrition item
     * @param proteinG Takes in the protein content of the nutrition item, in grams per 100g
     * @param fatG Takes in the fat content of the nutrition item, in grams per 100g
     * @param carbsG Takes in the carbohydrate content of the nutrition item, in grams per 100g
     * @param sugarG Takes in the sugar content of the nutrition item, in grams per 100g
     * @param fibreG Takes in the fibre content of the nutrition item, in grams per 100 g
     * @param cholesterolMg Takes in the cholesterol content of the nutrition item, in milligrams per 100g
     */
    public NutritionItem(String name, double kcal, double proteinG, double fatG,
                         double carbsG, double sugarG, double fibreG, double cholesterolMg) {

        checkConstructorInputs(name, kcal, proteinG, fatG, carbsG, sugarG, fibreG, cholesterolMg);

        this.name         = name;   this.kcal          = kcal;          this.proteinG    = proteinG;
        this.fatG         = fatG;   this.carbsG        = carbsG;        this.sugarG      = sugarG;
        this.fibreG       = fibreG; this.cholesterolMg = cholesterolMg; this.sodiumMg    = 0;
        this.potassiumMg  = 0;      this.calciumMg     = 0;             this.magnesiumMg = 0;
        this.phosphorusMg = 0;      this.ironMg        = 0;             this.copperMg    = 0;
        this.zincMg       = 0;      this.chlorideMg    = 0;             this.seleniumUg  = 0;
        this.iodineUg     = 0;      this.vitAUg        = 0;             this.vitDUg      = 0;
        this.thiaminMg    = 0;      this.riboflavinMg  = 0;             this.niacinMg    = 0;
        this.vitB6Mg      = 0;      this.vitB12Ug      = 0;             this.folateUg    = 0;
        this.vitCMg       = 0;
    }

    /**
     * Complete constructor for a NutritionItem. Initialises all instance variables to the equivalent passed parameter.
     *
     * @param name Takes in the name of the new nutrition item
     * @param kcal Takes in the caloric content of the nutrition item
     * @param proteinG Takes in the protein content of the nutrition item, in grams per 100g
     * @param fatG Takes in the fat content of the nutrition item, in grams per 100g
     * @param carbsG Takes in the carbohydrate content of the nutrition item, in grams per 100g
     * @param sugarG Takes in the sugar content of the nutrition item, in grams per 100g
     * @param fibreG Takes in the fibre content of the nutrition item, in grams per 100 g
     * @param cholesterolMg Takes in the cholesterol content of the nutrition item, in milligrams per 100g
     * @param sodiumMg Takes in the sodium content of the nutrition item, in milligrams per 100g
     * @param potassiumMg Takes in the potassium content of the nutrition item, in milligrams per 100g
     * @param calciumMg Takes in the calcium content of the nutrition item, in milligrams per 100g
     * @param magnesiumMg Takes in the magnesium content of the nutrition item, in milligrams per 100g
     * @param phosphorusMg Takes in the phosphorus content of the nutrition item, in milligrams per 100g
     * @param ironMg Takes in the iron content of the nutrition item, in milligrams per 100g
     * @param copperMg Takes in the copper content of the nutrition item, in milligrams per 100g
     * @param zincMg Takes in the zinc content of the nutrition item, in milligrams per 100g
     * @param chlorideMg Takes in the chloride content of the nutrition item, in milligrams per 100g
     * @param seleniumUg Takes in the selenium content of the nutrition item, in micrograms per 100g
     * @param iodineUg Takes in the iodine content of the nutrition item, in micrograms per 100g
     * @param vitAUg Takes in the vitamin A content of the nutrition item, in micrograms per 100g
     * @param vitDUg Takes in the vitamin D content of the nutrition item, in micrograms per 100g
     * @param thiaminMg Takes in the thiamin content of the nutrition item, in milligrams per 100g
     * @param riboflavinMg Takes in the riboflavin content of the nutrition item, in milligrams per 100g
     * @param niacinMg Takes in the niacin content of the nutrition item, in milligrams per 100g
     * @param vitB6Mg Takes in the vitamin B6 content of the nutrition item, in milligrams per 100g
     * @param vitB12Ug Takes in the vitamin B12 content of the nutrition item, in micrograms per 100g
     * @param folateUg Takes in the folate content of the nutrition item, in micrograms per 100g
     * @param vitCMg Takes in the vitamin C content of the nutrition item, in milligrams per 100g
     */
    public NutritionItem(String name, double kcal, double proteinG, double fatG, double carbsG, double sugarG,
                         double fibreG, double cholesterolMg, double sodiumMg, double potassiumMg, double calciumMg,
                         double magnesiumMg, double phosphorusMg, double ironMg, double copperMg, double zincMg,
                         double chlorideMg, double seleniumUg, double iodineUg, double vitAUg, double vitDUg,
                         double thiaminMg, double riboflavinMg, double niacinMg, double vitB6Mg, double vitB12Ug,
                         double folateUg, double vitCMg
    ) {

        checkConstructorInputs(
                name, kcal, proteinG, fatG, carbsG, sugarG, fibreG, cholesterolMg, sodiumMg, potassiumMg, calciumMg,
                magnesiumMg, phosphorusMg, ironMg, copperMg, zincMg, chlorideMg, seleniumUg, iodineUg, vitAUg, vitDUg,
                thiaminMg, riboflavinMg, niacinMg, vitB6Mg, vitB12Ug, folateUg, vitCMg
        );

        this.name         = name;         this.kcal          = kcal;          this.proteinG    = proteinG;
        this.fatG         = fatG;         this.carbsG        = carbsG;        this.sugarG      = sugarG;
        this.fibreG       = fibreG;       this.cholesterolMg = cholesterolMg; this.sodiumMg    = sodiumMg;
        this.potassiumMg  = potassiumMg;  this.calciumMg     = calciumMg;     this.magnesiumMg = magnesiumMg;
        this.phosphorusMg = phosphorusMg; this.ironMg        = ironMg;        this.copperMg    = copperMg;
        this.zincMg       = zincMg;       this.chlorideMg    = chlorideMg;    this.seleniumUg  = seleniumUg;
        this.iodineUg     = iodineUg;     this.vitAUg        = vitAUg;        this.vitDUg      = vitDUg;
        this.thiaminMg    = thiaminMg;    this.riboflavinMg  = riboflavinMg;  this.niacinMg    = niacinMg;
        this.vitB6Mg      = vitB6Mg;      this.vitB12Ug      = vitB12Ug;      this.folateUg    = folateUg;
        this.vitCMg       = vitCMg;
    }

    /**
     * Gets the name for this NutritionItem
     *
     * @return the name of this NutritionItem
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the kcal for this NutritionItem
     *
     * @return the kcal for this NutritionItem
     */
    public double getKcal() {
        return kcal;
    }

    /**
     * Gets the protein for this NutritionItem, in grams per 100g
     *
     * @return the protein for this NutritionItem, in grams per 100g
     */
    public double getProteinG() {
        return proteinG;
    }

    /**
     * Gets the fat for this NutritionItem, in grams per 100g
     *
     * @return the fat for this NutritionItem, in grams per 100g
     */
    public double getFatG() {
        return fatG;
    }

    /**
     * Gets the carbs for this NutritionItem, in grams per 100g
     *
     * @return the carbs for this NutritionItem, in grams per 100g
     */
    public double getCarbsG() {
        return carbsG;
    }

    /**
     * Gets the sugar for this NutritionItem, in grams per 100g
     *
     * @return the sugar for this NutritionItem, in grams per 100g
     */
    public double getSugarG() {
        return sugarG;
    }

    /**
     * Gets the fibre for this NutritionItem, in grams per 100g
     *
     * @return the fibre for this NutritionItem, in grams per 100g
     */
    public double getFibreG() {
        return fibreG;
    }

    /**
     * Gets the cholesterol for this NutritionItem, in milligrams per 100g
     *
     * @return the cholesterol for this NutritionItem, in milligrams per 100g
     */
    public double getCholesterolMg() {
        return cholesterolMg;
    }

    /**
     * Gets the sodium for this NutritionItem, in milligrams per 100g
     *
     * @return the sodium for this NutritionItem, in milligrams per 100g
     */
    public double getSodiumMg() {
        return sodiumMg;
    }

    /**
     * Gets the potassium for this NutritionItem, in milligrams per 100g
     *
     * @return the potassium for this NutritionItem, in milligrams per 100g
     */
    public double getPotassiumMg() {
        return potassiumMg;
    }

    /**
     * Gets the calcium for this NutritionItem, in milligrams per 100g
     *
     * @return the calcium for this NutritionItem, in milligrams per 100g
     */
    public double getCalciumMg() {
        return calciumMg;
    }

    /**
     * Gets the magnesium for this NutritionItem, in milligrams per 100g
     *
     * @return the magnesium for this NutritionItem, in milligrams per 100g
     */
    public double getMagnesiumMg() {
        return magnesiumMg;
    }

    /**
     * Gets the phosphorus for this NutritionItem, in milligrams per 100g
     *
     * @return the phosphorus for this NutritionItem, in milligrams per 100g
     */
    public double getPhosphorusMg() {
        return phosphorusMg;
    }

    /**
     * Gets the iron for this NutritionItem, in milligrams per 100g
     *
     * @return the iron for this NutritionItem, in milligrams per 100g
     */
    public double getIronMg() {
        return ironMg;
    }

    /**
     * Gets the copper for this NutritionItem, in milligrams per 100g
     *
     * @return the copper for this NutritionItem, in milligrams per 100g
     */
    public double getCopperMg() {
        return copperMg;
    }

    /**
     * Gets the zinc for this NutritionItem, in milligrams per 100g
     *
     * @return the zinc for this NutritionItem, in milligrams per 100g
     */
    public double getZincMg() {
        return zincMg;
    }

    /**
     * Gets the chloride for this NutritionItem, in milligrams per 100g
     *
     * @return the chloride for this NutritionItem, in milligrams per 100g
     */
    public double getChlorideMg() {
        return chlorideMg;
    }

    /**
     * Gets the selenium for this NutritionItem, in micrograms per 100g
     *
     * @return the selenium for this NutritionItem, in micrograms per 100g
     */
    public double getSeleniumUg() {
        return seleniumUg;
    }

    /**
     * Gets the iodine for this NutritionItem, in micrograms per 100g
     *
     * @return the iodine for this NutritionItem, in micrograms per 100g
     */
    public double getIodineUg() {
        return iodineUg;
    }

    /**
     * Gets the vitamin A for this NutritionItem, in micrograms per 100g
     *
     * @return the vitamin A for this NutritionItem, in micrograms per 100g
     */
    public double getVitAUg() {
        return vitAUg;
    }

    /**
     * Gets the vitamin D for this NutritionItem, in micrograms per 100g
     *
     * @return the vitamin D for this NutritionItem, in micrograms per 100g
     */
    public double getVitDUg() {
        return vitDUg;
    }

    /**
     * Gets the thiamin for this NutritionItem, in milligrams per 100g
     *
     * @return the thiamin for this NutritionItem, in milligrams per 100g
     */
    public double getThiaminMg() {
        return thiaminMg;
    }

    /**
     * Gets the riboflavin for this NutritionItem, in milligrams per 100g
     *
     * @return the riboflavin for this NutritionItem, in milligrams per 100g
     */
    public double getRiboflavinMg() {
        return riboflavinMg;
    }

    /**
     * Gets the niacin for this NutritionItem, in milligrams per 100g
     *
     * @return the niacin for this NutritionItem, in milligrams per 100g
     */
    public double getNiacinMg() {
        return niacinMg;
    }

    /**
     * Gets the vitamin B6 for this NutritionItem, in milligrams per 100g
     *
     * @return the vitamin B6 for this NutritionItem, in milligrams per 100g
     */
    public double getVitB6Mg() {
        return vitB6Mg;
    }

    /**
     * Gets the vitamin B12 for this NutritionItem, in micrograms per 100g
     *
     * @return the vitamin B12 for this NutritionItem, in micrograms per 100g
     */
    public double getVitB12Ug() {
        return vitB12Ug;
    }

    /**
     * Gets the folate for this NutritionItem, in micrograms per 100g
     *
     * @return the folate for this NutritionItem, in micrograms per 100g
     */
    public double getFolateUg() {
        return folateUg;
    }

    /**
     * Gets the vitamin C for this NutritionItem, in milligrams per 100g
     *
     * @return the vitamin C for this NutritionItem, in milligrams per 100g
     */
    public double getVitCMg() {
        return vitCMg;
    }

    /**
     * Method to get all macronutrient values of a this NutritionItem
     *
     * @return a HashMap the names of the macronutrients against their values.
     */
    public HashMap<String, Double> getMacroNutrients(){
        return new HashMap<>(Map.ofEntries(
                Map.entry("kcal", kcal),
                Map.entry("Protein (g)", proteinG),
                Map.entry("Fat (g)",fatG),
                Map.entry("Carbs (g)", carbsG),
                Map.entry("Sugar (g)", sugarG),
                Map.entry("Fibre (g)", fibreG),
                Map.entry("Cholestrol (mg)", cholesterolMg)
        ));
    }

    /**
     * Method to get all mineral values of a this NutritionItem
     *
     * @return a HashMap the names of the minerals against their values.
     */
    public HashMap<String, Double> getMinerals(){
        return new HashMap<>(Map.ofEntries(
                Map.entry("Potassium (mg)", potassiumMg),
                Map.entry("Sodium (mg)", sodiumMg),
                Map.entry("Calcium (mg)", calciumMg),
                Map.entry("Magnesium (mg)", magnesiumMg),
                Map.entry("Zinc (mg)", zincMg),
                Map.entry("Copper (mg)", copperMg),
                Map.entry("Selenium (ug)", seleniumUg),
                Map.entry("Iodine (ug)", iodineUg),
                Map.entry("Chloride (mg)",chlorideMg),
                Map.entry("Phosphorus (mg)", phosphorusMg),
                Map.entry("Iron (mg)", ironMg)

        ));
    }

    /**
     * Method to get all vitamin values of a this NutritionItem
     *
     * @return a HashMap the names of the vitamins against their values.
     */
    public HashMap<String, Double> getVitamins(){
        return new HashMap<>(Map.ofEntries(
                Map.entry("Folate (ug)", folateUg),
                Map.entry("Niacin (mg)", niacinMg),
                Map.entry("Riboflavin (mg)", riboflavinMg),
                Map.entry("Thiamin (mg)", thiaminMg),
                Map.entry("Vitamin B6 (mg)", vitB6Mg),
                Map.entry("Vitamin B12 (ug)", vitB12Ug),
                Map.entry("Vitamin C (mg)", vitCMg),
                Map.entry("Vitamin A (ug)", vitAUg),
                Map.entry("Vitamin D (ug)", vitDUg)
        ));
    }

    /**
     * Evaluates whether this NutritionItem is equal to a passed Object. Equality is assessed such that only
     * NutritionItems can be equal, and only if they have the same name. NutritionItems are restricted in the database
     * such that the names must be unique, so measuring equality in this way maintains parity between the database and
     * the application.
     *
     * @param o the object to be measured for equality.
     * @return  a boolean representing whether this NutritionItem is equal to the passed object.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof NutritionItem)) {
            return  false;
        }

        return this.name.equals(((NutritionItem) o).name);
    }

    /**
     * Retrieves a hash value for this NutritionItem. As NutritionItems are required to have unique names in the
     * database, using the native String hashCode method for name is valid and simple. Were this not guaranteed then
     * this function may need to consider calculating different prime modulus of each nutrient in turn.
     *
     * @return the hash value for this NutritionItem.
     */
    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    /**
     * Private helper method to check parameters for the basic constructor.
     *
     * @param name Takes in the name of the new nutrition item
     * @param kcal Takes in the caloric content of the nutrition item
     * @param proteinG Takes in the protein content of the nutrition item, in grams per 100g
     * @param fatG Takes in the fat content of the nutrition item, in grams per 100g
     * @param carbsG Takes in the carbohydrate content of the nutrition item, in grams per 100g
     * @param sugarG Takes in the sugar content of the nutrition item, in grams per 100g
     * @param fibreG Takes in the fibre content of the nutrition item, in grams per 100 g
     * @param cholesterolMg Takes in the cholesterol content of the nutrition item, in milligrams per 100g
     */
    private void checkConstructorInputs(String name, double kcal, double proteinG, double fatG,
                                        double carbsG, double sugarG, double fibreG, double cholesterolMg) {
        if (name == null) {
            throw new NullPointerException();
        }
        if (name.length() < 1) {
            throw new IllegalArgumentException();
        }
        if (kcal < 0) {
            throw new IllegalArgumentException();
        }
        if (proteinG < 0) {
            throw new IllegalArgumentException();
        }
        if (fatG < 0) {
            throw new IllegalArgumentException();
        }
        if (carbsG < 0) {
            throw new IllegalArgumentException();
        }
        if (sugarG < 0) {
            throw new IllegalArgumentException();
        }
        if (fibreG < 0) {
            throw new IllegalArgumentException();
        }
        if (cholesterolMg < 0) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Private helper method to check parameters for the complete constructor.
     *
     * @param name Takes in the name of the new nutrition item
     * @param kcal Takes in the caloric content of the nutrition item
     * @param proteinG Takes in the protein content of the nutrition item, in grams per 100g
     * @param fatG Takes in the fat content of the nutrition item, in grams per 100g
     * @param carbsG Takes in the carbohydrate content of the nutrition item, in grams per 100g
     * @param sugarG Takes in the sugar content of the nutrition item, in grams per 100g
     * @param fibreG Takes in the fibre content of the nutrition item, in grams per 100 g
     * @param cholesterolMg Takes in the cholesterol content of the nutrition item, in milligrams per 100g
     * @param sodiumMg Takes in the sodium content of the nutrition item, in milligrams per 100g
     * @param potassiumMg Takes in the potassium content of the nutrition item, in milligrams per 100g
     * @param calciumMg Takes in the calcium content of the nutrition item, in milligrams per 100g
     * @param magnesiumMg Takes in the magnesium content of the nutrition item, in milligrams per 100g
     * @param phosphorusMg Takes in the phosphorus content of the nutrition item, in milligrams per 100g
     * @param ironMg Takes in the iron content of the nutrition item, in milligrams per 100g
     * @param copperMg Takes in the copper content of the nutrition item, in milligrams per 100g
     * @param zincMg Takes in the zinc content of the nutrition item, in milligrams per 100g
     * @param chlorideMg Takes in the chloride content of the nutrition item, in milligrams per 100g
     * @param seleniumUg Takes in the selenium content of the nutrition item, in micrograms per 100g
     * @param iodineUg Takes in the iodine content of the nutrition item, in micrograms per 100g
     * @param vitAUg Takes in the vitamin A content of the nutrition item, in micrograms per 100g
     * @param vitDUg Takes in the vitamin D content of the nutrition item, in micrograms per 100g
     * @param thiaminMg Takes in the thiamin content of the nutrition item, in milligrams per 100g
     * @param riboflavinMg Takes in the riboflavin content of the nutrition item, in milligrams per 100g
     * @param niacinMg Takes in the niacin content of the nutrition item, in milligrams per 100g
     * @param vitB6Mg Takes in the vitamin B6 content of the nutrition item, in milligrams per 100g
     * @param vitB12Ug Takes in the vitamin B12 content of the nutrition item, in micrograms per 100g
     * @param folateUg Takes in the folate content of the nutrition item, in micrograms per 100g
     * @param vitCMg Takes in the vitamin C content of the nutrition item, in milligrams per 100g
     */
    private void checkConstructorInputs(
            String name, double kcal, double proteinG, double fatG, double carbsG, double sugarG, double fibreG,
            double cholesterolMg, double sodiumMg, double potassiumMg, double calciumMg, double magnesiumMg,
            double phosphorusMg, double ironMg, double copperMg, double zincMg, double chlorideMg, double seleniumUg,
            double iodineUg, double vitAUg, double vitDUg, double thiaminMg, double riboflavinMg, double niacinMg,
            double vitB6Mg, double vitB12Ug, double folateUg, double vitCMg
    ) {

        checkConstructorInputs(name, kcal, proteinG, fatG, carbsG, sugarG, fibreG, cholesterolMg);

        if (sodiumMg < 0) {
            throw new IllegalArgumentException();
        }
        if (potassiumMg < 0) {
            throw new IllegalArgumentException();
        }
        if (calciumMg < 0) {
            throw new IllegalArgumentException();
        }
        if (magnesiumMg < 0) {
            throw new IllegalArgumentException();
        }
        if (phosphorusMg < 0) {
            throw new IllegalArgumentException();
        }
        if (ironMg < 0) {
            throw new IllegalArgumentException();
        }
        if (copperMg < 0) {
            throw new IllegalArgumentException();
        }
        if (zincMg < 0) {
            throw new IllegalArgumentException();
        }
        if (chlorideMg < 0) {
            throw new IllegalArgumentException();
        }
        if (seleniumUg < 0) {
            throw new IllegalArgumentException();
        }
        if (iodineUg < 0) {
            throw new IllegalArgumentException();
        }
        if (vitAUg < 0) {
            throw new IllegalArgumentException();
        }
        if (vitDUg < 0) {
            throw new IllegalArgumentException();
        }
        if (thiaminMg < 0) {
            throw new IllegalArgumentException();
        }
        if (riboflavinMg < 0) {
            throw new IllegalArgumentException();
        }
        if (niacinMg < 0) {
            throw new IllegalArgumentException();
        }
        if (vitB6Mg < 0) {
            throw new IllegalArgumentException();
        }
        if (vitB12Ug < 0) {
            throw new IllegalArgumentException();
        }
        if (folateUg < 0) {
            throw new IllegalArgumentException();
        }
        if (vitCMg < 0) {
            throw new IllegalArgumentException();
        }
    }
}
