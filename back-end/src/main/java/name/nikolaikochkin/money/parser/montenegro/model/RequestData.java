package name.nikolaikochkin.money.parser.montenegro.model;

import io.smallrye.common.constraint.Assert;
import jakarta.ws.rs.FormParam;

import java.util.Map;
import java.util.Set;

import static name.nikolaikochkin.money.util.UrlUtils.urlStringToQueryParamsMap;

public class RequestData {
    @FormParam("iic")
    public String iic;

    @FormParam("dateTimeCreated")
    public String dateTimeCreated;

    @FormParam("tin")
    public String tin;

    @Override
    public String toString() {
        return "RequestData{" +
                "iic='" + iic + '\'' +
                ", dateTimeCreated='" + dateTimeCreated + '\'' +
                ", tin='" + tin + '\'' +
                '}';
    }

    private static final String IIC = "iic";
    private static final String CRTD = "crtd";
    private static final String TIN = "tin";

    private static final Set<String> PARAMS = Set.of(IIC, CRTD, TIN);

    public static RequestData urlToRequestData(String url) {
        Map<String, String> paramsMap = urlStringToQueryParamsMap(url);
        Assert.assertTrue(paramsMap.keySet().containsAll(PARAMS));
        var result = new RequestData();
        result.iic = paramsMap.get(IIC);
        result.dateTimeCreated = paramsMap.get(CRTD);
        result.tin = paramsMap.get(TIN);
        return result;
    }
}