package sample.model;

public class Variant {

    private int number;
    private String question;
    private String answer;

    public Variant(int number, String question, String answer) {
        this.number = number;
        this.question = question;
        this.answer = answer;
    }

    public int getNumber() {
        return number;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }
}
