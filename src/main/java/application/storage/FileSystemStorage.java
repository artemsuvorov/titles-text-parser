package application.storage;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

@Service
public class FileSystemStorage implements Storage {

    private final String uploadDirectoryName = "upload";
    private final Path directoryLocation = Paths.get(uploadDirectoryName);

    @Override
    public void init() {
        try {
            Files.createDirectories(directoryLocation);
        }
        catch (IOException ex) {
            throw new StorageException("Could not initialize storage", ex);
        }
    }

    @Override
    public void store(MultipartFile file, String filename) throws StorageException {
        if (file == null || file.isEmpty())
            throw new StorageException("Failed to store empty file.");
        try {
            var target = getTargetPath(filename);
            Files.createDirectories(target.getParent());
            try (var inputStream = file.getInputStream()) {
                Files.copy(inputStream, target, StandardCopyOption.REPLACE_EXISTING);
            }
        }
        catch (IOException ex) {
            throw new StorageException("Failed to store file.", ex);
        }
    }

    @Override
    public Stream<Path> files() throws StorageException {
        try {
            var dir = directoryLocation;
            return Files.walk(dir, 1).filter(path -> !path.equals(dir)).map(dir::relativize);
        } catch (IOException ex) {
            throw new StorageException("Failed to read stored files", ex);
        }
    }

    @Override
    public Resource load(String filename) throws StorageException {
        try {
            var file = getTargetPath(filename);
            var resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable())
                return resource;
            else
                throw new StorageException("Could not find or read file: " + filename);
        }
        catch (MalformedURLException ex) {
            throw new StorageException("Could not find or read file: " + filename, ex);
        }
    }

    @Override
    public void clear() throws StorageException {
        try {
            FileSystemUtils.deleteRecursively(directoryLocation);
        } catch (IOException ex) {
            throw new StorageException("Could not delete files", ex);
        }
    }

    private Path getTargetPath(String filename) {
        return directoryLocation.resolve(filename).normalize().toAbsolutePath();
    }

}