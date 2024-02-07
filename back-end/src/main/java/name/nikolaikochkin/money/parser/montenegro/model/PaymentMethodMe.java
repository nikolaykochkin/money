package name.nikolaikochkin.money.parser.montenegro.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;


@JsonIgnoreProperties(ignoreUnknown = true)
public record PaymentMethodMe(Long id, String type, BigDecimal amount, String typeCode) {
}