package dao;
import models.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;


import java.sql.BatchUpdateException;

import static org.junit.Assert.*;

public class Sql2oBusinessDaoTest {

    private Connection connection;
    private Sql2oBusinessDao businessDao;
    private Sql2oAddressDao addressDao;
    private  Sql2oCauseDao causeDao;

    public Business setupBakery() {
        return new Bakery("Little Red's Bakeshop and Cafe", "123-456-7890", "littlereds.com", "7am to 3pm", "Pastries and Cakes", false);
    }

    public Business setupBar() {
        return new Bar("McMenamins anywhere", "123-456-7890", "theirwebsitehere.com", "12pm to 1am", "Varies by location", "casual", true, true);
    }

    public Business setupCafe() {
        return new Cafe("Coffee Time", "123-456-7890", "maybecoffeetime.com", "5am - 3pm", "Pastries and snacks", true);
    }

    public Business setupRestaurant() {
        return new Restaurant("Shigezo", "123-456-7890", "here.com", "12pm - 11pm", "japanese izakaya", false, "casual", true);
    }

    @Before
    public void setUp() throws Exception {
        String connectionString  = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/create.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");
        businessDao = new Sql2oBusinessDao(sql2o);
        addressDao = new Sql2oAddressDao(sql2o);
        causeDao = new Sql2oCauseDao(sql2o);
        connection = sql2o.open();
    }

    @After
    public void tearDown() throws Exception {
        connection.close();
    }

    @Test
    public void add_SetsIdForAllBusinessTypes_true() throws Exception {
        Business bakery = setupBakery();
        int originalBakeryId = bakery.getId();
        businessDao.add(bakery);
        Business bar = setupBar();
        int originalBarId = bar.getId();
        businessDao.add(bar);
        Business cafe = setupCafe();
        int originalCafeId = cafe.getId();
        businessDao.add(cafe);
        Business restaurant = setupRestaurant();
        int originalRestaurantId = restaurant.getId();
        businessDao.add(restaurant);
        assertNotEquals(originalBakeryId, bakery.getId());
        assertNotEquals(originalBarId, bar.getId());
        assertNotEquals(originalCafeId, cafe.getId());
        assertNotEquals(originalRestaurantId, restaurant.getId());
        assertEquals(10, bakery.getId() + bar.getId() + cafe.getId() + restaurant.getId());
    }

    @Test
    public void addAddressToBusiness_WorksForAllSubtypes_True() throws Exception {
        Business bakery = setupBakery();
        Address bakeryAddress = new Address("a", "a", "a", "a");
        businessDao.add(bakery);
        addressDao.add(bakeryAddress);
        businessDao.addAddressToBusiness(bakery, bakeryAddress);

        Business bar = setupBar();
        Address barAddress = new Address("b", "b", "b", "b");
        businessDao.add(bar);
        addressDao.add(barAddress);
        businessDao.addAddressToBusiness(bar, barAddress);

        Business cafe = setupCafe();
        Address cafeAddress = new Address("c", "c", "c", "c");
        businessDao.add(cafe);
        addressDao.add(cafeAddress);
        businessDao.addAddressToBusiness(cafe, cafeAddress);

        Business restaurant = setupRestaurant();
        Address restaurantAddress = new Address("d", "d", "d", "d");
        businessDao.add(restaurant);
        addressDao.add(restaurantAddress);
        businessDao.addAddressToBusiness(restaurant, restaurantAddress);

        assertEquals(1, businessDao.getAllAddressesForABusiness(bakery.getId()).size());
        assertTrue(businessDao.getAllAddressesForABusiness(bakery.getId()).contains(bakeryAddress));

        assertEquals(1, businessDao.getAllAddressesForABusiness(bar.getId()).size());
        assertTrue(businessDao.getAllAddressesForABusiness(bar.getId()).contains(barAddress));

        assertEquals(1, businessDao.getAllAddressesForABusiness(cafe.getId()).size());
        assertTrue(businessDao.getAllAddressesForABusiness(cafe.getId()).contains(cafeAddress));

        assertEquals(1, businessDao.getAllAddressesForABusiness(restaurant.getId()).size());
        assertTrue(businessDao.getAllAddressesForABusiness(restaurant.getId()).contains(restaurantAddress));
    }

