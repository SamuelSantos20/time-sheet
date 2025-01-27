package io.github.samuelsantos20.time_sheet.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
@Slf4j
public class DatabaseConfig {

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Value("${spring.datasource.url}")
    private String url;

    @Bean
    @Primary
    @SneakyThrows
    public DataSource getHikariDataSource(){
        log.info("Inicializando a aplicação com o banco de dados de URL {}", url);

        if (username == null || password == null || url == null) {
            throw new IllegalArgumentException("Configurações de banco de dados inválidas!");
        }

        HikariConfig hikariConfig = new HikariConfig();


            hikariConfig.setUsername(username);
            hikariConfig.setPassword(password);
            hikariConfig.setDriverClassName(driverClassName);
            hikariConfig.setJdbcUrl(url);

            hikariConfig.setConnectionTimeout(30000); // 30 segundos
            hikariConfig.setMaxLifetime(1800000);    // 30 minutos
            hikariConfig.setLeakDetectionThreshold(2000); // Detectar vazamento em 2 segundos
            hikariConfig.setMaximumPoolSize(20);
            hikariConfig.setMinimumIdle(1);
            hikariConfig.setPoolName("time_sheet-database");
            hikariConfig.setConnectionTestQuery("SELECT 1");
            return new HikariDataSource(hikariConfig);

    }


}
