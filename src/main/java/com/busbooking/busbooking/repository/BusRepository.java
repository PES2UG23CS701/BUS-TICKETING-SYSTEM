package com.busbooking.busbooking.repository;

import com.busbooking.busbooking.model.Bus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BusRepository extends JpaRepository<Bus, Integer> {
    List<Bus> findBySourceAndDestination(String source, String destination);
    Bus findById(int id);
    List<Bus> findByOperatorId(int operatorId);
    List<Bus> findByOperatorIdOrderByDepartureTime(int operatorId);
}