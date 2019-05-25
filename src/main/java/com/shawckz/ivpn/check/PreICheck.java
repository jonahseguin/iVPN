package com.shawckz.ivpn.check;

import com.shawckz.ivpn.IVPN;
import lombok.Getter;

public abstract class PreICheck {

    @Getter
    private final IVPN instance;

    public PreICheck(IVPN instance) {
        this.instance = instance;
    }

    public abstract boolean check(String uuid, String ip);

}
