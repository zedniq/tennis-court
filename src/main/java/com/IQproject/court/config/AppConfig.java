package com.IQproject.court.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for loading custom application properties.
 *
 * @author Vojtech Zednik
 */
@Configuration
@ConfigurationProperties(prefix = "app")
public class AppConfig {
    private boolean dataInit;

    /**
     * Returns whether data initialization is enabled.
     *
     * @return true if data initialization should be run, false otherwise
     */
    public boolean isDataInit() {
        return dataInit;
    }

    /**
     * Sets the data initialization flag.
     *
     * @param dataInit true to enable data initialization, false to disable
     */
    public void setDataInit(boolean dataInit) {
        this.dataInit = dataInit;
    }
}