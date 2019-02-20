package io.github.rafaeljpc.server.services.got.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

@ApiModel(description = "Represents a person within the Game of Thrones fantasy world")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Person extends ResourceSupport implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Size(min = 1, max = 100)
    @JsonProperty(value = "name")
    @Pattern(regexp = "[a-zA-Z0-9\\s]+")
    @ApiModelProperty(notes = "The name of the person", required = true, allowEmptyValue = false, example = "Cersei Lannister")
    private String name;

    @JsonCreator
    public Person(@JsonProperty("name") String name) {
        super();
        this.name = name;
    }

    public Person withLink(Link link) {
        this.add(link);
        return this;
    }

    public Person withLink(String href, String rel) {
        this.add(new Link(href, rel));
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
