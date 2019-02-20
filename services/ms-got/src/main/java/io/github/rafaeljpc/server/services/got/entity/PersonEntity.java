package io.github.rafaeljpc.server.services.got.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "person")
public class PersonEntity extends BaseEntity<Long> {
    private static final long serialVersionUID = 1L;

    @Id
    @Access(AccessType.PROPERTY)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 1, max = 100)
    @Pattern(regexp = "[a-zA-Z0-9\\s]+")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @NotNull
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "house_id", nullable = false, updatable = true,
            foreignKey = @ForeignKey(name = "fk_person_house"))
    private HouseEntity house;

    public PersonEntity(@NotNull @Size(min = 1, max = 100) @Pattern(regexp = "[a-zA-Z0-9\\s]+") String name, @NotNull HouseEntity house) {
        this.name = name;
        this.house = house;
    }
}
