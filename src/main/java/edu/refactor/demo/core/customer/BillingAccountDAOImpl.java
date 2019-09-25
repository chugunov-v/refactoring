package edu.refactor.demo.core.customer;

import edu.refactor.demo.entities.BillingAccount;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * @author sofronov
 * Created: 25.09.2019
 */
public class BillingAccountDAOImpl extends SimpleJpaRepository<BillingAccount, Long> implements BillingAccountDAO {

    private final EntityManager em;

    public BillingAccountDAOImpl(EntityManager em) {
        super(BillingAccount.class, em);
        this.em = em;
    }

    @Override
    public List<BillingAccount> findBillingAccountsByLoginAndEmail(String login, String email) {
        return findAll((Specification<BillingAccount>) (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.and(criteriaBuilder.equal(root.get("customer").get("login"), login),
                criteriaBuilder.equal(root.get("customer").get("email"), email)));
    }
}
