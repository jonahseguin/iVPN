package com.shawckz.ivpn.check;

import com.shawckz.ivpn.IVPN;
import lombok.Getter;

public abstract class ICheck {

    @Getter
    private final IVPN instance;

    public ICheck(IVPN instance) {
        this.instance = instance;
    }

    public abstract boolean check(String uuid, String ip);

}
