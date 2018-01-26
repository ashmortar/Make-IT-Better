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

//    @Test
//    public void findByIdFindsCorrect() throws Exception {
//        Business business = setupBusiness();
//        Business business1 = setupBusiness1();
//        businessDao.add(business);
//        businessDao.add(business1);
//        int businessId = business.getId();
//        int businessId1 = business1.getId();
//        assertEquals(business, businessDao.findById(businessId));
//        assertEquals(business1, businessDao.findById(businessId1));
//    }
//
//    @Test
//    public void getAllGetsAll() throws Exception {
//        Business business = setupBusiness();
//        Business business1 = setupBusiness1();
//        businessDao.add(business);
//        businessDao.add(business1);
//        assertEquals(2, businessDao.getAll().size());
//        assertTrue(businessDao.getAll().contains(business));
//        assertTrue(businessDao.getAll().contains(business1));
//    }
//
//    @Test
//    public void getAllAdressesForBusinessGetsAll() throws Exception {
//        Business business = setupBusiness();
//        Address address = new Address("a", "a", "a", "a");
//        businessDao.add(business);
//        addressDao.add(address);
//        Address address1 = new Address("gf", "iaoerjtoia", "areioj", "345w2");
//        addressDao.add(address1);
//        Address controlAddress = new Address("1", "2", "3", "4");
//        addressDao.add(controlAddress);
//        businessDao.addAddressToBusiness(business, address);
//        businessDao.addAddressToBusiness(business, address1);
//        assertEquals(3, addressDao.getAll().size());
//        assertEquals(2, businessDao.getAllAddressesForABusiness(business.getId()).size());
//        assertFalse(businessDao.getAllAddressesForABusiness(business.getId()).contains(controlAddress));
//
//    }
//
//    @Test
//    public void getAllCausesForBusiness_returnsAllCausesAssociatedWithThatBusiness_true() throws Exception {
//        Business testBusiness = setupBusiness();
//        businessDao.add(testBusiness);
//        Cause testCause1 = new Cause("a", "lkjsf", "lksdf", "lkasdf");
//        Cause testCause2 = new Cause("b", "lsfdj", "oien", "nwoisd");
//        Cause controlCause = new Cause("c", "sdoi", "qoic", "k,mi");
//        causeDao.add(testCause1);
//        causeDao.add(testCause2);
//        causeDao.add(controlCause);
//        businessDao.addCauseToBusiness(testBusiness, testCause1);
//        businessDao.addCauseToBusiness(testBusiness, testCause2);
//        assertEquals(2, businessDao.getAllCausesForABusiness(testBusiness.getId()).size());
//        assertFalse(businessDao.getAllCausesForABusiness(testBusiness.getId()).contains(controlCause));
//    }
//
//    @Test
//    public void updateUpdatesInfo() throws Exception {
//        Business business = setupBusiness();
//        Business business1 = setupBusiness1();
//        businessDao.add(business);
//        businessDao.add(business1);
//        businessDao.update(1, "a", "b", "c", "d");
//        businessDao.update(2, "z", "x", "v", "q");
//        assertEquals("a", businessDao.findById(1).getName());
//        assertEquals("z", businessDao.findById(2).getName());
//        assertEquals("b", businessDao.findById(1).getType());
//        assertEquals("x", businessDao.findById(2).getType());
//    }
//
//
//    @Test
//    public void deleteByIdDeletesCorrectly() throws Exception {
//        Business business = setupBusiness();
//        Business business1 = setupBusiness1();
//        businessDao.add(business);
//        businessDao.add(business1);
//        businessDao.deleteById(1);
//        assertFalse(businessDao.getAll().contains(business));
//        assertEquals(1, businessDao.getAll().size());
//    }
//
//    @Test
//    public void deleteAllDeletesAll() throws Exception {
//        Business business = setupBusiness();
//        Business business1 = setupBusiness1();
//        businessDao.add(business);
//        businessDao.add(business1);
//        assertEquals(2, businessDao.getAll().size());
//        businessDao.deleteAll();
//        assertEquals(0, businessDao.getAll().size());
//    }
//
}