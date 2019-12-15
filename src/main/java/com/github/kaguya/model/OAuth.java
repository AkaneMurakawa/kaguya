package com.github.kaguya.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class OAuth implements Serializable {

    public long userId;

    public String authProviderId;

    public String authId;

    public String authToken;

    public long expiresAt;


}
