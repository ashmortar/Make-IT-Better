package dao;

import models.Address;
import models.Business;
import models.Cause;

import java.util.List;

/**
 * Created by Guest on 1/24/18.
 */
public interface BusinessDao {


    //create
    void add(Business business);
    void addAddressToBusiness(Business business, Address address);
    void addCauseToBusiness(Business business, Cause cause);

    //read
    Business findById(int id);

    List<Business> getAll();
    List<Cause> getAllCausesForABusiness(int id);
    List<Address> getAllAddressesForABusiness(int id);

    //update
    void update(int id, String name, String type, String phone, String website);

    //destroy
    void deleteById(int id);

    void deleteAll();

}
