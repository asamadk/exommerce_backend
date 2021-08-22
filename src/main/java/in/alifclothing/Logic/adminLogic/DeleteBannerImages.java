package in.alifclothing.Logic.adminLogic;

import in.alifclothing.FileHandlerService.fileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeleteBannerImages {

    @Autowired
    private fileStorageService fss;

    public boolean deleteAllBanners(List<String> bannerImages){
        bannerImages.forEach(image -> {
            try{
                int i = image.length()-1;
                while(i >= 0){
                    if(image.charAt(i) == '/')break;
                    i--;
                }
                fss.deleteFile(image.substring(i+1));
            }catch (Exception e){
                e.printStackTrace();
            }
        });
        return true;
    }
}
