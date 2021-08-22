package in.alifclothing.Controllers.Home;

import in.alifclothing.Logic.homeLogic.homeLogic;
import in.alifclothing.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@RestController
public class homeController {

    @Autowired
    private homeLogic homeLogic;
    @Autowired
    private in.alifclothing.FileHandlerService.fileStorageService fileStorageService;

    public homeController(in.alifclothing.FileHandlerService.fileStorageService fileStorageService){
        this.fileStorageService = fileStorageService;
    }

    @PostMapping("/register")
    public UserModel addUser(@RequestBody UserModel user){
        return homeLogic.persistUser(user);
    }

    @GetMapping("/download/{fileName}")
    ResponseEntity<Resource> downloadimage(@PathVariable String fileName, HttpServletRequest request){
        Resource resource = fileStorageService.downloadFile(fileName);
        String mimeType;
        try {
            mimeType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(mimeType))
//                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment;fileName="+resource.getFilename())
                .header(HttpHeaders.CONTENT_DISPOSITION,"inline;fileName="+resource.getFilename()) //to render in browser insetead of downloading
                .body(resource);
    }

    @GetMapping("/getAllUsers")
    public List<UserModel> get(){
        return homeLogic.get();
    }





}
