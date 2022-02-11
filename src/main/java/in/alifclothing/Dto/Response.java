package in.alifclothing.Dto;

import java.util.List;
import java.util.Map;

public class Response <T>{
    List<T> responseWrapper;
    Map<String,String > errorMap;
    String responseCode;
    String responseDesc;

    public List<T> getResponseWrapper() {
        return responseWrapper;
    }

    public void setResponseWrapper(List<T> responseWrapper) {
        this.responseWrapper = responseWrapper;
    }

    public Map<String, String> getErrorMap() {
        return errorMap;
    }

    public void setErrorMap(Map<String, String> errorMap) {
        this.errorMap = errorMap;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseDesc() {
        return responseDesc;
    }

    public void setResponseDesc(String responseDesc) {
        this.responseDesc = responseDesc;
    }

    @Override
    public String toString() {
        return "Response{" +
                "responseWrapper=" + responseWrapper +
                ", errorMap=" + errorMap +
                ", responseCode='" + responseCode + '\'' +
                ", responseDesc='" + responseDesc + '\'' +
                '}';
    }
}
