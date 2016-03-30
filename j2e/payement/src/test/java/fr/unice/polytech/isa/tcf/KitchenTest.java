package fr.unice.polytech.isa.tcf;

import fr.unice.polytech.isa.tcf.entities.*;
import fr.unice.polytech.isa.tcf.tests.AbstractTCFTest;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import javax.ejb.EJB;

import java.util.HashSet;
import java.util.Set;


@RunWith(Arquillian.class)
public class KitchenTest extends AbstractTCFTest {


	@EJB private OrderProcessing processor;
	@EJB private Tracker tracker;

	private Set<Item> items;

	@Before
	public void setUpContext() {
		memory.flush();
		items = new HashSet<>();
		items.add(new Item(Cookies.CHOCOLALALA, 3));
		items.add(new Item(Cookies.DARK_TEMPTATION, 2));
	}

	@Test
	public void processCommand() throws Exception {
		Customer pat = new Customer("pat", "1234-567890");
		Order inProgress = new Order(pat, items);
		processor.process(inProgress);
		assertEquals(OrderStatus.IN_PROGRESS, tracker.status(inProgress.getId()));

		Customer ron = new Customer("ron", "1234-567890");
		Order ready = new Order(ron, items);
		processor.process(ready);
		assertEquals(OrderStatus.READY, tracker.status(ready.getId()));
	}




}
