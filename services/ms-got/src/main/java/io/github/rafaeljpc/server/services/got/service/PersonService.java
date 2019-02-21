package io.github.rafaeljpc.server.services.got.service;

import io.github.rafaeljpc.server.services.got.controller.HouseController;
import io.github.rafaeljpc.server.services.got.controller.PersonController;
import io.github.rafaeljpc.server.services.got.dto.Person;
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
import rocks.spiffy.spring.hateoas.utils.uri.resolver.ControllerUriResolver;

import javax.annotation.PostConstruct;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static rocks.spiffy.spring.hateoas.utils.uri.builder.ControllerUriBuilder.linkTo;

@Service
@Log4j2
public class PersonService {

    @Autowired
    private PersonEntityRepository personRepository;

    @Autowired
    private HouseEntityRepository houseRepository;

    @Autowired
    private Validator validator;

    @PostConstruct
    public void populate() {
        log.info("People populated");
    }

    @Transactional(readOnly = true)
    public List<Person> listAll() {
        try (Stream<PersonEntity> stream = personRepository.streamAll()) {
            return stream.map(this::toDto).collect(Collectors.toList());
        }
        catch (Exception ex) {
            log.error("Unable to get all persons", ex);
            throw new InternalServerException();
        }
    }

    @Transactional(readOnly = true)
    public List<Person> listByHouseId(Long houseId) {
        try (Stream<PersonEntity> stream = personRepository.findByHouseId(houseId).stream()){
            return stream.map(this::toDto).collect(Collectors.toList());
        }
        catch (Exception ex) {
            log.error("Unable to get persons from house", ex);
            throw new InternalServerException();
        }
    }

    @Transactional(readOnly = true)
    public Person findById(Long id) {
        Optional<PersonEntity> entity = null;
        try {
            entity = personRepository.findById(id);
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
    public Person addNew(Person dto) {
        Set<ConstraintViolation<Person>> violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            throw new BadRequestException();
        }

        if (!dto.hasLink("house") || dto.getLink("house").getHref() == null) {
            throw new BadRequestException();
        }

        ControllerUriResolver cr = ControllerUriResolver.on(methodOn(HouseController.class).getOne(null));
        String houseId = cr.resolve(dto.getLink("house").getHref(), "house")
                .orElseThrow(BadRequestException::new);

        boolean personExists = false;
        Optional<HouseEntity> houseEntity = null;
        try {
            personExists = personRepository.existsByName(dto.getName());
            houseEntity = houseRepository.findById(Long.parseLong(houseId));
        }
        catch (Exception ex) {
            log.error("Unable to check if person exists", ex);
            throw new InternalServerException();
        }

        if (!houseEntity.isPresent()) {
            throw new BadRequestException();
        }

        if (personExists) {
            throw new ConflictException();
        }

        try {
            PersonEntity person = personRepository.save(
                    new PersonEntity(Encode.forHtml(dto.getName()), houseEntity.get())
            );
            return toDto(person);
        }
        catch (Exception ex) {
            log.error("Unable to add new person", ex);
            throw new InternalServerException();
        }
    }

    private Person toDto(PersonEntity personEntity) {
        return new Person(personEntity.getName())
                .withLink(linkTo(
                        methodOn(PersonController.class).getOne(personEntity.getId())
                ).withSelfRel())
                .withLink(linkTo(
                        methodOn(HouseController.class).getOne(personEntity.getHouse().getId())
                ).withRel("house"));
    }
}
