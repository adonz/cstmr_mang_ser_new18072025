package com.incede.nbfc.customer_management.SchemaConfig;

import org.hibernate.cfg.AvailableSettings;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class JpaSchemaConfig {
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder,
            DataSource dataSource) {
        Map<String, Object> properties = new HashMap<>();
        properties.put(AvailableSettings.DEFAULT_SCHEMA, "customers");
        return builder
                .dataSource(dataSource)
                .packages("com.incede.nbfc.customer_management")
                .properties(properties)
                .build();
    }
}

