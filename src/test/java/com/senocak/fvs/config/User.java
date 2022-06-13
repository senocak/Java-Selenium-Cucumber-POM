package com.senocak.fvs.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class User {
    String name;
    String surname;
    String email;
    String password;
    String company;
    String location;
}