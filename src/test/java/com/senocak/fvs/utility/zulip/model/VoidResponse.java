package com.senocak.fvs.utility.zulip.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VoidResponse {
    private String result;
    private String msg;
    private int id;
}