    @Test
    public void addCauseToBusiness_WorksForAllBusinessTypes_true() throws Exception {
        Business bakery = setupBakery();
        Cause bakeryCause = new Cause("a", "a", "a", "a");
        businessDao.add(bakery);
        causeDao.add(bakeryCause);
        businessDao.addCauseToBusiness(bakery, bakeryCause);

        Business bar = setupBar();
        Cause barCause = new Cause("b", "b", "b", "b");
        businessDao.add(bar);
        causeDao.add(barCause);
        businessDao.addCauseToBusiness(bar, barCause);

        Business cafe = setupCafe();
        Cause cafeCause = new Cause("c", "c", "c", "c");
        businessDao.add(cafe);
        causeDao.add(cafeCause);
        businessDao.addCauseToBusiness(cafe, cafeCause);

        Business restaurant = setupRestaurant();
        Cause restaurantCause = new Cause("d", "d", "d", "d");
        businessDao.add(restaurant);
        causeDao.add(restaurantCause);
        businessDao.addCauseToBusiness(restaurant, restaurantCause);

        assertEquals(1, businessDao.getAllCausesForABusiness(bakery.getId()).size());
        assertTrue(businessDao.getAllCausesForABusiness(bakery.getId()).contains(bakeryCause));

        assertEquals(1, businessDao.getAllCausesForABusiness(bar.getId()).size());
        assertTrue(businessDao.getAllCausesForABusiness(bar.getId()).contains(barCause));

        assertEquals(1, businessDao.getAllCausesForABusiness(cafe.getId()).size());
        assertTrue(businessDao.getAllCausesForABusiness(cafe.getId()).contains(cafeCause));

        assertEquals(1, businessDao.getAllCausesForABusiness(restaurant.getId()).size());
        assertTrue(businessDao.getAllCausesForABusiness(restaurant.getId()).contains(restaurantCause));
    }

    @Test
    public void findByIdFindsCorrect() throws Exception {
        Business bakery = setupBakery();
        businessDao.add(bakery);
        Business bar = setupBar();
        businessDao.add(bar);
        Business cafe = setupCafe();
        businessDao.add(cafe);
        Business restaurant = setupRestaurant();
        businessDao.add(restaurant);

        assertEquals(bakery.getName(), businessDao.findById(1).getName());
        assertEquals(bar.getName(), businessDao.findById(2).getName());
        assertEquals(cafe.getName(), businessDao.findById(3).getName());
        assertEquals(restaurant.getName(), businessDao.findById(4).getName());
    }

    @Test
    public void getAll_returnsAllBusinessesRegardlessOfSubtype_true() throws Exception {
        Business bakery = setupBakery();
        businessDao.add(bakery);
        Business bar = setupBar();
        businessDao.add(bar);
        Business cafe = setupCafe();
        businessDao.add(cafe);
        Business restaurant = setupRestaurant();
        businessDao.add(restaurant);
        assertEquals(4, businessDao.getAll().size());
    }

    @Test
    public void getAllBakeries_returnsOnlyBakerySubtype_true() throws Exception {
        Business bakeryOne = setupBakery();
        Business bakeryTwo = setupBakery();
        bakeryTwo.setName("TEST");
        Business notABakery = setupBar();
        businessDao.add(bakeryOne);
        businessDao.add(bakeryTwo);
        businessDao.add(notABakery);
        assertEquals(2, businessDao.getAllBakeries().size());
        assertFalse(businessDao.getAllBakeries().contains(notABakery));
    }

    @Test
    public void getAllBars_returnsOnlyBarSubtype_true() throws Exception {
        Business barOne = setupBar();
        Business barTwo = setupBar();
        barTwo.setName("TEST");
        Business notABar = setupCafe();
        businessDao.add(barOne);
        businessDao.add(barTwo);
        businessDao.add(notABar);
        assertEquals(2, businessDao.getAllBars().size());
        assertFalse(businessDao.getAllBakeries().contains(notABar));
    }

