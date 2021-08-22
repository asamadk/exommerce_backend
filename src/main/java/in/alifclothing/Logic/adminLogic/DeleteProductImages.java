package in.alifclothing.Logic.adminLogic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import in.alifclothing.FileHandlerService.fileStorageService;

@Service
public class DeleteProductImages {
    @Autowired
    private fileStorageService fss;

    public void deleteProductImages(String img1,String img2, String img3, String img4){
        boolean flag = false;
        try {
            int i = img1.length()-1;
            while(i >= 0 ){
                if(img1.charAt(i) == '/')break;
                i--;
            }
            fss.deleteFile(img1.substring(i+1));

            i = img2.length()-1;
            while(i >= 0){
                if(img2.charAt(i) == '/')break;
                i--;
            }
            fss.deleteFile(img2.substring(i+1));

            i = img3.length()-1;
            while (i >= 0){
                if(img3.charAt(i) == '/')break;
                i--;
            }
            fss.deleteFile(img3.substring(i+1));

            i = img4.length()-1;
            while (i >= 0){
                if(img4.charAt(i) == '/')break;
                i--;
            }
            fss.deleteFile(img4.substring(i+1));

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
