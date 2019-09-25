package edu.refactor.demo.core.vehicle.rental;

import edu.refactor.demo.entities.VehicleRental;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
public class VehicleRentalDAOImpl extends SimpleJpaRepository<VehicleRental, Long> implements VehicleRentalDAO {
    private static final String ID_PARAM = "id";

    private static final String UPDATE_STATUS_QUERY = "UPDATE vehicle_rental SET status = 'active', end_rent=now() " +
        "WHERE id=:" + ID_PARAM + " and (status='created' or status = 'expired')";

    private static final String UPDATE_CUSTOMER_STATUS_QUERY = "UPDATE customer C SET status = 'expired' " +
        "WHERE  c.id= " +
        "(select c.id " +
        "from vehicle_rental r join customer c on c.id=r.customer_id " +
        "where r.status='active' " +
        "and ((DATEDIFF(msc, now(), r.START_RENT) > 86400 and c.status='default') " +
        "or (DATEDIFF(msc, now(), r.START_RENT) > 259200 and c.status='vip'))";

    private static final String FIND_BY_ID_QUERY = "select r from vehicle_rental where id=:" + ID_PARAM + " and status='active'";

    private final EntityManager em;

    @Autowired
    public VehicleRentalDAOImpl(EntityManager em) {
        super(VehicleRental.class, em);
        this.em = em;
    }

    @Override
    public Optional<VehicleRental> findActiveById(Long id) {
        return em.createQuery(FIND_BY_ID_QUERY, VehicleRental.class)
            .setParameter(ID_PARAM, id)
            .getResultList()
            .stream()
            .findFirst();
    }

    @Override
    @Transactional
    public void updateCustomerStatus() {
        em.createNativeQuery(UPDATE_CUSTOMER_STATUS_QUERY)
            .executeUpdate();
    }

    @Override
    @Transactional
    public void activate(Long id) {
        em.createNativeQuery(UPDATE_STATUS_QUERY)
            .setParameter(ID_PARAM, id)
            .executeUpdate();

    }
}
