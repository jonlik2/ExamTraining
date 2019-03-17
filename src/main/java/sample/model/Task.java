package sample.model;

import java.util.List;

public class Task {

    private int number;
    private List<Variant> variants;
    private String theory;

    public Task(int number, List<Variant> variants, String theory) {
        this.number = number;
        this.variants = variants;
        this.theory = theory;
    }

    public int getNumber() {
        return number;
    }

    public List<Variant> getVariants() {
        return variants;
    }

    public String getTheory() {
        return theory;
    }
}
