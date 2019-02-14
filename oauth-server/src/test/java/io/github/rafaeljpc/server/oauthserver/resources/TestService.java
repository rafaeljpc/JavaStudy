package io.github.rafaeljpc.server.oauthserver.resources;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(
        value = {"/api/test"},
        produces = MediaType.APPLICATION_JSON_VALUE
)
@Validated
public class TestService {

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ServiceResult service(@RequestParam("name") String name) {
        return new ServiceResult(name);
    }

    public static class ServiceResult {

        private static final String fmt = "Welcome %s!";

        public String greetings;

        public ServiceResult(String name) {
            this.greetings = String.format(fmt, name);
        }
    }
}
