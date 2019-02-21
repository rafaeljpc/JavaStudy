package io.github.rafaeljpc.server.services.got;

import io.github.rafaeljpc.server.services.got.entity.HouseEntity;
import io.github.rafaeljpc.server.services.got.repository.HouseEntityRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.hateoas.config.EnableEntityLinks;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient
@EnableTransactionManagement
@EntityScan(basePackageClasses = HouseEntity.class)
@EnableJpaRepositories(basePackageClasses = HouseEntityRepository.class)
@EnableEntityLinks
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
