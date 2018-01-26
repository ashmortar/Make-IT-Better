package dao;

import models.Address;
import models.Business;
import models.Cause;

import java.util.List;


public interface BusinessDao {


    //create - cannot instantiate abstract class but can use abstract class to associate causes and addresses via shared table

    void add(Business business);
    void addAddressToBusiness(Business business, Address address);
    void addCauseToBusiness(Business business, Cause cause);

    //read
    Business findById(int id);
    List<Business> getAll();
    List<Cause> getAllCausesForABusiness(int id);
    List<Address> getAllAddressesForABusiness(int id);

    //update
    void update(int id, String name, String phone, String website);
    //destroy
    void deleteById(int id);
    void deleteAll();

}
