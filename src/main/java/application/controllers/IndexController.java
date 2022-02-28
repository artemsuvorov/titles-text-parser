package application.controllers;

import application.storage.Storage;

import application.storage.StorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.stream.Collectors;

@Controller
public class IndexController {

    private final Storage storage;

    @Autowired
    public IndexController(Storage storage) {
        this.storage = storage;
    }

    @GetMapping("/")
    public String listUploadedFiles(Model model) {
        try {
            var filenames = storage.files()
                    .map(path -> path.getFileName().toString())
                    .collect(Collectors.toList());
            if (!filenames.isEmpty())
                model.addAttribute("filenames", filenames);
        } catch (StorageException ex) {
            var status = "Failed to list all the stored files  => Error message: " + ex.getMessage();
            model.addAttribute("status", status);
        }
        return "index";
    }

}