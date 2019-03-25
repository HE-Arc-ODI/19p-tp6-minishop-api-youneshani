/*
 * Copyright (c) 2018. Cours Outils de développement intégré, HEG Arc.
 */

package ch.hearc.ig.odi.minishop.services;


import static ch.hearc.ig.odi.minishop.business.Cart.Cartstatus.CHECK_OUT;

import ch.hearc.ig.odi.minishop.business.Cart;
import ch.hearc.ig.odi.minishop.business.CartItem;
import ch.hearc.ig.odi.minishop.business.Customer;
import ch.hearc.ig.odi.minishop.business.Order;
import ch.hearc.ig.odi.minishop.business.Order.Orderstatus;
import ch.hearc.ig.odi.minishop.business.OrderLine;
import ch.hearc.ig.odi.minishop.business.Product;
import ch.hearc.ig.odi.minishop.exception.CustomerException;
import ch.hearc.ig.odi.minishop.exception.OrderException;
import ch.hearc.ig.odi.minishop.exception.ProductException;
import ch.hearc.ig.odi.minishop.exception.StoreException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class PersistenceService {

  private DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
  private Map<Long, Customer> customers;
  private Map<Long, Product> products;
  private Map<Long, Order> orders;
  private Map<Long, Cart> carts;
  private long idCounterCustomer = 3000;
  private long idCounterProduct = 4000;
  private long idCounterCart = 5000;
  private long idCounterCartItem = 6000;
  private long idCounterOrder = 7000;
  private long idCounterOrderLine = 8000;
  private EntityManagerFactory entityManagerFactory;


  public PersistenceService() {
    //  an EntityManagerFactory is set up once for an application
    //  IMPORTANT: the name here matches the name of persistence-unit in persistence.xml
    entityManagerFactory = Persistence.createEntityManagerFactory("ch.he-arc.odi.minishop.jpa");
    populateMockPersistence();
  }

  @Override
  protected void finalize() throws Throwable {
    entityManagerFactory.close();
    super.finalize();
  }

  /**
   * Return all existing customers
   *
   * @return a list
   */
  public ArrayList<Customer> getAllCustomers() {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    List<Customer> customers = entityManager.createQuery("from Customer", Customer.class)
        .getResultList();
    return (ArrayList<Customer>) customers;
  }


  /**
   * Create a new Customer and persist
   *
   * @return the customer object created
   */
  public Customer createAndPersistCustomer(String username, String firstname, String lastname,
      String email,
      String phone) {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    Customer newCustomer = new Customer(username, firstname, lastname, email,
        phone);
    entityManager.persist(newCustomer);
    entityManager.getTransaction().commit();
    entityManager.close();
    return newCustomer;
  }

  /**
   * Find a customer by her id
   *
   * @param customerid : specifies which customer to return
   * @return an objet customer
   */
  public Customer getCustomerByID(Long customerid) throws CustomerException {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    Customer actualCustomer = entityManager.find(Customer.class, customerid);
    if (actualCustomer != null) {
      return actualCustomer;
    } else {
      throw new CustomerException("Customer with id " + customerid + " not found");
    }
  }


  /**
   * Delete a customer
   *
   * @param customerId specifies which customer to delete
   * @throws CustomerException if id does not match any existing customer
   */
  public void deleteCustomer(Long customerId) throws CustomerException {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    Customer customer = entityManager.find(Customer.class, customerId);
    if (customer == null) {
      throw new CustomerException("Customer with id " + customerId + " not found");
    }
    entityManager.remove(customer);
    entityManager.getTransaction().commit();
    entityManager.close();

  }

  /**
   * Update a customer
   *
   * @param customerId specifies which object to update
   * @param newCustomer new data
   * @return the customer updated
   * @throws CustomerException if the id does not match any existing customer
   */
  public Customer updateCustomer(Long customerId, Customer newCustomer) throws CustomerException {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    Customer customer = entityManager.find(Customer.class, customerId);
    customer.update(newCustomer);
    entityManager.getTransaction().commit();
    return customer;
  }

  /**
   * Return all existing products
   *
   * @return a list
   */
  public List<Product> getAllProducts() {
    ArrayList<Product> products = new ArrayList<>(this.products.values());
    return products;
  }

  /**
   * Return a product
   *
   * @param productid : specifies which product to return
   * @return an object product
   * @throws ProductException if the id does not match any existing product
   */
  public Product getProductByID(Long productid) throws ProductException {
    Product p = this.products.get(productid);
    if (p != null) {
      return p;
    } else {
      throw new ProductException("does not exist");
    }
  }

  /**
   * Create a new product
   *
   * @return the product created
   * @throws ProductException if a required parameter is null
   */
  public Product createProduct(String productname, String description, String category,
      String price) throws ProductException {
    if (productname != null && description != null && category != null && price != null) {
      Product newProduct = new Product(idCounterProduct++, productname, description, category,
          new BigDecimal(price));
      this.products.put(newProduct.getProductid(), newProduct);
      return this.products.get(newProduct.getProductid());
    } else {
      throw new ProductException("can't be null");
    }
  }

  /**
   * Delete a product
   *
   * @param id of the objet to delete
   * @throws ProductException if the id does not match any existing product
   */
  public void deleteProduct(Long id) throws ProductException {
    Product productScrapped = this.products.remove(id);
    if (productScrapped == null) {
      throw new ProductException("can't be inexistent");
    }
  }

  /**
   * Update a product
   *
   * @param productid : specifies which product to update
   * @param product : new data
   * @return the product object updated
   * @throws ProductException if the id does not match any existing product
   */
  public Product updateProduct(Long productid, Product product) throws ProductException {
    Product p = this.products.get(productid);
    if (p != null) {
      products.put(product.getProductid(), product);
      if (product.getProductid().equals(productid)) {
        deleteProduct(productid);
      }
      return product;
    } else {
      throw new ProductException("can't be inexistent");
    }
  }

  /**
   * Return all orders
   *
   * @return a list of orders
   */
  public List<Order> getAllOrders() {
    ArrayList<Order> orders = new ArrayList<>(this.orders.values());
    return orders;
  }

  /**
   * Return an order
   *
   * @param orderid : specifies which prder to return
   * @return an object order
   * @throws OrderException if the id does not match any existing order
   */
  public Order getOrderById(Long orderid) throws OrderException {
    Order order = this.orders.get(orderid);
    if ((order != null)) {
      return order;
    } else {
      throw new OrderException("does not exist");
    }
  }

  /**
   * Update the status of an order
   *
   * @return the order with its updated status
   * @throws OrderException if the id does not match any existing order
   */
  public Order updateOrder(Long orderid, String status) throws OrderException {
    Order order = this.getOrderById(orderid);
    order.setOrderstatus(status);
    orders.put(orderid, order);
    return order;
  }

  /**
   * Create a new order and check out a cart
   *
   * @param cartid : specifies which cart to checkout
   * @return the new order
   * @throws ParseException if the date does not work
   * @throws OrderException if the id does not match any existing cart or if the cart is empty
   */
  public Order createOrder(Long cartid) throws ParseException, OrderException {
    return new Order();
  }

  /**
   * Get a cart by its ID
   *
   * @param id : specifies which customer owns the cart
   * @param cartid : specifies which cart to return
   * @return the cart
   * @throws StoreException if the cart or customer do not match the IDs
   */
  public Cart getCartById(Long id, Long cartid) throws StoreException {
    Customer customer = customers.get(id);
    // TODO
    return new Cart();
  }

  /**
   * Delete an item from a cart
   *
   * @param customerid : specifies which customer owns the cart
   * @param cartid : specifies which cart contains the item
   * @param cartitemid : specifies which item to remove
   * @throws StoreException if the cartitemid does not match any existing cartitem or the cartid
   * does not match any cart
   */
  public void removeItemFromCart(Long customerid, Long cartid, Long cartitemid)
      throws StoreException {
    Cart cart = getCartById(customerid, cartid);
    if (cart != null) {
      boolean result = cart.removeCartItemById(cartitemid);
      if (!result) {
        throw new StoreException("Item was not found");
      }
    } else {
      throw new StoreException("Cart was not found");
    }
  }

  /**
   * Add an item to a cart
   *
   * @param id : specifies to which customer the cart belongs
   * @param cartid : speficies to which cart the item will be added
   * @param productid : specifies which item to add
   * @return the cart with the new item added
   */
  public Cart addItemToAGivenCart(Long id, Long cartid, Long productid, Long quantity)
      throws StoreException, ProductException {
    Cart cart = getCartById(id, cartid);
    if (cart != null && productid != null && quantity != null) {
      Product product = getProductByID(productid);
      CartItem cartitem = new CartItem(idCounterCartItem++, product, quantity);
      cart.addCartItem(cartitem);
      return cart;
    } else {
      throw new StoreException("Cart was not found");
    }
  }

  /**
   * Update the quantity of an item in a cart
   *
   * @param id : specifies which customer owns the cart
   * @param cartid : specifies in which cart is the item
   * @param productid : specifies which product is to update
   * @param quantity : the new quantity
   * @return the cart with the updated data
   * @throws StoreException if the cart does not contain the product
   * @throws ProductException if the productid does not match any existing product
   */
  public Cart updateCart(Long id, Long cartid, Long productid, Long quantity)
      throws StoreException, ProductException {
    Cart cart = getCartById(id, cartid);
    Product product = getProductByID(productid);
    int position = -1;
    for (int i = 0; i < cart.getContent().size(); i++) {
      if (cart.getContent().get(i).getProduct().equals(product)) {
        position = i;
      }
    }
    if (position != -1) {
      cart.getContent().get(position).setQuantity(quantity);
      return cart;
    } else {
      throw new StoreException("error");
    }
  }

  /**
   * Return the total price of a cart
   *
   * @param id : specifies which customer owns the cart
   * @param cartid : specifies which cart to work with
   * @return the total sum
   * @throws StoreException if the IDs do not match any existing related objects
   */
  public BigDecimal getTotalPrice(Long id, Long cartid) throws StoreException {
    Cart cart = getCartById(id, cartid);
    BigDecimal total = new BigDecimal("0");
    for (int i = 0; i < cart.getContent().size(); i++) {
      total = total.add(cart.getContent().get(i).getProduct().getPrice());
    }
    return total;
  }

  /**
   * Add an item to a new cart
   *
   * @param id : specifies which customer will own the new cart
   * @param productid : specifies which product to add
   * @return the new cart
   * @throws ProductException if the productid does not match any existing product
   * @throws CustomerException if the id does not match any existing customer
   */
  public Cart addItemToNewCart(Long id, Long productid, Long quantity)
      throws ProductException, CustomerException {
    Product product = products.get(productid);
    if (product != null && productid != null && quantity != null) {
      CartItem cartitem = new CartItem(idCounterCartItem++, product, quantity);

      Customer customer = this.getCustomerByID(id);
      Cart cart = new Cart(idCounterCart++);
      cart.addCartItem(cartitem);
      customer.addCart(cart);
      carts.put(cart.getCartid(), cart);
      return cart;
    } else {
      throw new ProductException("can't be inexistent");
    }

  }

  private void populateMockPersistence() {
    customers = new HashMap<>();
    products = new HashMap<>();
    orders = new HashMap<>();
    carts = new HashMap<>();

    //Création et mappage Customer

    Customer c0 = new Customer(idCounterCustomer++, "agueniat", "Adeline", "Gueniat",
        "adeline.gueniat@he-arc.ch", "032 321 12 23");
    Customer c1 = new Customer(idCounterCustomer++, "jdubois", "Joyce", "Dubois",
        "joyce.dubois@he-arc.ch", "032 456 45 56");
    Customer c2 = new Customer(idCounterCustomer++, "urosselet", "Ulysse", "Rosselet",
        "ulysse.rosselet@he-arc.ch", "032 789 78 89");
    Customer c3 = new Customer(idCounterCustomer++, "mschaffter", "Myriam", "Schaffter",
        "myriam.schaffter@he-arc.ch", "032 852 25 58");
    Customer c4 = new Customer(idCounterCustomer++, "nobody", "john", "doe", "john@doe.com",
        "000 00 000 00"); // Pour delete
    Customer c5 = new Customer(idCounterCustomer++, "nobodette", "jane", "doe", "jane@doe.com",
        "000 00 000 00"); // Pour update

    customers.put(c0.getCustomerId(), c0);
    customers.put(c1.getCustomerId(), c1);
    customers.put(c2.getCustomerId(), c2);
    customers.put(c3.getCustomerId(), c3);
    customers.put(c4.getCustomerId(), c4);
    customers.put(c5.getCustomerId(), c5);

// Création et mappage Product

    Product p0 = new Product(idCounterProduct++, "Lifeprint",
        "Lifeprint est une imprimante photo bluetooth à réalité augmentée qui s’accompagne d’une app gratuite et qui vous permet d’imprimer instantanément vos photos et vidéos préférées aussi bien depuis la bibliothèque de votre smartphone que depuis vos réseaux sociaux (Snapchat, Twitter, Facebook, Instagram, …)",
        "High-Tech", BigDecimal.valueOf(149.50));
    Product p1 = new Product(idCounterProduct++, "GARDENA R40 Li",
        "La tondeuse robot GARDENA tond la pelouse toute seule et va se recharger automatiquement à sa station de charge. Vous pouvez profiter d'une pelouse toujours impeccable et également de votre temps libre.",
        "Maison et jardin", BigDecimal.valueOf(999.95));
    Product p2 = new Product(idCounterProduct++, "PANASONIC SC-SB10",
        "La barre de son SB10 est dotée de deux haut-parleurs (2x20W) + 1 subwoofer intégré (10W). Combinées, ces unités empliront votre salon d'un son net, riche et arrondi, beaucoup plus dynamique et immersif que ce que vous offrent habituellement les haut-parleurs des téléviseurs.",
        "High-Tech", BigDecimal.valueOf(234.90));
    Product p3 = new Product(idCounterProduct++, "POC Tectal 2018",
        "Plus couvrant que les casques VTT classiques, le casque POC Tectal est conçu pour allier protection optimale et ventilation efficace. Dessiné pour les parcours exigeants et les courses d'endurance, il est doté d'un revêtement en EPS renforcé, offrant au pilote une forte protection.",
        "Sports", BigDecimal.valueOf(212.00));
    Product p4 = new Product(idCounterProduct++,
        "MOULINEX Robot pâtissier Masterchef Gourmet Premium",
        "Le robot pâtissier Masterchef Gourmet allie l'utile à l'agréable avec des fonctionnalités essentielles et un design élégant. Doté d'un choix de 8 vitesses, ce robot va vite devenir l'outil indispensable de votre cuisine, celui dont on se sert quotidiennement et que l'on ne souhaite jamais ranger.",
        "Électroménager", BigDecimal.valueOf(328.95));
    Product p5 = new Product(idCounterProduct++, "VTT SANTA CRUZ HIGHTOWER Carbone CC 27,5+ XX1",
        "Le VTT SANTA CRUZ Hightower a été conçu pour procurer une grande polyvalence qui ravira les amateurs de All Mountain et d'Enduro comme les accros de DH. Dans cette version, il est monté avec des roues au standard 27,5\"+ permettant de monter des pneus de section plus importante - jusqu'à 3 pouces - pour un grip exceptionnel en toutes circonstances.",
        "Sports", BigDecimal.valueOf(10697.50));
    Product p6 = new Product(idCounterProduct++, "JOOKI Smart music player",
        "L'enceinte Jooki est fournie avec un lot de 5 figurines colorées, un câble de chargement USB et un manuel d'utilisateur.",
        "High-Tech", BigDecimal.valueOf(249.00));
    Product p7 = new Product(idCounterProduct++, "CIGAR CITY Floride 12x35cl",
        "Cigar City est une des brasseries les plus célèbres des Etats Unis et se voit régulièrement dans le groupe de tête lors des rankings.",
        "Alimentation", BigDecimal.valueOf(39.00));
    Product p8 = new Product(idCounterProduct++, "TOTTORI Blended Japanese Whisky",
        "Tottori est une gamme de whisky créée par la distillerie Matsui Shuzo située dans la préfecture de Tottori, au nord-est d'Hiroshima, sur la rive de la mer du Japon. Créé pour célébrer la beauté préservée de cette province rurale, cette bouteille est un assemblage de plusieurs whisky ayant nécessité l'eau pure des cours d'eau naturels de Tottori et reflète l'expertise de Matsui depuis plus de 100 ans dans l'art du vieillissement et de l'assemblage des spiritueux.",
        "Alimentation", BigDecimal.valueOf(55.00));
    Product p9 = new Product(idCounterProduct++, "CUISINART Multi Griddler",
        "Multi Griddler, c’est le petit malin de la collection Griddler. Ses spécialités ? Grillades bien saisies, brochettes parfumées, gaufres croustillantes, sandwichs triangles. Il puise son inspiration des saveurs du monde entier, pour régaler jeunes et petites familles de ses créations inspirées de la street food.",
        "Eléctroménager", BigDecimal.valueOf(114.95));
    Product p10 = new Product(idCounterProduct++, "Maillot ONEAL ELEMENT RACEWEAR ",
        "La performance devient votre seul souci avec ce maillot Element Racewear Femme Manches Longues, développé pour vous par la marque O'NEAL. Celui-ci est idéal pour les sportives qui se penchent sur leur guidon pour plus d'aérodynamisme. La coupe, plus longue à l'arrière qu'à l'avant, fixe le maillot dans le short/pantalon sans friper. Des protections rembourrées ont même été rajoutées aux coudes, pour limiter l'usure ! Avec son col en V très flexible et son jersey, c'est un vêtement aéré et très agréable d'utilisation. Celui-ci vous laissera sur la peau une sensation de fraîcheur, même dans les efforts les plus intenses.",
        "Sports", BigDecimal.valueOf(42.50));
    Product p11 = new Product(idCounterProduct++, "Produit à supprimer",
        "Ce produit n'existe que dans le but de pouvoir le supprimer dans Postman lors du run de la collection",
        "Autre", BigDecimal.valueOf(10.00));
    Product p12 = new Product(idCounterProduct++, "Produit à updater",
        "Ce produit n'existe que dans le but de pouvoir le mettre à jour dans Postman lors du run de la collection",
        "Autre", BigDecimal.valueOf(10.00));

    products.put(p0.getProductid(), p0);
    products.put(p1.getProductid(), p1);
    products.put(p2.getProductid(), p2);
    products.put(p3.getProductid(), p3);
    products.put(p4.getProductid(), p4);
    products.put(p5.getProductid(), p5);
    products.put(p6.getProductid(), p6);
    products.put(p7.getProductid(), p7);
    products.put(p8.getProductid(), p8);
    products.put(p9.getProductid(), p9);
    products.put(p10.getProductid(), p10);
    products.put(p11.getProductid(), p11);
    products.put(p12.getProductid(), p12);

    //Création des Cart et ajout d'item

    Cart ca0 = new Cart(idCounterCart++); // check_out
    Cart ca1 = new Cart(idCounterCart++); // open
    Cart ca2 = new Cart(idCounterCart++); // open
    Cart ca3 = new Cart(idCounterCart++); // check_out
    Cart ca4 = new Cart(idCounterCart++); // open

    ca0.addCartItem(
        new CartItem(idCounterCartItem++, (this.products.get(p0.getProductid())), (long) 1));
    ca0.addCartItem(
        new CartItem(idCounterCartItem++, (this.products.get(p7.getProductid())), (long) 1));
    ca0.setCartstatus(CHECK_OUT.toString());

    ca1.addCartItem(
        new CartItem(idCounterCartItem++, (this.products.get(p1.getProductid())), (long) 2));
    ca1.addCartItem(
        new CartItem(idCounterCartItem++, (this.products.get(p8.getProductid())), (long) 1));

    ca2.addCartItem(
        new CartItem(idCounterCartItem++, (this.products.get(p8.getProductid())), (long) 2));
    ca2.addCartItem(
        new CartItem(idCounterCartItem++, (this.products.get(p4.getProductid())), (long) 1));
    ca2.addCartItem(
        new CartItem(idCounterCartItem++, (this.products.get(p2.getProductid())), (long) 1));
    ca2.addCartItem(
        new CartItem(idCounterCartItem++, (this.products.get(p0.getProductid())), (long) 1));

    ca3.addCartItem(
        new CartItem(idCounterCartItem++, (this.products.get(p6.getProductid())), (long) 1));
    ca3.addCartItem(
        new CartItem(idCounterCartItem++, (this.products.get(p7.getProductid())), (long) 2));
    ca3.addCartItem(
        new CartItem(idCounterCartItem++, (this.products.get(p9.getProductid())), (long) 1));
    ca3.setCartstatus(CHECK_OUT.toString());

    ca4.addCartItem(
        new CartItem(idCounterCartItem++, (this.products.get(p5.getProductid())), (long) 1));
    ca4.addCartItem(
        new CartItem(idCounterCartItem++, (this.products.get(p3.getProductid())), (long) 1));
    ca4.addCartItem(
        new CartItem(idCounterCartItem++, (this.products.get(p10.getProductid())), (long) 3));

    carts.put(ca0.getCartid(), ca0);
    carts.put(ca1.getCartid(), ca1);
    carts.put(ca2.getCartid(), ca2);
    carts.put(ca3.getCartid(), ca3);
    carts.put(ca4.getCartid(), ca4);

    //Attribution des carts aux customers
    c0.addCart(ca0);
    c0.addCart(ca4);
    c1.addCart(ca1);
    c2.addCart(ca2);
    c3.addCart(ca3);

    // Création et mappage Order

    Order o0 = null;
    try {
      o0 = new Order(idCounterOrder++, dateFormat.parse("12.02.2018"));
    } catch (ParseException e) {
      e.printStackTrace();
    }
    o0.addOrderLine(
        new OrderLine(idCounterOrderLine++, (this.products.get(p0.getProductid())), (long) 1));
    o0.addOrderLine(
        new OrderLine(idCounterOrderLine++, (this.products.get(p7.getProductid())), (long) 1));
    o0.setOrderstatus(Orderstatus.PAID.toString());

    Order o1 = null;
    try {
      o1 = new Order(idCounterOrder++, dateFormat.parse("01.05.2018"));
    } catch (ParseException e) {
      e.printStackTrace();
    }
    o1.addOrderLine(
        new OrderLine(idCounterOrderLine++, (this.products.get(p6.getProductid())), (long) 1));
    o1.addOrderLine(
        new OrderLine(idCounterOrderLine++, (this.products.get(p7.getProductid())), (long) 2));
    o1.addOrderLine(
        new OrderLine(idCounterOrderLine++, (this.products.get(p9.getProductid())), (long) 1));
    o1.setOrderstatus(Orderstatus.OPEN.toString());

    orders.put(o0.getOrderid(), o0);
    orders.put(o1.getOrderid(), o1);

    // Attribution des orders aux customers
    c0.addOrder(orders.get(o0.getOrderid()));
    c3.addOrder(orders.get(o1.getOrderid()));

  }


  public List<Cart> getOpenCartsForCustomer(Long customerid) throws StoreException {
    // TODO
    return new ArrayList<Cart>();
  }
}





