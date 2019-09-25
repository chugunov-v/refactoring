package edu.refactor.demo.core.customer;

import edu.refactor.demo.entities.Customer;
import edu.refactor.demo.entities.enums.CustomerStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

/**
 * @author sofronov
 * Created: 25.09.2019
 */
public class CustomerDAOImpl extends SimpleJpaRepository<Customer, Long> implements CustomerDAO {
    private final EntityManager em;

    @Autowired
    public CustomerDAOImpl(EntityManager em) {
        super(Customer.class, em);
        this.em = em;
    }

    @Override
    public List<Customer> loadNotDeletedCustomers() {

        return findAll((Specification<Customer>) (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.notEqual(root.get("status"), CustomerStatus.DELETE.getId()));
    }

    @Override
    public boolean existsByLoginAndEmail(String login, String email) {
        Optional<Customer> optionalCustomer = loadCustomerByLoginAndEmail(login, email);
        return optionalCustomer.isPresent();
    }

    @Override
    public Optional<Customer> loadCustomerByLoginAndEmail(String login, String email) {
        return findOne((Specification<Customer>) (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.or(criteriaBuilder.equal(root.get("login"), login),
                        criteriaBuilder.equal(root.get("email"), email)));
    }
}
