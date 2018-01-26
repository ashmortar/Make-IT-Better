package dao;

import models.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;



import static org.junit.Assert.*;

public class Sql2oCauseDaoTest {

    private Connection fred;
    private Sql2oCauseDao causeDao;
    private Sql2oBusinessDao businessDao;
    private Sql2oAddressDao addressDao;

    public Cause setupCause() {
        return new Cause("name", "type", "description", "phone");
    }

    public Cause setupCause1() {
        return new Cause("indigo", "charity", "we do stufF", "callme");
    }
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
        causeDao = new Sql2oCauseDao(sql2o);
        businessDao = new Sql2oBusinessDao(sql2o);
        addressDao = new Sql2oAddressDao(sql2o);
        fred = sql2o.open();
    }

    @After
    public void tearDown() throws Exception {
        fred.close();
    }

    @Test
    public void add_setsUniqueID_true() throws Exception {
        Cause cause = setupCause();
        int originalId = cause.getId();
        causeDao.add(cause);
        assertNotEquals(originalId, cause.getId());
    }

    @Test
    public void addAddressToCauseDoesRightThingies() throws Exception {
        Cause cause = setupCause();
        Cause cause1 = setupCause1();
        Address address = new Address("arew", "haierug", "raetho", "aereilr");
        Address address1 = new Address("ggg", "g", "f", "ggggggg");

        causeDao.add(cause);
        causeDao.add(cause1);

        addressDao.add(address);
        addressDao.add(address1);

        causeDao.addAddressToCause(cause, address);
        causeDao.addAddressToCause(cause1, address);
        causeDao.addAddressToCause(cause, address1);

        assertEquals(2, causeDao.getAllAddressesForCause(cause.getId()).size());
        assertTrue(causeDao.getAllAddressesForCause(cause.getId()).contains(address));
        assertTrue(causeDao.getAllAddressesForCause(cause.getId()).contains(address1));
        assertFalse(causeDao.getAllAddressesForCause(cause1.getId()).contains(address1));
    }

    @Test
    public void findById_returnsCorrectInstanceOfCause_true() throws Exception {
        Cause testCause = setupCause();
        Cause controlCause = setupCause1();
        causeDao.add(testCause);
        causeDao.add(controlCause);
        assertEquals("name", causeDao.findById(1).getName());
        assertEquals("indigo", causeDao.findById(2).getName());
    }

    @Test
    public void getAll_returnsAllInstancesOfCauseInDB_true() throws Exception {
        Cause testCause = setupCause();
        testCause.setName("test");
        causeDao.add(testCause);
        Cause testCause2 = setupCause1();
        causeDao.add(testCause2);
        Cause controlCause = setupCause();
        assertEquals(2, causeDao.getAll().size());
        assertFalse(causeDao.getAll().contains(controlCause));
    }

    @Test
    public void getAllBusinessesForCause_returnsAllAssociateBusinesses_true() throws Exception {
        Business testBusiness = setupBakery();
        Business testBusiness1 = setupBar();
        Business controlBusiness = setupRestaurant();
        controlBusiness.setName("fred");
        businessDao.add(testBusiness);
        businessDao.add(testBusiness1);
        businessDao.add(controlBusiness);
        Cause testCause = setupCause();
        Cause controlCause = setupCause1();
        causeDao.add(testCause);
        causeDao.add(controlCause);
        businessDao.addCauseToBusiness(testBusiness, testCause);
        businessDao.addCauseToBusiness(testBusiness1, testCause);
        assertEquals(2, causeDao.getAllBusinessesForCause(testCause.getId()).size());
        assertEquals(0, causeDao.getAllBusinessesForCause(controlCause.getId()).size());
        assertFalse(causeDao.getAllBusinessesForCause(testCause.getId()).contains(controlBusiness));

    }

    @Test
    public void getAllAddressesForACauseGetsRightStuff() throws Exception {
        Address testAddress = new Address("arew", "haierug", "raetho", "aereilr");
        Address testAddress1 = new Address("9009 poop st", "pee", "BR", "93333");
        Address controlAddress = new Address("ggg", "g", "f", "ggggggg");
        addressDao.add(testAddress);
        addressDao.add(controlAddress);

        Cause testCause = setupCause();
        Cause controlCause = setupCause1();
        causeDao.add(testCause);
        causeDao.add(controlCause);

        causeDao.addAddressToCause(testCause, testAddress);
        causeDao.addAddressToCause(testCause, testAddress1);
        causeDao.addAddressToCause(controlCause, controlAddress);

        assertEquals(2, causeDao.getAllAddressesForCause(testCause.getId()).size());
        assertFalse(causeDao.getAllAddressesForCause(testCause.getId()).contains(controlAddress));

    }

    @Test
    public void update_ChangesAllValuesCorrectly_true() throws Exception {
        Cause testCause = setupCause();
        Cause controlCause = setupCause1();
        causeDao.add(testCause);
        causeDao.add(controlCause);
        causeDao.update(testCause.getId(), "newName", "newType", "newDesc", "newPhone");
        assertEquals("newName", causeDao.findById(testCause.getId()).getName());
        assertEquals("newType", causeDao.findById(testCause.getId()).getType());
        assertEquals("newDesc", causeDao.findById(testCause.getId()).getDescription());
        assertEquals("newPhone", causeDao.findById(testCause.getId()).getPhone());

        assertEquals("indigo", causeDao.findById(controlCause.getId()).getName());
    }

    @Test
    public void deleteById_removesCorrectInstance_true() throws Exception {
        Cause testCause = setupCause();
        Cause controlCause = setupCause1();
        causeDao.add(testCause);
        causeDao.add(controlCause);
        causeDao.deleteById(testCause.getId());
        assertEquals(1, causeDao.getAll().size());
        assertTrue(causeDao.getAll().contains(controlCause));
    }

    @Test
    public void deleteAll_removesAllInstances_true() throws Exception {
        Cause testCause = setupCause();
        Cause secondTEst = setupCause1();
        causeDao.add(testCause);
        causeDao.add(secondTEst);
        assertEquals(2, causeDao.getAll().size());
        causeDao.deleteAll();
        assertEquals(0, causeDao.getAll().size());
    }
}