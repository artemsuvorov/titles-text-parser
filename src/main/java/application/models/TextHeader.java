package application.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.function.Predicate;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public final class TextHeader extends TextLine {

    @JsonIgnore
    private final static char headerMarkdownSymbol = '#';

    @JsonProperty
    private final int level;

    @JsonProperty
    private boolean isHeader = true; // serialize only

    private TextHeader(String text, int level) {
        super(text);
        this.level = level;
    }

    public static TextHeader fromLine(String line) throws IllegalArgumentException {
        var level = countWhile(line, c -> c == headerMarkdownSymbol);
        if (level <= 0)
            throw new IllegalArgumentException("Header line must start with 1 or more '" + headerMarkdownSymbol + "' symbols.");
        var text = line.substring(level);
        return new TextHeader(text, level);
    }

    private static int countWhile(String str, Predicate<Character> predicate) {
        int index = 0, count = 0; // actually they store the same value all the time
        while (predicate.test(str.charAt(index))) {
            count++;
            index++;
        }
        return count;
    }

    public int level() {
        return level;
    }

    @Override
    public String toString() {
        return "TextHeader level: {" + level() + "} {" + text() + "}";
    }

}