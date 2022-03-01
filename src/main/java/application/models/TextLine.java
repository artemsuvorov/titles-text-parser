package application.models;

public class TextLine {

    private final String text;

    public TextLine(String text) {
        this.text = text.trim();
    }

    public String text() {
        return text;
    }

    public boolean isEmpty() {
        return text().isBlank();
    }

    @Override
    public String toString() {
        return "TextLine: {" + text() + "}";
    }

}