    @Test
    public void getAllCafes_returnsOnlyCafeSubtype_true() throws Exception {
        Business cafeOne = setupCafe();
        Business cafeTwo = setupCafe();
        cafeTwo.setName("TEST");
        Business notACafe = setupBar();
        businessDao.add(cafeOne);
        businessDao.add(cafeTwo);
        businessDao.add(notACafe);
        assertEquals(2, businessDao.getAllCafes().size());
        assertFalse(businessDao.getAllBakeries().contains(notACafe));
    }

    @Test
    public void getAllRestaurants_returnsOnlyRestaurantSubtype_true() throws Exception {
        Business restaurantOne = setupRestaurant();
        Business restaurantTwo = setupRestaurant();
        restaurantTwo.setName("TEST");
        Business notARestaurant = setupBar();
        businessDao.add(restaurantOne);
        businessDao.add(restaurantTwo);
        businessDao.add(notARestaurant);
        assertEquals(2, businessDao.getAllRestaurants().size());
        assertFalse(businessDao.getAllBakeries().contains(notARestaurant));
    }

    @Test
    public void getAllAddressesForBusiness_worksForAllSubtypes_True() throws Exception {
        Business bakery = setupBakery();
        Address bakeryAddress1 = new Address("a", "a", "a", "a");
        Address bakeryAddress2 = new Address("b", "b", "b", "b");
        businessDao.add(bakery);
        addressDao.add(bakeryAddress1);
        addressDao.add(bakeryAddress2);
        businessDao.addAddressToBusiness(bakery, bakeryAddress1);
        businessDao.addAddressToBusiness(bakery, bakeryAddress2);
        assertEquals(2, businessDao.getAllAddressesForABusiness(bakery.getId()).size());
        assertTrue(businessDao.getAllAddressesForABusiness(bakery.getId()).contains(bakeryAddress1));
        assertTrue(businessDao.getAllAddressesForABusiness(bakery.getId()).contains(bakeryAddress2));

        Business bar = setupBar();
        Address barAddress1 = new Address("c", "c", "c", "c");
        Address barAddress2 = new Address("d", "d", "d", "d");
        businessDao.add(bar);
        addressDao.add(barAddress1);
        addressDao.add(barAddress2);
        businessDao.addAddressToBusiness(bar, barAddress1);
        businessDao.addAddressToBusiness(bar, barAddress2);
        assertEquals(2, businessDao.getAllAddressesForABusiness(bar.getId()).size());
        assertTrue(businessDao.getAllAddressesForABusiness(bar.getId()).contains(barAddress1));
        assertTrue(businessDao.getAllAddressesForABusiness(bar.getId()).contains(barAddress2));

        Business cafe = setupCafe();
        Address cafeAddress1 = new Address("e", "e", "e", "e");
        Address cafeAddress2 = new Address("f", "f", "f", "f");
        businessDao.add(cafe);
        addressDao.add(cafeAddress1);
        addressDao.add(cafeAddress2);
        businessDao.addAddressToBusiness(cafe, cafeAddress1);
        businessDao.addAddressToBusiness(cafe, cafeAddress2);
        assertEquals(2, businessDao.getAllAddressesForABusiness(cafe.getId()).size());
        assertTrue(businessDao.getAllAddressesForABusiness(cafe.getId()).contains(cafeAddress1));
        assertTrue(businessDao.getAllAddressesForABusiness(cafe.getId()).contains(cafeAddress2));

        Business restaurant = setupRestaurant();
        Address restaurantAddress1 = new Address("g", "g", "g", "g");
        Address restaurantAddress2 = new Address("h", "h", "h", "h");
        businessDao.add(restaurant);
        addressDao.add(restaurantAddress1);
        addressDao.add(restaurantAddress2);
        businessDao.addAddressToBusiness(restaurant, restaurantAddress1);
        businessDao.addAddressToBusiness(restaurant, restaurantAddress2);
        assertEquals(2, businessDao.getAllAddressesForABusiness(restaurant.getId()).size());
        assertTrue(businessDao.getAllAddressesForABusiness(restaurant.getId()).contains(restaurantAddress1));
        assertTrue(businessDao.getAllAddressesForABusiness(restaurant.getId()).contains(restaurantAddress2));

    }

