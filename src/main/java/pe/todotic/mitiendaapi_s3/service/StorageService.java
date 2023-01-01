package pe.todotic.mitiendaapi_s3.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface StorageService {
    void init();
    String store(MultipartFile file);
    Resource loadAsResource(String filename);
    void delete(String filename);
}
