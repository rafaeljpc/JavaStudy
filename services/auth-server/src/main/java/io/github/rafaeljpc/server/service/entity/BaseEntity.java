package io.github.rafaeljpc.server.service.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@MappedSuperclass
@NoArgsConstructor
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Version
    protected long version;

    @CreationTimestamp
    protected LocalDateTime createdOn;

    @UpdateTimestamp
    protected LocalDateTime updatedOn;
}