    @Test
    public void getAllCausesForBusiness_returnsAllCausesAssociatedWithThatBusiness_true() throws Exception {
        Business bakery = setupBakery();
        Cause bakeryCause1 = new Cause("a", "a", "a", "a");
        Cause bakeryCause2 = new Cause("b", "b", "b", "b");
        businessDao.add(bakery);
        causeDao.add(bakeryCause1);
        causeDao.add(bakeryCause2);
        businessDao.addCauseToBusiness(bakery, bakeryCause1);
        businessDao.addCauseToBusiness(bakery, bakeryCause2);
        assertEquals(2, businessDao.getAllCausesForABusiness(bakery.getId()).size());
        assertTrue(businessDao.getAllCausesForABusiness(bakery.getId()).contains(bakeryCause1));
        assertTrue(businessDao.getAllCausesForABusiness(bakery.getId()).contains(bakeryCause2));

        Business bar = setupBar();
        Cause barCause1 = new Cause("c", "c", "c", "c");
        Cause barCause2 = new Cause("d", "d", "d", "d");
        businessDao.add(bar);
        causeDao.add(barCause1);
        causeDao.add(barCause2);
        businessDao.addCauseToBusiness(bar, barCause1);
        businessDao.addCauseToBusiness(bar, barCause2);
        assertEquals(2, businessDao.getAllCausesForABusiness(bar.getId()).size());
        assertTrue(businessDao.getAllCausesForABusiness(bar.getId()).contains(barCause1));
        assertTrue(businessDao.getAllCausesForABusiness(bar.getId()).contains(barCause2));

        Business cafe = setupCafe();
        Cause cafeCause1 = new Cause("e", "e", "e", "e");
        Cause cafeCause2 = new Cause("f", "f", "f", "f");
        businessDao.add(cafe);
        causeDao.add(cafeCause1);
        causeDao.add(cafeCause2);
        businessDao.addCauseToBusiness(cafe, cafeCause1);
        businessDao.addCauseToBusiness(cafe, cafeCause2);
        assertEquals(2, businessDao.getAllCausesForABusiness(cafe.getId()).size());
        assertTrue(businessDao.getAllCausesForABusiness(cafe.getId()).contains(cafeCause1));
        assertTrue(businessDao.getAllCausesForABusiness(cafe.getId()).contains(cafeCause2));

        Business restaurant = setupRestaurant();
        Cause restaurantCause1 = new Cause("g", "g", "g", "g");
        Cause restaurantCause2 = new Cause("h", "h", "h", "h");
        businessDao.add(restaurant);
        causeDao.add(restaurantCause1);
        causeDao.add(restaurantCause2);
        businessDao.addCauseToBusiness(restaurant, restaurantCause1);
        businessDao.addCauseToBusiness(restaurant, restaurantCause2);
        assertEquals(2, businessDao.getAllCausesForABusiness(restaurant.getId()).size());
        assertTrue(businessDao.getAllCausesForABusiness(restaurant.getId()).contains(restaurantCause1));
        assertTrue(businessDao.getAllCausesForABusiness(restaurant.getId()).contains(restaurantCause2));
    }

    @Test
    public void updateUpdatesInfo() throws Exception {
        Business bakery = setupBakery();
        Business bar = setupBar();
        Business cafe = setupCafe();
        Business restaurant = setupRestaurant();
        businessDao.add(bakery);
        businessDao.add(bar);
        businessDao.add(cafe);
        businessDao.add(restaurant);
        Business bakeryUpdate = new Bakery("a", "a", "a", "a", "a" ,false);
        Business barUpdate = new Bar("b", "b", "b", "b", "b", "b", false, false);
        Business cafeUpdate = new Cafe("c", "c", "c", "c", "c", true);
        Business restaurantUpdate = new Restaurant("d", "d", "d", "d", "d", true, "d", false);
        businessDao.update(1, bakeryUpdate);
        businessDao.update(2, barUpdate);
        businessDao.update(3, cafeUpdate);
        businessDao.update(4, restaurantUpdate);
        assertEquals("a", businessDao.findById(1).getName());
        assertEquals("b", businessDao.findById(2).getName());
        assertEquals("c", businessDao.findById(3).getName());
        assertEquals("d", businessDao.findById(4).getName());
    }


