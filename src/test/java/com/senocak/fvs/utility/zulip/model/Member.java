package com.senocak.fvs.utility.zulip.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Member {
    private String email;
    private int user_id;
    private int avatar_version;
    private boolean is_admin;
    private boolean is_owner;
    private boolean is_guest;
    private boolean is_bot;
    private String full_name;
    private String timezone;
    private boolean is_active;
    private String date_joined;
    private String avatar_url;
}
