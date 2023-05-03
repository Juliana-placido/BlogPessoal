package com.generation.controller;

import com.generation.model.Postagem;
import com.generation.repository.PostagemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/postagens")
public class PostagemController {

    @Autowired
    private PostagemRepository postagemRepository;

    @GetMapping
    public List<Postagem> getAll() {
        return postagemRepository.findAll();
    }
}
