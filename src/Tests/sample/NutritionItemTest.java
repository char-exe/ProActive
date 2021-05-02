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
    void negativeSodium() {
        assertThrows(IllegalArgumentException.class, () -> new NutritionItem(
                "Bread", 1, 1, 1, 1, 1, 1, 1,
                -1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1
        ));
    }

    @Test
    void negativePotassium() {
        assertThrows(IllegalArgumentException.class, () -> new NutritionItem(
                "Bread", 1, 1, 1, 1, 1, 1, 1,
                1, -1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1
        ));
    }

    @Test
    void negativeCalcium() {
        assertThrows(IllegalArgumentException.class, () -> new NutritionItem(
                "Bread", 1, 1, 1, 1, 1, 1, 1,
                1, 1, -1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1
        ));
    }

    @Test
    void negativeMagnesium() {
        assertThrows(IllegalArgumentException.class, () -> new NutritionItem(
                "Bread", 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, -1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1
        ));
    }

    @Test
    void negativePhosphorus() {
        assertThrows(IllegalArgumentException.class, () -> new NutritionItem(
                "Bread", 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, -1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1
        ));
    }

    @Test
    void negativeIron() {
        assertThrows(IllegalArgumentException.class, () -> new NutritionItem(
                "Bread", 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, -1, 1,
                1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1
        ));
    }

    @Test
    void negativeCopper() {
        assertThrows(IllegalArgumentException.class, () -> new NutritionItem(
                "Bread", 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, -1,
                1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1
        ));
    }

    @Test
    void negativeZinc() {
        assertThrows(IllegalArgumentException.class, () -> new NutritionItem(
                "Bread", 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1,
                -1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1
        ));
    }

    @Test
    void negativeChloride() {
        assertThrows(IllegalArgumentException.class, () -> new NutritionItem(
                "Bread", 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1,
                1, -1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1
        ));
    }

    @Test
    void negativeSelenium() {
        assertThrows(IllegalArgumentException.class, () -> new NutritionItem(
                "Bread", 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1,
                1, 1, -1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1
        ));
    }

    @Test
    void negativeIodine() {
        assertThrows(IllegalArgumentException.class, () -> new NutritionItem(
                "Bread", 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, -1, 1, 1, 1, 1,
                1, 1, 1, 1, 1
        ));
    }

    @Test
    void negativeVitA() {
        assertThrows(IllegalArgumentException.class, () -> new NutritionItem(
                "Bread", 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, -1, 1, 1, 1,
                1, 1, 1, 1, 1
        ));
    }

    @Test
    void negativeVitD() {
        assertThrows(IllegalArgumentException.class, () -> new NutritionItem(
                "Bread", 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, -1, 1, 1,
                1, 1, 1, 1, 1
        ));
    }

    @Test
    void negativeThiamin() {
        assertThrows(IllegalArgumentException.class, () -> new NutritionItem(
                "Bread", 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, -1, 1,
                1, 1, 1, 1, 1
        ));
    }

    @Test
    void negativeRiboflavin() {
        assertThrows(IllegalArgumentException.class, () -> new NutritionItem(
                "Bread", 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, -1,
                1, 1, 1, 1, 1
        ));
    }

    @Test
    void negativeNiacin() {
        assertThrows(IllegalArgumentException.class, () -> new NutritionItem(
                "Bread", 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1,
                -1, 1, 1, 1, 1
        ));
    }

    @Test
    void negativeVitB6() {
        assertThrows(IllegalArgumentException.class, () -> new NutritionItem(
                "Bread", 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1,
                1, -1, 1, 1, 1
        ));
    }

    @Test
    void negativeVitB12() {
        assertThrows(IllegalArgumentException.class, () -> new NutritionItem(
                "Bread", 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, -1, 1, 1
        ));
    }

    @Test
    void negativeFolate() {
        assertThrows(IllegalArgumentException.class, () -> new NutritionItem(
                "Bread", 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, -1, 1
        ));
    }

    @Test
    void negativeVitC() {
        assertThrows(IllegalArgumentException.class, () -> new NutritionItem(
                "Bread", 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, -1
        ));
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

        assertEquals(1, bread.getProteinG());
    }

    @Test
    void getFat() {
        NutritionItem bread = new NutritionItem("Bread", 1, 1, 1, 1, 1, 1, 1);

        assertEquals(1, bread.getFatG());
    }

    @Test
    void getCarbs() {
        NutritionItem bread = new NutritionItem("Bread", 1, 1, 1, 1, 1, 1, 1);

        assertEquals(1, bread.getCarbsG());
    }

    @Test
    void getSugar() {
        NutritionItem bread = new NutritionItem("Bread", 1, 1, 1, 1, 1, 1, 1);

        assertEquals(1, bread.getSugarG());
    }

    @Test
    void getFibre() {
        NutritionItem bread = new NutritionItem("Bread", 1, 1, 1, 1, 1, 1, 1);

        assertEquals(1, bread.getFibreG());
    }

    @Test
    void getCholesterol() {
        NutritionItem bread = new NutritionItem("Bread", 1, 1, 1, 1, 1, 1, 1);

        assertEquals(1, bread.getCholesterolMg());
    }

    @Test
    void getSodium() {
        NutritionItem bread = new NutritionItem("Bread", 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1);

        assertEquals(1, bread.getSodiumMg());
    }

    @Test
    void getPotassium() {
        NutritionItem bread = new NutritionItem("Bread", 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1);

        assertEquals(1, bread.getPotassiumMg());
    }

    @Test
    void getCalcium() {
        NutritionItem bread = new NutritionItem("Bread", 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1);

        assertEquals(1, bread.getCalciumMg());
    }

    @Test
    void getMagnesium() {
        NutritionItem bread = new NutritionItem("Bread", 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1);

        assertEquals(1, bread.getMagnesiumMg());
    }

    @Test
    void getPhosphorus() {
        NutritionItem bread = new NutritionItem("Bread", 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1);

        assertEquals(1, bread.getPhosphorusMg());
    }

    @Test
    void getIron() {
        NutritionItem bread = new NutritionItem("Bread", 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1);

        assertEquals(1, bread.getIronMg());
    }

    @Test
    void getCopper() {
        NutritionItem bread = new NutritionItem("Bread", 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1);

        assertEquals(1, bread.getCopperMg());
    }

    @Test
    void getZinc() {
        NutritionItem bread = new NutritionItem("Bread", 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1);

        assertEquals(1, bread.getZincMg());
    }

    @Test
    void getChloride() {
        NutritionItem bread = new NutritionItem("Bread", 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1);

        assertEquals(1, bread.getChlorideMg());
    }

    @Test
    void getSelenium() {
        NutritionItem bread = new NutritionItem("Bread", 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1);

        assertEquals(1, bread.getSeleniumUg());
    }

    @Test
    void getIodine() {
        NutritionItem bread = new NutritionItem("Bread", 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1);

        assertEquals(1, bread.getIodineUg());
    }

    @Test
    void getVitA() {
        NutritionItem bread = new NutritionItem("Bread", 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1);

        assertEquals(1, bread.getVitAUg());
    }

    @Test
    void getVitD() {
        NutritionItem bread = new NutritionItem("Bread", 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1);

        assertEquals(1, bread.getVitDUg());
    }

    @Test
    void getThiamin() {
        NutritionItem bread = new NutritionItem("Bread", 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1);

        assertEquals(1, bread.getThiaminMg());
    }

    @Test
    void getRiboflavin() {
        NutritionItem bread = new NutritionItem("Bread", 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1);

        assertEquals(1, bread.getRiboflavinMg());
    }

    @Test
    void getNiacin() {
        NutritionItem bread = new NutritionItem("Bread", 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1);

        assertEquals(1, bread.getNiacinMg());
    }

    @Test
    void getVitB6() {
        NutritionItem bread = new NutritionItem("Bread", 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1);

        assertEquals(1, bread.getVitB6Mg());
    }

    @Test
    void getVitB12() {
        NutritionItem bread = new NutritionItem("Bread", 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1);

        assertEquals(1, bread.getVitB12Ug());
    }

    @Test
    void getFolate() {
        NutritionItem bread = new NutritionItem("Bread", 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1);

        assertEquals(1, bread.getFolateUg());
    }

    @Test
    void getVitC() {
        NutritionItem bread = new NutritionItem("Bread", 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1);

        assertEquals(1, bread.getVitCMg());
    }
}