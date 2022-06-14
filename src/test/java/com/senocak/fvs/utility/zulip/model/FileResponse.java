package com.senocak.fvs.utility.zulip.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileResponse {
    private String result;
    private String msg;
    private String uri;
}
