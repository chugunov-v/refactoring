package edu.refactor.demo.core.vehicle;

import edu.refactor.demo.entities.Vehicle;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import java.util.Optional;

/**
 * @author sofronov
 * Created: 25.09.2019
 */
public class VehicleDAOImpl extends SimpleJpaRepository<Vehicle, Long> implements VehicleDAO {

    public VehicleDAOImpl(EntityManager entityManager) {
        super(Vehicle.class, entityManager);
    }

    @Override
    public Optional<Vehicle> findVehicleBySerialNumber(String serialNumber) {
        return findOne((Specification<Vehicle>) (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("serialNumber"), serialNumber));
    }
}