    @Test
    public void deleteByIdDeletesCorrectly() throws Exception {
        Business bakery = setupBakery();
        Business bar = setupBar();
        Business cafe = setupCafe();
        Business restaurant = setupRestaurant();
        businessDao.add(bakery);
        businessDao.add(bar);
        businessDao.add(cafe);
        businessDao.add(restaurant);
        assertEquals(4, businessDao.getAll().size());
        businessDao.deleteById(1);
        assertEquals(3, businessDao.getAll().size());
        assertFalse(businessDao.getAll().contains(bakery));
        businessDao.deleteById(2);
        assertEquals(2, businessDao.getAll().size());
        assertFalse(businessDao.getAll().contains(bar));
        businessDao.deleteById(3);
        assertEquals(1, businessDao.getAll().size());
        assertFalse(businessDao.getAll().contains(cafe));
        businessDao.deleteById(4);
        assertEquals(0, businessDao.getAll().size());
        assertFalse(businessDao.getAll().contains(restaurant));
    }

    @Test
    public void deleteAllBakeries_removesAllBakerySubtypeBusiness_true() {
        Business bakeryOne = setupBakery();
        Business bakeryTwo = setupBakery();
        bakeryTwo.setName("TEST");
        Business notABakery = setupBar();
        businessDao.add(bakeryOne);
        businessDao.add(bakeryTwo);
        businessDao.add(notABakery);
        assertEquals(3, businessDao.getAll().size());
        assertEquals(2, businessDao.getAllBakeries().size());
        businessDao.deleteAllBakeries();
        assertEquals(1, businessDao.getAll().size());
        assertEquals(0, businessDao.getAllBakeries().size());
    }

    @Test
    public void deleteAllBars_removesAllBarSubtypeBusiness_true() {
        Business barOne = setupBar();
        Business barTwo = setupBar();
        barTwo.setName("TEST");
        Business notABar = setupCafe();
        businessDao.add(barOne);
        businessDao.add(barTwo);
        businessDao.add(notABar);
        assertEquals(3, businessDao.getAll().size());
        assertEquals(2, businessDao.getAllBars().size());
        businessDao.deleteAllBars();
        assertEquals(1, businessDao.getAll().size());
        assertEquals(0, businessDao.getAllBars().size());
    }

    @Test
    public void deleteAllCafes_removesAllCafeSubtypeBusiness_true() {
        Business cafeOne = setupCafe();
        Business cafeTwo = setupCafe();
        cafeTwo.setName("TEST");
        Business notACafe = setupBar();
        businessDao.add(cafeOne);
        businessDao.add(cafeTwo);
        businessDao.add(notACafe);
        assertEquals(3, businessDao.getAll().size());
        assertEquals(2, businessDao.getAllCafes().size());
        businessDao.deleteAllCafes();
        assertEquals(1, businessDao.getAll().size());
        assertEquals(0, businessDao.getAllCafes().size());
    }

    @Test
    public void deleteAllRestaurants_removesAllRestaurantSubtypeBusiness_true() {
        Business restaurantOne = setupRestaurant();
        Business restaurantTwo = setupRestaurant();
        restaurantTwo.setName("TEST");
        Business notARestaurant = setupBar();
        businessDao.add(restaurantOne);
        businessDao.add(restaurantTwo);
        businessDao.add(notARestaurant);
        assertEquals(3, businessDao.getAll().size());
        assertEquals(2, businessDao.getAllRestaurants().size());
        businessDao.deleteAllRestaurants();
        assertEquals(1, businessDao.getAll().size());
        assertEquals(0, businessDao.getAllRestaurants().size());
    }

    @Test
    public void deleteAllDeletesAll() throws Exception {
        Business bakery = setupBakery();
        Business bar = setupBar();
        Business cafe = setupCafe();
        Business restaurant = setupRestaurant();
        businessDao.add(bakery);
        businessDao.add(bar);
        businessDao.add(cafe);
        businessDao.add(restaurant);
        assertEquals(4, businessDao.getAll().size());
        businessDao.deleteAll();
        assertEquals(0, businessDao.getAll().size());
    }

}