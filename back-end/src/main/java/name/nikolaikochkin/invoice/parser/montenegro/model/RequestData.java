package name.nikolaikochkin.invoice.parser.montenegro.model;

import jakarta.ws.rs.FormParam;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class RequestData {
    private static final String IIC = "iic";
    private static final String CRTD = "crtd";
    private static final String TIN = "tin";
    private static final Set<String> PARAMS = Set.of(IIC, CRTD, TIN);

    @FormParam("iic")
    public String iic;
    @FormParam("dateTimeCreated")
    public String dateTimeCreated;
    @FormParam("tin")
    public String tin;

    public static Optional<RequestData> fromUrl(String url) {
        var paramsMap = urlToQueryParamsMap(url);

        if (paramsMap.isEmpty()
                || !paramsMap.keySet().containsAll(PARAMS)) {
            return Optional.empty();
        }

        var data = new RequestData();

        data.iic = paramsMap.get(IIC);
        data.dateTimeCreated = paramsMap.get(CRTD);
        data.tin = paramsMap.get(TIN);

        return Optional.of(data);
    }

    private static Map<String, String> urlToQueryParamsMap(String url) {
        String[] split = url.split("\\?");
        if (split.length != 2) {
            return Collections.emptyMap();
        }
        return URLEncodedUtils.parse(split[1], StandardCharsets.UTF_8)
                .stream()
                .collect(Collectors.toMap(NameValuePair::getName, NameValuePair::getValue));
    }
}