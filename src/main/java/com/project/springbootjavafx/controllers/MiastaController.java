package com.project.springbootjavafx.controllers;

import com.project.springbootjavafx.models.Miasta;
import com.project.springbootjavafx.services.MiastaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/miasta")
public class MiastaController {

    private final MiastaService miastaService;

    @Autowired
    public MiastaController(MiastaService miastaService) {
        this.miastaService = miastaService;
    }

    // Pobierz wszystkie miasta
    @GetMapping
    public List<Miasta> getAllMiasta() {
        return miastaService.getAllMiasta();
    }

    // Pobierz miasto po nazwie
    @GetMapping("/{name}")
    public Miasta getMiastoByName(@PathVariable String name) {
        return miastaService.getMiastoByName(name);
    }

    // Dodaj nowe miasto
    @PostMapping
    public Miasta addMiasto(@RequestBody Miasta miasto) {
        return miastaService.addMiasto(miasto);
    }

    // Usuń miasto
    @DeleteMapping("/{name}")
    public void deleteMiasto(@PathVariable String name) {
        miastaService.deleteMiasto(name);
    }
}
