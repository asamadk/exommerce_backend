package in.alifclothing.model;

import javax.persistence.*;

@Entity
@Table(name = "companyContact")
public class CompanyContactModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int company_contactId;
    private String comapny_contact_number;
    private String company_email;
    private String company_address;
    private String company_city;
    private String company_state;

    public CompanyContactModel(String comapny_contact_number, String company_email, String company_address, String company_city, String company_state) {
        this.comapny_contact_number = comapny_contact_number;
        this.company_email = company_email;
        this.company_address = company_address;
        this.company_city = company_city;
        this.company_state = company_state;
    }

    public CompanyContactModel() {
    }

    public int getCompany_contactId() {
        return company_contactId;
    }

    public void setCompany_contactId(int company_contactId) {
        this.company_contactId = company_contactId;
    }

    public String getComapny_contact_number() {
        return comapny_contact_number;
    }

    public void setComapny_contact_number(String comapny_contact_number) {
        this.comapny_contact_number = comapny_contact_number;
    }

    public String getCompany_email() {
        return company_email;
    }

    public void setCompany_email(String company_email) {
        this.company_email = company_email;
    }

    public String getCompany_address() {
        return company_address;
    }

    public void setCompany_address(String company_address) {
        this.company_address = company_address;
    }

    public String getCompany_city() {
        return company_city;
    }

    public void setCompany_city(String company_city) {
        this.company_city = company_city;
    }

    public String getCompany_state() {
        return company_state;
    }

    public void setCompany_state(String company_state) {
        this.company_state = company_state;
    }
}
