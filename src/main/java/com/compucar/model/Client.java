package com.compucar.model;

import lombok.Data;

@Data
public class Client extends Contact {
    private String email;
    private ClientType type;

}
