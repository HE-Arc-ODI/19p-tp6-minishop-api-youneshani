package ch.hearc.ig.odi.minishop.services;

import ch.hearc.ig.odi.minishop.business.Cart;
import ch.hearc.ig.odi.minishop.business.Customer;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class basicJPATests {
  private EntityManagerFactory entityManagerFactory;

  @Before
  public void setUp() throws Exception {
    // like discussed with regards to SessionFactory, an EntityManagerFactory is set up once for an application
    // 		IMPORTANT: notice how the name here matches the name we gave the persistence-unit in persistence.xml!
    entityManagerFactory = Persistence.createEntityManagerFactory( "ch.he-arc.odi.minishop.jpa" );
  }

  @After
  public void tearDown() throws Exception {
    entityManagerFactory.close();
  }


  @Test
  public void testJPABasicUsage() {
    // create a couple of events...
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    Customer customer1 = new Customer("urosselet", "Ulysse", "Rosselet",
        "ulysse.rosselet@he-arc.ch", "032 789 78 89");
    entityManager.persist(customer1);
    entityManager.getTransaction().commit();
    entityManager.close();

    // now lets pull events from the database and list them
    entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    List<Customer> result = entityManager.createQuery( "from Customer", Customer.class ).getResultList();
    for (Customer customer : (List<Customer>) result) {
      System.out.println("customer " + customer.getCustomerId() + " " + customer.getFirstName() + " " + customer.getLastName() + " : " + customer.getEmail());
    }
    entityManager.getTransaction().commit();
    entityManager.close();
  }


  @Test
  public void testManyToOne() {
    // create a couple of events...
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    Customer customer1 = new Customer("urosselet", "Ulysse", "Rosselet",
        "ulysse.rosselet@he-arc.ch", "032 789 78 89");
    Cart cart = new Cart();
    cart.setCustomer(customer1);
    entityManager.persist(customer1);
    entityManager.persist(cart);
    entityManager.getTransaction().commit();
    entityManager.close();

    // now lets pull events from the database and list them
    entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    List<Customer> customers = entityManager.createQuery( "from Customer", Customer.class ).getResultList();
    for (Customer customer : (List<Customer>) customers) {
      System.out.println("customer " + customer.getCustomerId() + " " + customer.getFirstName() + " " + customer.getLastName() + " : " + customer.getEmail());
    }
    List<Cart> carts = entityManager.createQuery( "from Cart", Cart.class ).getResultList();
    for (Cart cart1 : (List<Cart>) carts) {
      System.out.println("cart " + cart1.getCartid() + " " + cart1.getCustomer().getUsername());
    }
    entityManager.getTransaction().commit();
    entityManager.close();
  }
}