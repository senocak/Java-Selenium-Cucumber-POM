package com.senocak.fvs.utility.zulip.model;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class AllUsers {
    private String result;
    private String msg;
    private List<Member> members;
}
