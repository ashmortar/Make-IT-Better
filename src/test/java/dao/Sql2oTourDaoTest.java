package dao;

import models.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class Sql2oTourDaoTest {
    private Connection fred;
    private Sql2oCauseDao causeDao;
    private Sql2oBusinessDao businessDao;
    private Sql2oAddressDao addressDao;
    private Sql2oTourDao tourDao;

    public Address setupAddress() {
        return new Address("street", "city", "state", "zip");
    }

    public Address setupAddress1() {
        return new Address("fdasfda", "afsdsadtg", "gard", "adfg");
    }

    public Cause setupCause() {
        return new Cause("name", "type", "description", "phone");
    }

    public Cause setupCause1() {
        return new Cause("indigo", "charity", "we do stufF", "callme");
    }

    public Business setupBusiness() {
        return new Business("poop", "pee", "12345", "poop@poop.com");
    }

    public Business setupBusiness1() {
        return new Business("Evil Corp", "Evil", "666-666-6666", "evil@corporation.com");
    }

    public Tour setupTour() { return new Tour(1, 2);}
    public Tour setupTour1() { return new Tour (2, 3);}


    @Before
    public void setUp() throws Exception {
        String connectionString  = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/create.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");
        causeDao = new Sql2oCauseDao(sql2o);
        businessDao = new Sql2oBusinessDao(sql2o);
        addressDao = new Sql2oAddressDao(sql2o);
        tourDao = new Sql2oTourDao(sql2o);
        fred = sql2o.open();
    }

    @After
    public void tearDown() throws Exception {
        fred.close();
    }

    @Test
    public void addAdds() throws Exception {
        Tour tour = setupTour();
        int originalId = tour.getId();
        tourDao.add(tour);
        assertNotEquals(originalId, tour.getId());
    }

    @Test
    public void getAllGetsAll() throws Exception {
        Tour tour = setupTour();
        Tour tour1 = setupTour1();
        List<String> addressIds = new ArrayList<>();
        addressIds.add("2");
        addressIds.add("4");
        Tour tour2 = new Tour(1,3, addressIds);
        tourDao.add(tour);
        tourDao.add(tour1);
        tourDao.add(tour2);
        assertEquals(3, tourDao.getAllTours().size());
    }

    @Test
    public void findById_returnsCorrectInstance_true() throws Exception {
        Tour testTour = setupTour();
        Tour controlTour = setupTour1();
        tourDao.add(testTour);
        tourDao.add(controlTour);
        assertEquals(testTour, tourDao.findById(1));
        assertNotEquals(controlTour, tourDao.findById(1));
    }

    @Test
    public void update_does() throws Exception {
        Tour testTour = setupTour();
        Tour controlTour = setupTour1();
        tourDao.add(testTour);
        tourDao.add(controlTour);
        assertEquals(null, testTour.getWaypoints());
        List<String> addressIds = new ArrayList<>();
        addressIds.add("2");
        addressIds.add("4");
        tourDao.update(1, 4, 5, addressIds);
        int testId = testTour.getId();
        Integer expectedStart = 4;
        Integer expectedEnd = 5;
        assertEquals(expectedStart, tourDao.findById(testId).getStartPoint());
        assertEquals(expectedEnd, tourDao.findById(testId).getEndPoint());
        assertEquals(addressIds.toString(), tourDao.findById(testId).getWaypoints());
        assertNotEquals(addressIds.toString(), tourDao.findById(2).getWaypoints());


    }

    @Test
    public void deleteById_deletesCorrectly() throws Exception {
        Tour testTour = setupTour();
        Tour controlTour = setupTour1();
        tourDao.add(testTour);
        tourDao.add(controlTour);
        assertEquals(null, testTour.getWaypoints());
        List<String> addressIds = new ArrayList<>();
        addressIds.add("2");
        addressIds.add("4");
        Tour testTour1 = new Tour(1,3, addressIds);
        tourDao.add(testTour1);
        assertEquals(3, tourDao.getAllTours().size());
        tourDao.deleteById(1);
        assertEquals(2, tourDao.getAllTours().size());
        assertFalse(tourDao.getAllTours().contains(testTour));
    }

    @Test
    public void deleteAll_removesAllInstancesOfTour_true() throws Exception {
        Tour testTour = setupTour();
        Tour testTour1 = setupTour1();
        tourDao.add(testTour);
        tourDao.add(testTour1);
        assertEquals(2, tourDao.getAllTours().size());
        tourDao.deleteAll();
        assertEquals(0, tourDao.getAllTours().size());
    }


}