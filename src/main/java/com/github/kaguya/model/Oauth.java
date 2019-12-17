package com.github.kaguya.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class Oauth implements Serializable {

    private static final long serialVersionUID = 1L;

    public long userId;

    public String authProviderId;

    public String authId;

    public String authToken;

    public long expiresAt;


}
