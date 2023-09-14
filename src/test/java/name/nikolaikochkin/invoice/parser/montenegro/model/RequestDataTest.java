package name.nikolaikochkin.invoice.parser.montenegro.model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RequestDataTest {


    @ParameterizedTest
    @CsvFileSource(resources = "/montenegro-request-data-from-url.csv", numLinesToSkip = 1)
    void fromUrl(String url, String iic, String crdt, String tin) {
        var requestData = RequestData.fromUrl(url);

        assertTrue(requestData.isPresent());

        var data = requestData.get();
        assertEquals(iic, data.iic);
        assertEquals(crdt, data.dateTimeCreated);
        assertEquals(tin, data.tin);
    }
}