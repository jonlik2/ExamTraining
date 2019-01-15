package sample.model;

import java.util.List;

public class Task {

    private int number;
    private List<Variant> variants;

    public Task(int number, List<Variant> variants) {
        this.number = number;
        this.variants = variants;
    }

    public int getNumber() {
        return number;
    }

    public List<Variant> getVariants() {
        return variants;
    }
}
