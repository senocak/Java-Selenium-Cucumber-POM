package com.senocak.fvs.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.util.List;

@Getter
@Setter
@ToString
public class Config {
    String url;
    String timeout;
    String browser;
    String environment;
    List<User> users;
}