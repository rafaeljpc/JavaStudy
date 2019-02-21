package io.github.rafaeljpc.server.services.got.repository;


import io.github.rafaeljpc.server.services.got.entity.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

public interface PersonEntityRepository extends JpaRepository<PersonEntity, Long> {

    @Transactional(readOnly = true)
    List<PersonEntity> findByHouseId(Long houseId);

    @Transactional(readOnly = true)
    @Query("select e from PersonEntity e")
    Stream<PersonEntity> streamAll();

    @Transactional(readOnly = true)
    boolean existsByName(String name);
}
