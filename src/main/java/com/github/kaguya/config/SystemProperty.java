package com.github.kaguya.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SystemProperty {

    private static String author;

    public static String getAuthor(){
        return author;
    }

    @Value("${system.property.author}")
    public void setAuthor(String author){
        this.author = author;
    }
}
