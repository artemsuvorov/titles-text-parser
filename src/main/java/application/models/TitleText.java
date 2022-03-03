package application.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public class TitleText implements Iterable<TextLine> {

    // supports CR, LF, and CRLF
    @JsonIgnore
    private static final String SPLIT_PATTERN = "(\r|\n|\r\n)";

    @JsonProperty
    public final List<TextLine> lines;

    public TitleText(List<TextLine> lines) {
        this.lines = lines;
    }

    public static TitleText parse(String text) {
        var lines = new ArrayList<TextLine>();
        for (var line : text.split(SPLIT_PATTERN)) {
            if (line.startsWith("#"))
                lines.add(TextHeader.fromLine(line));
            else
                lines.add(new TextLine(line));
        }
        return new TitleText(lines);
    }

    public boolean hasHeaders() {
        return lines.stream().anyMatch(line -> line instanceof TextHeader);
    }

    public Iterable<TextLine> lines() {
        return lines;
    }

    @Override
    public Iterator<TextLine> iterator() {
        return lines.iterator();
    }

}