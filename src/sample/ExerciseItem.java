package sample;

public class ExerciseItem {
    private String name;
    private int burn_rate;

    public String getName() { return name; }
    public int getBurn_rate() { return burn_rate; }

    public ExerciseItem(String name, int burn_rate) {
        this.name = name;
        this.burn_rate = burn_rate;
    }

    public ExerciseItem() {
        this.name = "Default";
        this.burn_rate = 0;
    }

    public double calculateBurn(int minutes) { return this.burn_rate*minutes; }

}

