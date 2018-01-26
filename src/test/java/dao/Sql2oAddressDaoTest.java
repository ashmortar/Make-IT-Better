package dao;

import models.Address;
import models.Business;
import models.Cause;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;


import static org.junit.Assert.*;

/**
 * Created by Guest on 1/24/18.
 */
public class Sql2oAddressDaoTest {

    private Connection fred;
    private Sql2oAddressDao addressDao;
    private Sql2oBusinessDao businessDao;
    private Sql2oCauseDao causeDao;

    public Address setupAddress() {
        return new Address("street", "city", "state", "zip");
    }

    public Address setupAddress1() {
        return new Address("fdasfda", "afsdsadtg", "gard", "adfg");
    }

    @Before
    public void setUp() throws Exception {
        String connectionString  = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/create.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");
        addressDao = new Sql2oAddressDao(sql2o);
        businessDao = new Sql2oBusinessDao(sql2o);
        causeDao = new Sql2oCauseDao(sql2o);
        fred = sql2o.open();
    }

    @After
    public void tearDown() throws Exception {
        fred.close();
    }

    @Test
    public void add_SetsUniqueId_true() throws Exception {
        Address testAddress = setupAddress();
        int originalId = testAddress.getId();
        addressDao.add(testAddress);
        assertNotEquals(originalId, testAddress.getId());
    }

    @Test
    public void findById_findsCorrectAddress_true() throws Exception {
        Address testAddress = setupAddress();
        Address testAddress1 = setupAddress1();
        addressDao.add(testAddress);
        addressDao.add(testAddress1);
        int addressId = testAddress.getId();
        int addressId1 = testAddress1.getId();
        assertEquals(testAddress, addressDao.findById(addressId));
        assertEquals(testAddress1, addressDao.findById(addressId1));
    }

    @Test
    public void getAll_returnsAllAddressObjectsInDB_true() throws Exception {
        Address testAddress = setupAddress();
        Address testAddress1 = setupAddress1();
        Address controlAddress = setupAddress();
        controlAddress.setStreet("different");
        addressDao.add(testAddress);
        addressDao.add(testAddress1);
        assertEquals(2, addressDao.getAll().size());
        assertFalse(addressDao.getAll().contains(controlAddress));
    }

    @Test
    public void update_changesAllValues_true() throws Exception {
        Address testAddress = setupAddress();
        Address controlAddress = setupAddress();
        addressDao.add(testAddress);
        addressDao.add(controlAddress);
        addressDao.update(testAddress.getId(), "LOOK", "HOW", "I", "PASS");

        assertEquals("street", addressDao.findById(controlAddress.getId()).getStreet());
        assertEquals("city", addressDao.findById(controlAddress.getId()).getCity());
        assertEquals("state", addressDao.findById(controlAddress.getId()).getState());
        assertEquals("zip", addressDao.findById(controlAddress.getId()).getZip());

        assertEquals("LOOK", addressDao.findById(testAddress.getId()).getStreet());
        assertEquals("HOW", addressDao.findById(testAddress.getId()).getCity());
        assertEquals("I", addressDao.findById(testAddress.getId()).getState());
        assertEquals("PASS", addressDao.findById(testAddress.getId()).getZip());
    }

    @Test
    public void deleteById_() throws Exception {
        Address testAddress = setupAddress();
        Address controlAddress = setupAddress1();
        addressDao.add(testAddress);
        addressDao.add(controlAddress);
        addressDao.deleteById(testAddress.getId());
        assertEquals(1, addressDao.getAll().size());
        assertFalse(addressDao.getAll().contains(testAddress));
    }

    @Test
    public void deleteAll_removesAllInstancesOfAddressFromDb_true() throws Exception {
        Address testAddress = setupAddress();
        Address secondTest = setupAddress1();
        addressDao.add(testAddress);
        addressDao.add(secondTest);
        assertEquals(2, addressDao.getAll().size());
        addressDao.deleteAll();
        assertEquals(0, addressDao.getAll().size());
    }

    @Test
    public void deleteUnused_removesAddressNotAssociatedWithCauseOrBusiness() throws Exception {
        Address testAddress = setupAddress();
        Address secondTestAddress = setupAddress1();
        Address controlAddress = setupAddress();
        controlAddress.setStreet("control");
        addressDao.add(controlAddress);
        addressDao.add(testAddress);
        addressDao.add(secondTestAddress);

        Business testBusiness = new Business("name", "lsdf", "lksdjf", "sodaifj");
        businessDao.add(testBusiness);

        Cause testCause = new Cause("slfdjk", "lsjkf", "lsakdf ", "alsdkfj");
        causeDao.add(testCause);

        businessDao.addAddressToBusiness(testBusiness, testAddress);

        causeDao.addAddressToCause(testCause, secondTestAddress);

        assertEquals(3, addressDao.getAll().size());

        addressDao.deleteUnused();

        assertEquals(2, addressDao.getAll().size());
        assertFalse(addressDao.getAll().contains(controlAddress));
    }
}