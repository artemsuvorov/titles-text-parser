package application.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public class TextLine {

    @JsonProperty
    private final String text;

    @JsonProperty
    private boolean isHeader = false; // serialize only

    public TextLine(String text) {
        this.text = text.trim();
    }

    @JsonIgnore
    public String text() {
        return text;
    }

    @JsonIgnore
    public boolean isBlank() {
        return text().isBlank();
    }

    @Override
    public String toString() {
        return "TextLine: {" + text() + "}";
    }

}