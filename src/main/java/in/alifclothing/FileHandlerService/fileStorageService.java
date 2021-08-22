package in.alifclothing.FileHandlerService;

import ch.qos.logback.core.net.SyslogOutputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class fileStorageService {

    private final Path fileStoragepath;
    private final String fileStorageLocation;

    public fileStorageService(@Value("${file.storage.location:temp}") String fileStorageLocation) {
        this.fileStorageLocation = fileStorageLocation;
        fileStoragepath = Paths.get(fileStorageLocation).toAbsolutePath().normalize();
        try {
            Files.createDirectories(fileStoragepath);
        } catch (IOException e) {
            throw new RuntimeException("Error creating file path");
        }
    }

    public String storeFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        Path filePath = Paths.get(fileStoragepath+"/"+fileName);
        try {
            Files.copy(file.getInputStream(),filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Error in storing files");
        }
        return fileName;
    }

    public  String deleteFile(String filename){

        try {
            File file = new File(fileStorageLocation+"/"+filename);
            if(file.delete()){
                return file.getName()+" is deleted";
            }else{
                return "File Not deleted";
            }
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return "";
    }

    public Resource downloadFile(String fileName) {
        Path path = Paths.get(fileStorageLocation).toAbsolutePath().resolve(fileName);
        Resource resource;
        try {
            resource =  new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error in reading image");
        }

        if(resource.exists() && resource.isReadable()){
            return resource;
        }else{
            throw new RuntimeException("Error in reading image or the file does not exists");
        }
    }
}
