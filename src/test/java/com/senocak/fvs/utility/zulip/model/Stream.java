package com.senocak.fvs.utility.zulip.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Stream {
    private String name;
    private int stream_id;
    private String description;
    private String rendered_description;
    private boolean invite_only;
    private boolean is_web_public;
    private int stream_post_policy;
    private boolean history_public_to_subscribers;
    private int first_message_id;
    private Integer message_retention_days;
    private boolean is_announcement_only;
}
