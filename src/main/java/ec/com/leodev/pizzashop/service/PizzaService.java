package ec.com.leodev.pizzashop.service;

import ec.com.leodev.pizzashop.persistence.entity.PizzaEntity;
import ec.com.leodev.pizzashop.persistence.repository.PizzaPagSortRepository;
import ec.com.leodev.pizzashop.persistence.repository.PizzaRepository;
import ec.com.leodev.pizzashop.service.dto.UpdatePizzaPriceDto;
import ec.com.leodev.pizzashop.service.exception.EmailApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PizzaService {

//    private final JdbcTemplate jdbcTemplate;
    private final PizzaRepository pizzaRepository;
    private final PizzaPagSortRepository pizzaPagSortRepository;

    @Autowired
    public PizzaService(PizzaRepository pizzaRepository, PizzaPagSortRepository pizzaPagSortRepository) {
        //this.jdbcTemplate = jdbcTemplate;
        this.pizzaRepository = pizzaRepository;
        this.pizzaPagSortRepository = pizzaPagSortRepository;
    }

    public Page<PizzaEntity> findAll(Integer page, Integer size) {
        // return this.jdbcTemplate.query("SELECT * FROM pizza", new BeanPropertyRowMapper<>(PizzaEntity.class));
        // return this.pizzaRepository.findAll();
        Pageable pageRequest = PageRequest.of(page, size);
        return this.pizzaPagSortRepository.findAll(pageRequest);
    }

    public Page<PizzaEntity> findAvailable(Integer page, Integer size, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageRequest = PageRequest.of(page, size, sort);
        return this.pizzaPagSortRepository.findByAvailableTrue(pageRequest);
    }

    public PizzaEntity getByName(String name) {
        return this.pizzaRepository.findFirstByAvailableTrueAndNameIgnoreCase(name).orElseThrow(() -> new RuntimeException("No existe pizza"));
    }

    public List<PizzaEntity> getWith(String ingredient) {
        return this.pizzaRepository.findAllByAvailableTrueAndDescriptionContainingIgnoreCase(ingredient);
    }

    public List<PizzaEntity> getWithOut(String ingredient) {
        return this.pizzaRepository.findAllByAvailableTrueAndDescriptionNotContainingIgnoreCase(ingredient);
    }

    public List<PizzaEntity> getCheapest(Double price) {
        return this.pizzaRepository.findTop3ByAvailableTrueAndPriceLessThanEqualOrderByPriceAsc(price);
    }

    public PizzaEntity get(int idPizza) {
        return this.pizzaRepository.findById(idPizza).orElse(null);
    }

    public PizzaEntity save(PizzaEntity pizza) {
        return this.pizzaRepository.save(pizza);
    }

    public void delete(int idPizza) {
        this.pizzaRepository.deleteById(idPizza);
    }

    @Transactional(noRollbackFor = EmailApiException.class)
    public void updatePrice(UpdatePizzaPriceDto dto) {
        this.pizzaRepository.updatePrice(dto);
        this.sendEmail();
    }

    private void sendEmail() {
        throw new EmailApiException();
    }

    public boolean exists(int idPizza) {
        return this.pizzaRepository.existsById(idPizza);
    }


}
