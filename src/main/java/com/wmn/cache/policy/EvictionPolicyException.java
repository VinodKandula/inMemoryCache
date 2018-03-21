package com.wmn.cache.policy;

public class EvictionPolicyException extends RuntimeException{

    EvictionPolicyException(String msg){
        super(msg);
    }
}
