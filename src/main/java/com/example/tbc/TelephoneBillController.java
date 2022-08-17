package com.example.tbc;

import com.example.tbc.billing.TelephoneBillCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
public class TelephoneBillController {

    private TelephoneBillCalculator telephoneBillCalculator;

    @Autowired
    public void setTelephoneBillCalculator(TelephoneBillCalculator telephoneBillCalculator) {
        this.telephoneBillCalculator = telephoneBillCalculator;
    }

    @GetMapping("/")
    public String index() {
        String phoneLog = loadLog("/phoneLog.csv");

        BigDecimal calculated = telephoneBillCalculator.calculate(phoneLog);


        StringBuilder result = new StringBuilder();
        result.append("<pre>");
        result.append("Greetings from Telephone Bill Controller!\n");
        result.append("Calculated Amount: ").append(calculated).append("\n");
        result.append("</pre>");

        return result.toString();
    }

    private String loadLog(String phoneLog) {
        try {
            return Files.readString(Paths.get(getClass().getResource(phoneLog).toURI()),
                    StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
