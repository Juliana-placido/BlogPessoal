package com.generation.controller;

import com.generation.model.Tema;
import com.generation.repository.TemaRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/temas")
public class TemaController {


    @Autowired
    private TemaRepository temaRepository;

    @GetMapping
    public List<Tema> getAllTemas() {
        return temaRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tema> getTemaById(@PathVariable(value = "id") Long temaId)
            throws ResourceNotFoundException {
        Tema tema = temaRepository.findById(temaId)
                .orElseThrow(() -> new ResourceNotFoundException("Tema não encontrado com o id :: " + temaId));
        return ResponseEntity.ok().body(tema);
    }

    @GetMapping("/descricao/{descricao}")
    public List<Tema> getTemaByDescricao(@PathVariable(value = "descricao") String descricao) {
        return temaRepository.findAllByDescricaoContainingIgnoreCase(descricao);
    }

    @PostMapping
    public Tema createTema(@Valid @RequestBody Tema tema) {
        return temaRepository.save(tema);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tema> updateTema(@PathVariable(value = "id") Long temaId,
                                           @Valid @RequestBody Tema temaDetails) throws ResourceNotFoundException {
        Tema tema = temaRepository.findById(temaId)
                .orElseThrow(() -> new ResourceNotFoundException("Tema não encontrado com o id :: " + temaId));

        tema.setDescricao(temaDetails.getDescricao());

        final Tema updatedTema = temaRepository.save(tema);
        return ResponseEntity.ok(updatedTema);
    }

    @DeleteMapping("/{id}")
    public Map<String, Boolean> deleteTema(@PathVariable(value = "id") Long temaId)
            throws ResourceNotFoundException {
        Tema tema = temaRepository.findById(temaId)
                .orElseThrow(() -> new ResourceNotFoundException("Tema não encontrado com o id :: " + temaId));

        temaRepository.delete(tema);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
