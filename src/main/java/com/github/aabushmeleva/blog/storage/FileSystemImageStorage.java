package com.github.aabushmeleva.blog.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Component
public class FileSystemImageStorage implements ImageStorage {

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png", "gif", "webp");

    private final Path rootDir;

    public FileSystemImageStorage(@Value("${images.dir}") String imagesDir) throws IOException {
        this.rootDir = Path.of(imagesDir).toAbsolutePath().normalize();
        Files.createDirectories(rootDir);
    }

    @Override
    public String save(InputStream content, String originalFilename) {
        String imageName = UUID.randomUUID() + extensionOf(originalFilename);
        try {
            Files.copy(content, rootDir.resolve(imageName));
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to save image " + imageName, e);
        }
        return imageName;
    }

    @Override
    public void delete(String imageName) {
        try {
            Files.deleteIfExists(rootDir.resolve(imageName));
        } catch (IOException e) {
            log.warn("Failed to delete image {}", imageName, e);
        }
    }

    @Override
    public byte[] load(String imageName) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private static String extensionOf(String filename) {
        String ext = StringUtils.getFilenameExtension(filename);
        if (ext == null) {
            return "";
        }
        ext = ext.toLowerCase(Locale.ROOT);
        return ALLOWED_EXTENSIONS.contains(ext) ? "." + ext : "";
    }
}
