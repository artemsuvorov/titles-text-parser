package application.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface Storage {

    void init();

    void store(MultipartFile file, String filename) throws StorageException;

    Stream<Path> files() throws StorageException;

    Resource load(String filename) throws StorageException;

    void clear() throws StorageException;

}