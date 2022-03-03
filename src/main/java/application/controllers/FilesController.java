package application.controllers;

import application.models.TitleText;
import application.storage.Storage;
import application.views.PlainTextView;
import application.views.TitleTextView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class FilesController {

    private final Storage storage;

    @Autowired
    public FilesController(Storage storage) {
        this.storage = storage;
    }

    @GetMapping("/files/{filename:.+}")
    public String serveFileContents(@PathVariable String filename) throws IOException {
        var text = readAllText(filename);
        return new PlainTextView(text).buildResponseBody();
    }

    @GetMapping("/files/parsed/{filename:.+}")
    public String serveParsedFile(@PathVariable String filename) throws IOException {
        var text = TitleText.parse(readAllText(filename));
        return new TitleTextView(text).buildResponseBody();
    }

    @GetMapping("/files/parsed/json/{filename:.+}")
    public TitleText serveParsedFileAsJson(@PathVariable String filename) throws IOException {
        var text = readAllText(filename);
        return TitleText.parse(text);
    }

    private String readAllText(String filename) throws IOException {
        var file = storage.load(filename);
        try (var stream = file.getInputStream()) {
            return new String(stream.readAllBytes());
        }
    }

    @ExceptionHandler(IOException.class)
    public @ResponseBody String handleException(IOException ex) {
        return ex.getMessage();
    }

}