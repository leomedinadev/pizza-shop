package ec.com.leodev.pizzashop.web.controller;

import ec.com.leodev.pizzashop.persistence.entity.PizzaEntity;
import ec.com.leodev.pizzashop.service.PizzaService;
import ec.com.leodev.pizzashop.service.dto.UpdatePizzaPriceDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/pizzas")
public class PizzaController {

    @Autowired
    private PizzaService pizzaService;

    @GetMapping("/findAll")
    public ResponseEntity<Page<PizzaEntity>> findAll(@RequestParam(defaultValue = "0") Integer page,
                                                     @RequestParam(defaultValue = "8") Integer size) {
        return ResponseEntity.ok(this.pizzaService.findAll(page, size));
    }

    @GetMapping("/{idPizza}")
    public ResponseEntity<PizzaEntity> findById(@PathVariable Integer idPizza) {
        return ResponseEntity.ok(this.pizzaService.get(idPizza));
    }


    @GetMapping("/available")
    public ResponseEntity<Page<PizzaEntity>> findAvailable(@RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "8") int size,
                                                           @RequestParam(defaultValue = "price") String sortBy,
                                                           @RequestParam(defaultValue = "ASC") String sortDirection) {
        return ResponseEntity.ok(this.pizzaService.findAvailable(page, size, sortBy, sortDirection));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<PizzaEntity> findByName(@PathVariable String name) {
        return ResponseEntity.ok(this.pizzaService.getByName(name));
    }

    @GetMapping("/with/{ingredient}")
    public ResponseEntity<List<PizzaEntity>> findWith(@PathVariable String ingredient) {
        return ResponseEntity.ok(this.pizzaService.getWith(ingredient));
    }

    @GetMapping("/without/{ingredient}")
    public ResponseEntity<List<PizzaEntity>> findWithout(@PathVariable String ingredient) {
        return ResponseEntity.ok(this.pizzaService.getWithOut(ingredient));
    }

    @GetMapping("/cheapest/{price}")
    public ResponseEntity<List<PizzaEntity>> findCheapestPizzas(@PathVariable double price) {
        return ResponseEntity.ok(this.pizzaService.getCheapest(price));
    }

    @PostMapping("/save")
    public ResponseEntity<PizzaEntity> save(@RequestBody PizzaEntity pizza) {
        if (pizza.getIdPizza() == null) {
            return ResponseEntity.ok(this.pizzaService.save(pizza));
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/update")
    public ResponseEntity<PizzaEntity> update(@RequestBody PizzaEntity pizza) {
        if (pizza.getIdPizza() != null && !this.pizzaService.exists(pizza.getIdPizza())) {
            return ResponseEntity.ok(this.pizzaService.save(pizza));
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/price")
    public ResponseEntity<Void> updatePrice(@RequestBody UpdatePizzaPriceDto dto) {
        if (this.pizzaService.exists(dto.getPizzaId())) {
            this.pizzaService.updatePrice(dto);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{idPizza}")
    public ResponseEntity<Void> delete(@PathVariable int idPizza) {
        if (this.pizzaService.exists(idPizza)) {
            this.pizzaService.delete(idPizza);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
