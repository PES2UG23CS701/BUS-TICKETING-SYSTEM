package com.busbooking.busbooking.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.busbooking.busbooking.model.Bus;
import com.busbooking.busbooking.service.BusService;

@RestController
@RequestMapping("/bus")
@CrossOrigin("*")
public class BusController {

    @Autowired
    private BusService service;

    @GetMapping("/all")
    public List<Bus> getBuses() {
        return service.getAllBuses();
    }

    @PostMapping("/add")
    public Bus addBus(@RequestBody Bus bus) {
        return service.addBus(bus);
    }

    @GetMapping("/search")
    public List<Bus> search(@RequestParam String source, @RequestParam String destination) {
        return service.search(source, destination);
    }

    @GetMapping("/{id}")
    public Bus getBusById(@PathVariable int id) {
        return service.getBusById(id);
    }
} 