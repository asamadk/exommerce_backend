package in.alifclothing.model;

import javax.persistence.*;

@Entity
@Table(name = "banner")
public class BannerModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int banner_id;
    private String bannerimg1;
    private String bannerimg2;
    private String bannerimg3;
    private String bannerimg4;
    private String bannerimg5;
    private String bannerimg6;
    private String bannerimg7;

    public BannerModel(String bannerimg1, String bannerimg2, String bannerimg3, String bannerimg4, String bannerimg5, String bannerimg6, String bannerimg7) {
        this.bannerimg1 = bannerimg1;
        this.bannerimg2 = bannerimg2;
        this.bannerimg3 = bannerimg3;
        this.bannerimg4 = bannerimg4;
        this.bannerimg5 = bannerimg5;
        this.bannerimg6 = bannerimg6;
        this.bannerimg7 = bannerimg7;
    }

    public BannerModel() {
    }

    public int getBanner_id() {
        return banner_id;
    }

    public void setBanner_id(int banner_id) {
        this.banner_id = banner_id;
    }

    public String getBannerimg1() {
        return bannerimg1;
    }

    public void setBannerimg1(String bannerimg1) {
        this.bannerimg1 = bannerimg1;
    }

    public String getBannerimg2() {
        return bannerimg2;
    }

    public void setBannerimg2(String bannerimg2) {
        this.bannerimg2 = bannerimg2;
    }

    public String getBannerimg3() {
        return bannerimg3;
    }

    public void setBannerimg3(String bannerimg3) {
        this.bannerimg3 = bannerimg3;
    }

    public String getBannerimg4() {
        return bannerimg4;
    }

    public void setBannerimg4(String bannerimg4) {
        this.bannerimg4 = bannerimg4;
    }

    public String getBannerimg5() {
        return bannerimg5;
    }

    public void setBannerimg5(String bannerimg5) {
        this.bannerimg5 = bannerimg5;
    }

    public String getBannerimg6() {
        return bannerimg6;
    }

    public void setBannerimg6(String bannerimg6) {
        this.bannerimg6 = bannerimg6;
    }

    public String getBannerimg7() {
        return bannerimg7;
    }

    public void setBannerimg7(String bannerimg7) {
        this.bannerimg7 = bannerimg7;
    }
}
