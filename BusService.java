package com.busbooking.busbooking.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.busbooking.busbooking.model.Bus;
import com.busbooking.busbooking.repository.BusRepository;

@Service
public class BusService {

    @Autowired
    private BusRepository repo;

    public List<Bus> getAllBuses() {
        return repo.findAll();
    }

    public Bus addBus(Bus bus) {
        return repo.save(bus);
    }

    public Bus updateBus(Bus bus) {
        return repo.save(bus);
    }

    public void deleteBus(int busId) {
        repo.deleteById(busId);
    }

    public List<Bus> search(String source, String destination) {
        return repo.findBySourceAndDestination(source, destination);
    }

    public List<Bus> searchBuses(String source, String destination) {
        return repo.findBySourceAndDestination(source, destination);
    }

    public Bus getBusById(int id) {
        return repo.findById(id);
    }

    public List<Bus> getBusesByOperator(int operatorId) {
        return repo.findByOperatorId(operatorId);
    }
}