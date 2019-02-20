package io.github.rafaeljpc.server.services.got.repository;

import io.github.rafaeljpc.server.services.got.entity.HouseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

public interface HouseEntityRepository extends JpaRepository<HouseEntity, Long> {

    @Transactional(readOnly = true)
    @Query("select e from HouseEntity e")
    Stream<HouseEntity> streamAll();

    @Transactional(readOnly = true)
    boolean existsByName(String name);
}
