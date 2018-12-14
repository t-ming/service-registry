package me.tm.serviceregistry.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WorldController {
    @RequestMapping(value = "world", name = "WorldService")
    public String world() {
        return "world!";
    }
}