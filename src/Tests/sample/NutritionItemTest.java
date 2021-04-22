package sample;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NutritionItemTest {

    @Test
    void nullName() {
        assertThrows(NullPointerException.class, () -> new NutritionItem(null, 1, 1, 1, 1, 1, 1, 1));
    }

    @Test
    void emptyName() {
        assertThrows(IllegalArgumentException.class, () -> new NutritionItem("", 1, 1, 1, 1, 1, 1, 1));
    }

    @Test
    void negativeKcal() {
        assertThrows(IllegalArgumentException.class, () -> new NutritionItem("Bread", -1, 1, 1, 1, 1, 1, 1));
    }

    @Test
    void negativeProtein() {
        assertThrows(IllegalArgumentException.class, () -> new NutritionItem("Bread", 1, -1, 1, 1, 1, 1, 1));
    }

    @Test
    void negativeFat() {
        assertThrows(IllegalArgumentException.class, () -> new NutritionItem("Bread", 1, 1, -1, 1, 1, 1, 1));
    }

    @Test
    void negativeCarbs() {
        assertThrows(IllegalArgumentException.class, () -> new NutritionItem("Bread", 1, 1, 1, -1, 1, 1, 1));
    }

    @Test
    void negativeSugar() {
        assertThrows(IllegalArgumentException.class, () -> new NutritionItem("Bread", 1, 1, 1, 1, -1, 1, 1));
    }

    @Test
    void negativeFibre() {
        assertThrows(IllegalArgumentException.class, () -> new NutritionItem("Bread", 1, 1, 1, 1, 1, -1, 1));
    }

    @Test
    void negativeCholesterol() {
        assertThrows(IllegalArgumentException.class, () -> new NutritionItem("Bread", 1, 1, 1, 1, 1, 1, -1));
    }

    @Test
    void successfulConstruction() {
        assertDoesNotThrow(() -> new NutritionItem("Bread", 1, 1, 1, 1, 1, 1, 1));
    }

    @Test
    void getName() {
        NutritionItem bread = new NutritionItem("Bread", 1, 1, 1, 1, 1, 1, 1);

        assertEquals("Bread", bread.getName());
    }

    @Test
    void getKcal() {
        NutritionItem bread = new NutritionItem("Bread", 1, 1, 1, 1, 1, 1, 1);

        assertEquals(1, bread.getKcal());
    }

    @Test
    void getProtein() {
        NutritionItem bread = new NutritionItem("Bread", 1, 1, 1, 1, 1, 1, 1);

        assertEquals(1, bread.getProtein());
    }

    @Test
    void getFat() {
        NutritionItem bread = new NutritionItem("Bread", 1, 1, 1, 1, 1, 1, 1);

        assertEquals(1, bread.getFat());
    }

    @Test
    void getCarbs() {
        NutritionItem bread = new NutritionItem("Bread", 1, 1, 1, 1, 1, 1, 1);

        assertEquals(1, bread.getCarbs());
    }

    @Test
    void getSugar() {
        NutritionItem bread = new NutritionItem("Bread", 1, 1, 1, 1, 1, 1, 1);

        assertEquals(1, bread.getSugar());
    }

    @Test
    void getFibre() {
        NutritionItem bread = new NutritionItem("Bread", 1, 1, 1, 1, 1, 1, 1);

        assertEquals(1, bread.getFibre());
    }

    @Test
    void getCholesterol() {
        NutritionItem bread = new NutritionItem("Bread", 1, 1, 1, 1, 1, 1, 1);

        assertEquals(1, bread.getCholesterol());
    }
}