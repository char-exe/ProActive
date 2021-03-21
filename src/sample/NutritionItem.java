package sample;

public class NutritionItem {
    private String name;
    private double kcal, protein, fat, carbs, sugar, fibre, cholesterol;

    //get methods
    public String getName() { return name; }
    public double getKcal() { return kcal; }
    public double getProtein() { return protein; }
    public double getFat() { return fat; }
    public double getCarbs() { return carbs; }
    public double getSugar() { return sugar; }
    public double getFibre() { return fibre; }
    public double getCholesterol() { return cholesterol; }

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

}
