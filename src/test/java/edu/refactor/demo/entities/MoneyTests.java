package edu.refactor.demo.entities;
import edu.refactor.demo.entities.enums.Currency;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
public class MoneyTests {

    Money money(Currency c, BigDecimal v){
        Money m = new Money();
        m.setValue(v);
        m.setCurrency(c);
        return m;
    }

    @Test
    public void testPaymentSystem() {
        BillingAccount b = new BillingAccount();
        List<Money> bills = new ArrayList<>();
        bills.add(money(Currency.DOLLARS, new BigDecimal(100)));
        bills.add(money(Currency.RUBLES, new BigDecimal(1000)));
        bills.add(money(Currency.EUROS, new BigDecimal(300)));
        b.setMoney(bills);

        b.checkBalanceAndPay(Currency.RUBLES, new BigDecimal(100));

        for (Money y: b.getMoney())
            if(y.getCurrency().getId().equals(Currency.RUBLES.getId()))
                assertEquals(new BigDecimal(900), y.getValue());

        b.checkBalanceAndPay(Currency.RUBLES, new BigDecimal(1000));

        for (Money y: b.getMoney())
            if(y.getCurrency().getId().equals(Currency.RUBLES.getId()))
                assertEquals(y.getValue(), BigDecimal.ZERO);
    }

    @Test(expected = RuntimeException.class)
    public void testMoreThanCanAfford() {
        BillingAccount b = new BillingAccount();
        List<Money> bills = new ArrayList<>();
        bills.add(money(Currency.DOLLARS, new BigDecimal(100)));
        bills.add(money(Currency.RUBLES, new BigDecimal(1000)));
        bills.add(money(Currency.EUROS, new BigDecimal(300)));
        b.setMoney(bills);

        b.checkBalanceAndPay(Currency.EUROS, new BigDecimal(1000));
    }


}
