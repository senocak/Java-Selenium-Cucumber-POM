package com.senocak.fvs.config;

import com.senocak.fvs.utility.zulip.model.Zulip;
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
    Zulip zulip;
}