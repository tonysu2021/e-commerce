package com.commerce.txn.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import com.commerce.txn.conf.SnowflakeConfig;
import com.commerce.txn.conf.SnowflakeConfigProp;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({ SnowflakeConfigProp.class,SnowflakeConfig.class })
public @interface EnableSnowflake {

}
