package ec.com.leodev.pizzashop.persistence.repository;

import ec.com.leodev.pizzashop.persistence.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, String> {
}
