package com.example.biblioteca_pessoal.controller;

import com.example.biblioteca_pessoal.model.Livro;
import com.example.biblioteca_pessoal.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Indica que esta classe é um controlador REST (retorna JSON em vez de views HTML)
@RequestMapping("/api/livros") // Define a rota base: todos os endpoints começam com /api/livros
public class LivroController {

    @Autowired // Injeta automaticamente uma instância do LivroRepository
    private LivroRepository livroRepository;

    // CREATE -> POST /api/livros
    @PostMapping
    public Livro criarLivro(@RequestBody Livro livro) {
        // @RequestBody -> indica que os dados virão no corpo da requisição em JSON
        return livroRepository.save(livro);
    }

    // READ -> GET /api/livros
    @GetMapping
    public List<Livro> listarLivros() {
        return livroRepository.findAll();
    }

    // READ -> GET /api/livros/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Livro> buscarPorId(@PathVariable Long id) {
        // @PathVariable -> pega o valor do {id} da URL
        return livroRepository.findById(id)
                .map(ResponseEntity::ok) // Se encontrar, retorna 200 OK com o livro
                .orElse(ResponseEntity.notFound().build()); // Se não, retorna 404
    }

    // UPDATE -> PUT /api/livros/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Livro> atualizarLivro(@PathVariable Long id, @RequestBody Livro detalhes) {
        return livroRepository.findById(id)
                .map(livro -> {
                    livro.setTitulo(detalhes.getTitulo());
                    livro.setAutor(detalhes.getAutor());
                    livro.setAnoPublicacao(detalhes.getAnoPublicacao());
                    livro.setLido(detalhes.isLido());
                    return ResponseEntity.ok(livroRepository.save(livro));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE -> DELETE /api/livros/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarLivro(@PathVariable Long id) {
        return livroRepository.findById(id)
                .map(livro -> {
                    livroRepository.delete(livro);
                    return ResponseEntity.noContent().<Void>build(); // retorna 204 No Content
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
