package com.credibanco.bankinc.dfsv.response;

import org.springframework.hateoas.RepresentationModel;

/**
 * @author Daniel felipe saraza velez
 * @since Mayo 22 2024
 */
public class GenericResponse extends RepresentationModel<GenericResponse> {
    public int id;
    public String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
