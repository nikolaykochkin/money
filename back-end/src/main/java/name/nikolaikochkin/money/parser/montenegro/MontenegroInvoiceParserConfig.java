package name.nikolaikochkin.money.parser.montenegro;

import io.smallrye.config.ConfigMapping;

import java.util.Currency;

@ConfigMapping(prefix = "money.parser.montenegro")
public interface MontenegroInvoiceParserConfig {
    String url();
    Currency currency();
}