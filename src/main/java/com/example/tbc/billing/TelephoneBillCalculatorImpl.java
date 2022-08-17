package com.example.tbc.billing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class TelephoneBillCalculatorImpl implements TelephoneBillCalculator {

    private static Logger logger = LoggerFactory.getLogger(TelephoneBillCalculatorImpl.class);

    /**
     * Výstupem metody je částka k uhrazení spočtena dle vstupního výpisu dle následujících pravidel:
     *
     * - Minutová sazba v intervalu <8:00:00,16:00:00) je zpoplatněna 1 Kč za každou započatou minutu.
     *   Mimo uvedený interval platí snížená sazba 0,50 Kč za každou započatou minutu. Pro každou minutu
     *   hovoru je pro stanovení sazby určující čas započetí dané minuty.
     *
     * - Pro hovory delší, než pět minut platí pro každou další započatou minutu nad rámec prvních
     *   pěti minut snížená sazba 0,20 Kč bez ohledu na dobu kdy telefonní hovor probíhá.
     *
     * - V rámci promo akce operátora dále platí, že hovory na nejčastěji volané číslo v rámci výpisu
     *   nebudou zpoplatněny. V případě, že by výpis obsahoval dvě nebo více takových čísel, zpoplatněny
     *   nebudou hovory na číslo s aritmeticky nejvyšší hodnotou.
     *
     * @param phoneLog CSV formatted input file
     * @return
     */
    @Override
    public BigDecimal calculate(String phoneLog) {
        logger.info(phoneLog);
        List<PhoneLogLine> logs = createLogs(phoneLog);
        logger.info(List.of(logs).toString());

        BigDecimal price = BigDecimal.ZERO;
        for(PhoneLogLine log : logs) {

            LocalDateTime currentTime = log.getStartTime();
            while(currentTime.isBefore(log.getEndTime())) {
                price = price.add(calculateMinute(currentTime));
                currentTime = currentTime.plusMinutes(1);
            }
        }

        return price;
    }

    private BigDecimal calculateMinute(LocalDateTime currentTime) {
        BigDecimal price = BigDecimal.ZERO;
        if(currentTime.getHour() >= 8 && currentTime.getHour() <= 16) {
            price = new BigDecimal("1");
        } else {
            price = new BigDecimal("0.5");
        }
        return price;
    }

    private List<PhoneLogLine> createLogs(String phoneLog) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        String[] lines = phoneLog.split("\r?\n|\r");
        List<PhoneLogLine> logs = new ArrayList<>();
        for(String line : lines) {
            if(line.startsWith("#") || line.trim().isEmpty()) {
                continue;
            }
            String[] cols = line.split(",");
            logs.add(new PhoneLogLine(
                    cols[0],
                    LocalDateTime.parse(cols[1], formatter),
                    LocalDateTime.parse(cols[2], formatter)
            ));
        }
        return logs;
    }
}
