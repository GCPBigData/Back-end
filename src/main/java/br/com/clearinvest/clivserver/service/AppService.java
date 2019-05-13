package br.com.clearinvest.clivserver.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
@Transactional
public class AppService {

    private final Logger log = LoggerFactory.getLogger(AppService.class);

    private final Environment environment;

    public AppService(Environment environment) {
        this.environment = environment;
    }

    public boolean isEnvironmentProd() {
        return Arrays.asList(environment.getActiveProfiles()).contains("prod");
    }

    public boolean isEnvironmentDev() {
        return Arrays.asList(environment.getActiveProfiles()).contains("dev");
    }
}
