package io.github.rafaeljpc.server.services.got.controller;

import io.github.rafaeljpc.server.services.got.aop.LogExecution;
import io.github.rafaeljpc.server.services.got.dto.House;
import io.github.rafaeljpc.server.services.got.dto.Person;
import io.github.rafaeljpc.server.services.got.service.HouseService;
import io.github.rafaeljpc.server.services.got.service.PersonService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
@RequestMapping(path = "/houses", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class HouseController {

    @Autowired
    private HouseService houseService;

    @Autowired
    private PersonService personService;

    @LogExecution
    @GetMapping
    public List<House> getAll() {
        return houseService.listAll();
    }

    @LogExecution
    @PreAuthorize("hasAuthority('role_admin')")
    @GetMapping(path = "/{id}")
    public House getOne(@PathVariable("id") Long id) {
        return houseService.findById(id);
    }

    @LogExecution
    @PreAuthorize("hasAuthority('role_admin')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public House postNew(@RequestBody(required = true) House house) {
        return houseService.addNew(house);
    }

    @LogExecution
    @PreAuthorize("hasAuthority('role_admin')")
    @GetMapping(path = "/{id}/members")
    public List<Person> getHouseMembers(@PathVariable("id") Long id) {
        return personService.listByHouseId(id);
    }

}
