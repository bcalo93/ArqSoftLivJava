package com.compucar.context;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import java.beans.PropertyVetoException;
import java.util.Properties;

@Configuration
@EnableJpaRepositories(basePackages = "com.compucar.dao")
@EnableTransactionManagement
public class DbConfig {

    @Value("${db.driver}")
    private String dbDriverClass;

    @Value("${db.url}")
    private String dbUrl;

    @Value("${db.user}")
    private String dbUser;

    @Value("${db.password}")
    private String dbPassword;

    @Value("${db.maxPool}")
    private int dbMaxPool;

    @Value("${db.minPool}")
    private int dbMinPool;

    @Value("${db.testQuery}")
    private String dbTestQuery;

    @Bean
    public ComboPooledDataSource dataSource() {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        try {
            dataSource.setDriverClass(dbDriverClass);
            dataSource.setJdbcUrl(dbUrl);
            dataSource.setUser(dbUser);
            dataSource.setPassword(dbPassword);
            dataSource.setMaxPoolSize(dbMaxPool);
            dataSource.setMinPoolSize(dbMinPool);
            dataSource.setPreferredTestQuery(dbTestQuery);
        } catch (PropertyVetoException e) {
            throw new RuntimeException("Error initializing db pool");
        }
        return dataSource;
    }

    // JpaRepositories FACTORY START
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan("com.compucar.model");
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        properties.put("hibernate.show_sql", true);
        em.setJpaProperties(properties);
        return em;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }
    // JpaRepositories FACTORY END
}