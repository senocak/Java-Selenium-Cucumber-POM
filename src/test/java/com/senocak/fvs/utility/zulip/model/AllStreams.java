package com.senocak.fvs.utility.zulip.model;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class AllStreams {
    private String result;
    private String msg;
    private List<Stream> streams;
}
