package io.github.rafaeljpc.server.services.got.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@Data
@NoArgsConstructor
@MappedSuperclass
public abstract class BaseEntity<T extends Serializable> implements Serializable {
    private static final long serialVersionUID = 1L;

}
