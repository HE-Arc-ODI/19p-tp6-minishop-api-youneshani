package ch.hearc.ig.odi.minishop.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import ch.hearc.ig.odi.minishop.business.Customer;
import ch.hearc.ig.odi.minishop.exception.CustomerException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PersistenceServiceTest {

  private PersistenceService persistenceService;
  private EntityManagerFactory entityManagerFactory;

  @Before
  public void setUp() throws Exception {
    persistenceService = new PersistenceService();
    // like discussed with regards to SessionFactory, an EntityManagerFactory is set up once for an application
    // 		IMPORTANT: notice how the name here matches the name we gave the persistence-unit in persistence.xml!
    entityManagerFactory = Persistence.createEntityManagerFactory( "ch.he-arc.odi.minishop.jpa" );
  }

  @After
  public void tearDown() throws Throwable {
    persistenceService.finalize();
  }


  @Test
  public void getAllCustomers() {
  }

  @Test
  public void createAndPersistCustomer() {
    Customer expectedCustomer;
    String expectedUserName = "acooper";
    expectedCustomer = persistenceService.createAndPersistCustomer(expectedUserName,"alice","cooper","acooper@hell.net","666 55 55 55");
    Long expectedCustomerId = expectedCustomer.getCustomerId();

    EntityManager entityManager = entityManagerFactory.createEntityManager();
    Customer actualCustomer = entityManager.find(Customer.class, expectedCustomerId);
    entityManager.close();

    assertSame(actualCustomer.getUsername(), expectedUserName);
  }

  @Test
  public void getCustomerByID() {
    long expectedCustomerId = 2l;
    String expectedUsername = "2mississipi";

    persistenceService.createAndPersistCustomer("1mississipi","1","1","acooper@hell.net","00000000001");
    persistenceService.createAndPersistCustomer("2mississipi","2","2","acooper@hell.net","00000000002");
    persistenceService.createAndPersistCustomer("3mississipi","3","3","acooper@hell.net","00000000003");

    EntityManager entityManager = entityManagerFactory.createEntityManager();
    Customer actualCustomer = entityManager.find(Customer.class, expectedCustomerId);

    assertEquals(expectedUsername, actualCustomer.getUsername());
  }

  @Test
  public void deleteCustomer() throws CustomerException {
    Customer expectedCustomer;

    expectedCustomer = persistenceService.createAndPersistCustomer("deleteMe","alice","cooper","acooper@hell.net","666 55 55 55");
    long idToDelete = expectedCustomer.getCustomerId();

    persistenceService.deleteCustomer(idToDelete);

    EntityManager entityManager = entityManagerFactory.createEntityManager();
    Customer deletedCustomer = entityManager.find(Customer.class, idToDelete);
    entityManager.close();

    assertNull(deletedCustomer);
  }

  @Test
  public void updateCustomer() throws CustomerException {
    Customer expectedCustomer;
    String updatedFirstName = "Updated First Name";
    String updatedLastName = "Updated Last Name";
    String updatedEmail = "updated@ido.com";
    String updatedUsername = "updated";
    String updatedPhone = "00022222";

    expectedCustomer = persistenceService.createAndPersistCustomer("updateMe","alice","cooper","acooper@hell.net","666 55 55 55");
    long idToUpdate = expectedCustomer.getCustomerId();

    expectedCustomer.setFirstName(updatedFirstName);
    expectedCustomer.setLastName(updatedLastName);
    expectedCustomer.setEmail(updatedEmail);
    expectedCustomer.setUsername(updatedUsername);
    expectedCustomer.setPhone(updatedPhone);

    persistenceService.updateCustomer(idToUpdate, expectedCustomer);

    EntityManager entityManager = entityManagerFactory.createEntityManager();
    Customer actualCustomer = entityManager.find(Customer.class, idToUpdate);
    entityManager.close();

    assertEquals(updatedFirstName, actualCustomer.getFirstName());
    assertEquals(updatedLastName, actualCustomer.getLastName());
    assertEquals(updatedEmail, actualCustomer.getEmail());
    assertEquals(updatedUsername, actualCustomer.getUsername());
    assertEquals(updatedPhone, actualCustomer.getPhone());
  }
}