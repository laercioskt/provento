package br.com.laercioskt.backend.service;

import br.com.laercioskt.backend.data.Parameter;
import br.com.laercioskt.backend.repository.ParameterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ParameterService {

    @Autowired
    ParameterRepository parameterRepository;

    public Parameter save(String name, String description, LocalDateTime value) {
        Parameter parameter = new Parameter.ParameterBuilder()
                .withName(name)
                .withDescription(description)
                .withValue(value)
                .build();
        return parameterRepository.save(parameter);
    }

    public Optional<Parameter> findByName(String name) {
        return parameterRepository.findByName(name);
    }
}
