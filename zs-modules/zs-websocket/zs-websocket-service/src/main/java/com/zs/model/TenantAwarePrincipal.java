package com.zs.model;

import lombok.Getter;
import lombok.Setter;

import java.security.Principal;

@Setter
@Getter
public class TenantAwarePrincipal implements Principal {

    private String tenantId;
    private Long userId;
    private String username;


    public TenantAwarePrincipal(String tenantId, Long userId, String username) {
        this.tenantId = tenantId;
        this.userId = userId;
        this.username = username;
    }



    @Override
    public String getName() {
        return tenantId + ":" + userId;
    }

}
