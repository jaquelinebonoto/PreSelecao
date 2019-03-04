package br.com.dbc.PreSelecao.rest;

import br.com.dbc.PreSelecao.service.AbstractCrudService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;



public abstract class AbstractRestController< E, S extends AbstractCrudService<E> > {
    
    protected abstract S getService();
    
    
    @GetMapping()
    public ResponseEntity<?> list(Pageable pageable) {
         return ResponseEntity.ok(getService().findAll(pageable));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        return getService().findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> put(@PathVariable Long id, @RequestBody E input) {
        return ResponseEntity.ok(getService().save(input));
    }
    
    @PostMapping
    public ResponseEntity<?> post(@RequestBody E input) {
        return ResponseEntity.ok(getService().save(input));
    }
    
    @PreAuthorize("hasAuthority('ADMIN_USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        getService().delete(id);
        return ResponseEntity.noContent().build();
    }

    

    
}
