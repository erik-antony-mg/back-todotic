package pe.todotic.mitiendaapi_s3.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pe.todotic.mitiendaapi_s3.service.StorageService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/media")
public class MediaController {
    @Autowired
    private StorageService storageService;
    // si solo queremos pdf o png o jp se colocara asi filename:.pdf
    @GetMapping("/{filename:.+}")
    ResponseEntity<Resource> getResource(@PathVariable String filename) throws IOException {
      Resource response=   storageService.loadAsResource(filename);
      String contenType= Files.probeContentType(response.getFile().toPath());
      return ResponseEntity
              .ok()
              .header("Content-Type",contenType)
              .body(response);
    }

    @PostMapping("/upload")
    Map<String,String>  upload(@RequestParam MultipartFile file){
        String ruta= storageService.store(file);
        Map<String,String> response=new HashMap<>();
        response.put("ruta",ruta);
        return response;
    }

}
