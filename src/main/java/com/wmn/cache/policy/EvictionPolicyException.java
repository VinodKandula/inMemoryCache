package com.wmn.cache.policy;

public class EvictionPolicyException extends RuntimeException{

    public EvictionPolicyException(String msg){
        super(msg);
    }
}
