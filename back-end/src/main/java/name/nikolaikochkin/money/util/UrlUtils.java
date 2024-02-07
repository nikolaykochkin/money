package name.nikolaikochkin.money.util;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public final class UrlUtils {
    private static final String QUERY_STRING_DELIMITER = "\\?";
    private static final String PARAM_PAIR_DELIMITER = "&";
    private static final String KEY_VALUE_DELIMITER = "=";

    private UrlUtils() {
    }

    public static Map<String, String> urlStringToQueryParamsMap(String url) {
        return Optional.of(url.split(QUERY_STRING_DELIMITER))
                .filter(arr -> arr.length == 2)
                .map(arr -> arr[1].split(PARAM_PAIR_DELIMITER))
                .stream()
                .flatMap(Arrays::stream)
                .map(s -> s.split(KEY_VALUE_DELIMITER))
                .collect(Collectors.toMap(keyValue -> urlDecode(keyValue[0]), keyValue -> urlDecode(keyValue[1])));
    }

    public static String urlDecode(String url) {
        return URLDecoder.decode(url, StandardCharsets.UTF_8);
    }
}