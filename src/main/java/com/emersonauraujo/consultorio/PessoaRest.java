package com.emersonauraujo.consultorio;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:5500")
@RequestMapping("/pessoa")
public class PessoaRest {

    @Autowired
    private PessoaDao pessoaDao;

   @PostMapping
    public ResponseEntity<String> post(@RequestBody Pessoa pessoa) {
        // 1. Verifica se o CPF já existe no banco de dados.
        // O método `existsByCpf` é criado automaticamente pelo Spring Data JPA
        // e é a forma mais eficiente de fazer essa verificação.
        if (this.pessoaDao.existsByCpf(pessoa.getCpf())) {
            // 2. Se o CPF já existe, retorna um status de erro 409 Conflict.
            // A mensagem no corpo da resposta explica o motivo do erro.
            return ResponseEntity.status(HttpStatus.CONFLICT).body("CPF já cadastrado.");
        }
        
        // 3. Se o CPF não existe, salva a nova pessoa no banco de dados.
        this.pessoaDao.save(pessoa);
        
        // 4. Retorna uma resposta de sucesso com status 201 Created.
        return ResponseEntity.status(HttpStatus.CREATED).body("Pessoa cadastrada com sucesso!");
    }




    @GetMapping
    public List<Pessoa> get(){
        return this.pessoaDao.findAll();
    }

    @PutMapping("/{id}")
    public void put(@PathVariable UUID id, @RequestBody Pessoa pessoa) {
        // Busca o registro existente pelo ID
        Pessoa pessoaExistente = this.pessoaDao.findById(id).orElse(null);

        if (pessoaExistente != null) {
            // Atualiza os campos do objeto existente com os novos dados
            pessoaExistente.setNome(pessoa.getNome());
            pessoaExistente.setCpf(pessoa.getCpf());
            pessoaExistente.setTelefone(pessoa.getTelefone());
            pessoaExistente.setDataNascimento(pessoa.getDataNascimento());

            // Salva o objeto atualizado no banco de dados
            this.pessoaDao.save(pessoaExistente);

            
        } else {
            // Opcional: Tratar o caso em que o registro não é encontrado
            // Por exemplo, lançar uma exceção ou retornar uma resposta de erro
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pessoa> getById(@PathVariable UUID id){
        return this.pessoaDao.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (!this.pessoaDao.existsById(id)) {
            // Retorna 404 Not Found se o ID não for encontrado
            return ResponseEntity.notFound().build();
        }
        this.pessoaDao.deleteById(id);
        // Retorna 204 No Content para indicar sucesso na exclusão
        return ResponseEntity.noContent().build();
    }

}


