package com.softklass.grs.controller;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

@Controller("/report")
public class Index {

    @Get(uri = "/", produces = "text/plain")
    public String index() {
        return "Example Response";
    }
}
