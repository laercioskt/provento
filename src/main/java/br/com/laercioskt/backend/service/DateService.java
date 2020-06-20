package br.com.laercioskt.backend.service;

import br.com.laercioskt.backend.data.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static java.time.ZoneId.systemDefault;

@Service
public class DateService {

    private static final String PAR_SYSTEM_DATE = "SystemDate";

    @Autowired
    ParameterService parameterService;

    private LocalDateTime initialSystemDate;

    public LocalDate today() {
        return systemDate().toLocalDate();
    }

    public LocalDateTime systemDate() {
        long actual = LocalDateTime.now().atZone(systemDefault()).toEpochSecond();
        long initial = getInitialSystemDate().atZone(systemDefault()).toEpochSecond();
        return getSystemDatePersistedOrNow().plusSeconds(actual - initial);
    }

    public LocalDateTime getInitialSystemDate() {
        if (initialSystemDate == null)
            initialSystemDate = LocalDateTime.now();
        return initialSystemDate;
    }

    public void setSystemDate(LocalDateTime systemDate) {
        parameterService.save(PAR_SYSTEM_DATE, "system date for tests", systemDate);
        this.initialSystemDate = LocalDateTime.now();
    }

    private LocalDateTime getSystemDatePersistedOrNow() {
        Optional<Parameter> systemDate = parameterService.findByName(PAR_SYSTEM_DATE);
        if (systemDate.isPresent())
            return systemDate.get().getValueLocalDateTime();
        else
            return LocalDateTime.now();
    }

}
