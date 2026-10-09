package ec.com.leodev.pizzashop.persistence.entity.id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemId implements Serializable {

    private Integer idOrder;
    private Integer idItem;

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        OrderItemId that = (OrderItemId) object;
        return Objects.equals(idOrder, that.idOrder) && Objects.equals(idItem, that.idItem);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idOrder, idItem);
    }
}
