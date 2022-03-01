package application.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TitleText implements Iterable<TextLine> {

    public final List<TextLine> lines;

    public TitleText(List<TextLine> lines) {
        this.lines = lines;
    }

    public static TitleText parse(String text) {
        var lines = new ArrayList<TextLine>();
        for (var line : text.split("\\r?\\n")) {
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