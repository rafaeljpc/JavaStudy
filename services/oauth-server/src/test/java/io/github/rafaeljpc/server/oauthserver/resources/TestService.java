package io.github.rafaeljpc.server.oauthserver.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping(
        value = {"/api/test"},
        produces = MediaType.APPLICATION_JSON_VALUE
)
@Validated
public class TestService {

    private static final Logger log = LoggerFactory.getLogger(TestService.class);

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ServiceResult service(@RequestParam("name") String name, Principal principal) {
        log.debug("principal = " + principal.getName());
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
