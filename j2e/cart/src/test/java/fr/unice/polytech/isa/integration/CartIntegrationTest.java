package fr.unice.polytech.isa.integration;

import fr.unice.polytech.isa.tcf.AbstractTCFTest;
import fr.unice.polytech.isa.tcf.CartModifier;
import fr.unice.polytech.isa.tcf.CustomerFinder;
import fr.unice.polytech.isa.tcf.CustomerRegistration;
import fr.unice.polytech.isa.tcf.entities.Cookies;
import fr.unice.polytech.isa.tcf.entities.Customer;
import fr.unice.polytech.isa.tcf.entities.Item;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

/**
 * Basic integration test
 * Created by Loic on 04/04/2016.
 */
@RunWith(Arquillian.class)
public class CartIntegrationTest extends AbstractTCFTest
{
    @EJB
    private CustomerRegistration registry;
    @EJB private CustomerFinder finder;
    @EJB(name = "cart-stateless") private CartModifier cart;

    @Test
    public void mainTest() throws Exception
    {
        // On créer le customer dans la base
        String name = "Kevin";
        String creditCard = "0836656565";
        registry.register(name, creditCard);
        Optional<Customer> customer = finder.findByName(name); // A debug
        Assert.assertTrue(customer.isPresent());
        Customer kevin = customer.get();
        Assert.assertEquals(name, kevin.getName());
        Assert.assertEquals(creditCard, kevin.getCreditCard());

        // On utilise ce customer pour effectuer une commande
        cart.add(kevin, new Item(Cookies.CHOCOLALALA, 3));
        cart.add(kevin, new Item(Cookies.DARK_TEMPTATION, 5));
        Item[] oracle = new Item[] {new Item(Cookies.CHOCOLALALA, 3), new Item(Cookies.DARK_TEMPTATION, 5)  };
        assertEquals(new HashSet<>(Arrays.asList(oracle)), cart.contents(kevin));
    }
}
