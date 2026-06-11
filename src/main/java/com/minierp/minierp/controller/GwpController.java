package com.minierp.minierp.controller;

import com.minierp.minierp.entity.Gwp;
import com.minierp.minierp.repository.GwpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gwps")
@RequiredArgsConstructor
public class GwpController {
    private  final GwpRepository gwpRepository;

    @GetMapping
    public List<Gwp> getAll(){
        return gwpRepository.findAll();
    }

    @PostMapping
    public Gwp create(@RequestBody Gwp gwp){
        return gwpRepository.save(gwp);
    }

    @PutMapping("/{id}")
    public Gwp update(@PathVariable Integer id, @RequestBody Gwp gwp) {
        gwp.setGwpId(id);
        return gwpRepository.save(gwp);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        gwpRepository.deleteById(id);
    }
}
