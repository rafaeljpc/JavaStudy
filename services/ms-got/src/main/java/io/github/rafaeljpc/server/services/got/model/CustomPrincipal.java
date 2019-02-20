package io.github.rafaeljpc.server.services.got.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.Serializable;


@Data
@Component
@NoArgsConstructor
@AllArgsConstructor
public class CustomPrincipal implements Serializable {
    private static final long serialVersionUID = 1L;

    private String username;
    private String email;
}
