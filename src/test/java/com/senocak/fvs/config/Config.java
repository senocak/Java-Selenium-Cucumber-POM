package com.senocak.fvs.config;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class Config {
    String url;
    String timeout;
    String browser;
    String environment;
    List<Users> users;
}