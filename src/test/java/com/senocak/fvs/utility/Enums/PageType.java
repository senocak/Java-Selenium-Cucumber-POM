package com.senocak.fvs.utility.Enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PageType {
    LOGIN("login"),
    FORGOT_PASSWORD("forgot-password"),
    PROFILE("profile");

    private final String isim;

    public static PageType fromString(String name) {
        if(name != null) {
            for (PageType pt : PageType.values()) {
                if (pt.getIsim().equalsIgnoreCase(name)) {
                    return pt;
                }
            }
            throw new IllegalArgumentException("Invalid endpoint type: " + name);
        }
        return null;
    }
}
