package com.time.swimtime.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertyService {

    @Value("${spring.datasource.local.url}")
    private String url;

    @Value("${spring.datasource.local.user}")
    private String user;

    @Value("${spring.datasource.local.password}")
    private String password;

    @Value("${spring.datasource.docker.url}")
    private String urlDocker;

    @Value("${spring.datasource.docker.user}")
    private String userDocker;

    @Value("${spring.datasource.docker.password}")
    private String passwordDocker;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrlDocker() {
        return urlDocker;
    }

    public void setUrlDocker(String urlDocker) {
        this.urlDocker = urlDocker;
    }

    public String getUserDocker() {
        return userDocker;
    }

    public void setUserDocker(String userDocker) {
        this.userDocker = userDocker;
    }

    public String getPasswordDocker() {
        return passwordDocker;
    }

    public void setPasswordDocker(String passwordDocker) {
        this.passwordDocker = passwordDocker;
    }



}