package br.com.clearinvest.clivserver.service;

import static java.nio.file.Files.copy;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.util.Objects.requireNonNull;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.util.StringUtils.cleanPath;

import br.com.clearinvest.clivserver.config.FileStorageProperties;
import br.com.clearinvest.clivserver.web.rest.errors.FileNotFoundException;
import br.com.clearinvest.clivserver.web.rest.errors.FileStorageException;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {

    private final Logger log = getLogger(FileStorageService.class);

    private final Path fileStorageLocation;

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public String storeFile(MultipartFile file, String dir) {
        // Normalize file name
        String fileName = cleanPath(requireNonNull(file.getOriginalFilename()));
        try {
            createDirectoryIfNecessary(dir);
            copy(file.getInputStream(), this.fileStorageLocation.resolve(dir + fileName), REPLACE_EXISTING);
            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    private void createDirectoryIfNecessary(String dir) {
        File file = this.fileStorageLocation.resolve(dir).toFile();
        if (!file.exists()) {
            if (file.mkdirs()) {
                log.info("Directory {} is created!", file.getAbsolutePath());
            } else {
                log.error("Failed to create directory {}!", file.getAbsolutePath());
            }
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new FileNotFoundException("File not found " + fileName, ex);
        }
    }
}
