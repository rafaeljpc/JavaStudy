package io.github.rafaeljpc.server.services.got.service;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import io.github.rafaeljpc.server.services.got.controller.HouseController;
import io.github.rafaeljpc.server.services.got.dto.House;
import io.github.rafaeljpc.server.services.got.entity.HouseEntity;
import io.github.rafaeljpc.server.services.got.entity.PersonEntity;
import io.github.rafaeljpc.server.services.got.exception.BadRequestException;
import io.github.rafaeljpc.server.services.got.exception.ConflictException;
import io.github.rafaeljpc.server.services.got.exception.InternalServerException;
import io.github.rafaeljpc.server.services.got.exception.ResourceNotFoundException;
import io.github.rafaeljpc.server.services.got.repository.HouseEntityRepository;
import io.github.rafaeljpc.server.services.got.repository.PersonEntityRepository;
import lombok.extern.log4j.Log4j2;
import org.owasp.encoder.Encode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Log4j2
public class HouseService {

    @Autowired
    private HouseEntityRepository houseRepository;

    @Autowired
    private PersonEntityRepository personRepository;

    @Autowired
    private Validator validator;

    @PostConstruct
    public void populate() {

        HouseEntity starks = houseRepository.save(new HouseEntity("Stark"));
        HouseEntity lannisters = houseRepository.save(new HouseEntity("Lannister"));
        houseRepository.save(new HouseEntity("Targaryen"));
        houseRepository.save(new HouseEntity("Arryn"));
        houseRepository.save(new HouseEntity("Greyjoy"));
        houseRepository.save(new HouseEntity("Tully"));
        houseRepository.save(new HouseEntity("Frey"));
        houseRepository.save(new HouseEntity("Baratheon"));
        houseRepository.save(new HouseEntity("Martell"));
        houseRepository.save(new HouseEntity("Tyrell"));
        houseRepository.save(new HouseEntity("Bolton"));

        personRepository.save(new PersonEntity("Eddard Stark", starks));
        personRepository.save(new PersonEntity("Sansa Stark", starks));

        personRepository.save(new PersonEntity("Tyrion Lannister", lannisters));
        personRepository.save(new PersonEntity("Cersei Lannister", lannisters));
        personRepository.save(new PersonEntity("Jaime Stark", lannisters));

        log.info("Houses populated");
    }

    @Transactional(readOnly = true)
    public List<House> listAll() {
        try (Stream<HouseEntity> stream = houseRepository.streamAll()){
            return stream
                    .map(this::toDto).collect(Collectors.toList());
        }
        catch (Exception ex) {
            log.error("Unable to get all houses", ex);
            throw new InternalServerException();
        }
    }

    @Transactional(readOnly = true)
    public House findById(Long id) {
        Optional<HouseEntity> entity = null;

        try {
            entity = houseRepository.findById(id);
        }
        catch (Exception ex) {
            log.error("Unable to get house by id", ex);
            throw new InternalServerException();
        }

        if (!entity.isPresent()) {
            throw new ResourceNotFoundException();
        }
        return toDto(entity.get());
    }

    @Transactional(readOnly = false)
    public House addNew(House house) {
        Set<ConstraintViolation<House>> violations = validator.validate(house);
        if (!violations.isEmpty()) {
            throw new BadRequestException();
        }

        boolean exists = false;
        try {
            exists = houseRepository.existsByName(house.getName());
        }
        catch (Exception ex) {
            log.error("Unable to check if house exists", ex);
            throw new InternalServerException();
        }

        if (exists) {
            throw new ConflictException();
        }

        try {
            HouseEntity houseEntity = houseRepository.save(new HouseEntity(Encode.forHtml(house.getName())));
            return toDto(houseEntity);
        }
        catch (Exception ex) {
            log.error("Unable to add new house", ex);
            throw new InternalServerException();
        }
    }


    private House toDto(HouseEntity houseEntity) {
        return new House(houseEntity.getName())
                .withLink(linkTo(
                        methodOn(HouseController.class).getOne(houseEntity.getId())
                ).withSelfRel())
                .withLink(linkTo(
                        methodOn(HouseController.class).getHouseMembers(houseEntity.getId())
                ).withRel("members"));
    }
}
