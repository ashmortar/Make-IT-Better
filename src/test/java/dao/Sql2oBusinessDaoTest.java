package dao;
import models.Address;
import models.Business;
import models.Cause;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;


import java.sql.BatchUpdateException;

import static org.junit.Assert.*;

public class Sql2oBusinessDaoTest {

    private Connection fred;
    private Sql2oBusinessDao businessDao;
    private Sql2oAddressDao addressDao;
    private  Sql2oCauseDao causeDao;

    public Business setupBusiness() {
        return new Business("poop", "pee", "12345", "poop@poop.com");
    }

    public Business setupBusiness1() {
        return new Business("Evil Corp", "Evil", "666-666-6666", "evil@corporation.com");
    }

    @Before
    public void setUp() throws Exception {
        String connectionString  = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/create.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");
        businessDao = new Sql2oBusinessDao(sql2o);
        addressDao = new Sql2oAddressDao(sql2o);
        causeDao = new Sql2oCauseDao(sql2o);
        fred = sql2o.open();
    }

    @After
    public void tearDown() throws Exception {
        fred.close();
    }

    @Test
    public void addsCorrectly() throws Exception {
        Business business = setupBusiness();
        int originalId = business.getId();
        businessDao.add(business);
        assertNotEquals(originalId, business.getId());
    }

    @Test
    public void addAddressToBusiness() throws Exception {
        Business business = setupBusiness();
        Address address = new Address("a", "a", "a", "a");
        businessDao.add(business);
        addressDao.add(address);
        businessDao.addAddressToBusiness(business, address);
        assertEquals(1, businessDao.getAllAddressesForABusiness(business.getId()).size());
        assertTrue(businessDao.getAllAddressesForABusiness(business.getId()).contains(address));
    }

    @Test
    public void addCauseToBusinessFreakingWorks() throws Exception {
        Business business = setupBusiness();
        Cause cause = new Cause("eafw", "fsdjlk", "saf", "ssklj");
        businessDao.add(business);
        causeDao.add(cause);
        businessDao.addCauseToBusiness(business, cause);
        assertEquals(1, businessDao.getAllCausesForABusiness(business.getId()).size());
        assertTrue(businessDao.getAllCausesForABusiness(business.getId()).contains(cause));
    }

    @Test
    public void findByIdFindsCorrect() throws Exception {
        Business business = setupBusiness();
        Business business1 = setupBusiness1();
        businessDao.add(business);
        businessDao.add(business1);
        int businessId = business.getId();
        int businessId1 = business1.getId();
        assertEquals(business, businessDao.findById(businessId));
        assertEquals(business1, businessDao.findById(businessId1));
    }

    @Test
    public void getAllGetsAll() throws Exception {
        Business business = setupBusiness();
        Business business1 = setupBusiness1();
        businessDao.add(business);
        businessDao.add(business1);
        assertEquals(2, businessDao.getAll().size());
        assertTrue(businessDao.getAll().contains(business));
        assertTrue(businessDao.getAll().contains(business1));
    }

    @Test
    public void getAllAdressesForBusinessGetsAll() throws Exception {
        Business business = setupBusiness();
        Address address = new Address("a", "a", "a", "a");
        businessDao.add(business);
        addressDao.add(address);
        Address address1 = new Address("gf", "iaoerjtoia", "areioj", "345w2");
        addressDao.add(address1);
        Address controlAddress = new Address("1", "2", "3", "4");
        addressDao.add(controlAddress);
        businessDao.addAddressToBusiness(business, address);
        businessDao.addAddressToBusiness(business, address1);
        assertEquals(3, addressDao.getAll().size());
        assertEquals(2, businessDao.getAllAddressesForABusiness(business.getId()).size());
        assertFalse(businessDao.getAllAddressesForABusiness(business.getId()).contains(controlAddress));

    }

    @Test
    public void getAllCausesForBusiness_returnsAllCausesAssociatedWithThatBusiness_true() throws Exception {
        Business testBusiness = setupBusiness();
        businessDao.add(testBusiness);
        Cause testCause1 = new Cause("a", "lkjsf", "lksdf", "lkasdf");
        Cause testCause2 = new Cause("b", "lsfdj", "oien", "nwoisd");
        Cause controlCause = new Cause("c", "sdoi", "qoic", "k,mi");
        causeDao.add(testCause1);
        causeDao.add(testCause2);
        causeDao.add(controlCause);
        businessDao.addCauseToBusiness(testBusiness, testCause1);
        businessDao.addCauseToBusiness(testBusiness, testCause2);
        assertEquals(2, businessDao.getAllCausesForABusiness(testBusiness.getId()).size());
        assertFalse(businessDao.getAllCausesForABusiness(testBusiness.getId()).contains(controlCause));
    }

    @Test
    public void updateUpdatesInfo() throws Exception {
        Business business = setupBusiness();
        Business business1 = setupBusiness1();
        businessDao.add(business);
        businessDao.add(business1);
        businessDao.update(1, "a", "b", "c", "d");
        businessDao.update(2, "z", "x", "v", "q");
        assertEquals("a", businessDao.findById(1).getName());
        assertEquals("z", businessDao.findById(2).getName());
        assertEquals("b", businessDao.findById(1).getType());
        assertEquals("x", businessDao.findById(2).getType());
    }


    @Test
    public void deleteByIdDeletesCorrectly() throws Exception {
        Business business = setupBusiness();
        Business business1 = setupBusiness1();
        businessDao.add(business);
        businessDao.add(business1);
        businessDao.deleteById(1);
        assertFalse(businessDao.getAll().contains(business));
        assertEquals(1, businessDao.getAll().size());
    }

    @Test
    public void deleteAllDeletesAll() throws Exception {
        Business business = setupBusiness();
        Business business1 = setupBusiness1();
        businessDao.add(business);
        businessDao.add(business1);
        assertEquals(2, businessDao.getAll().size());
        businessDao.deleteAll();
        assertEquals(0, businessDao.getAll().size());
    }

}