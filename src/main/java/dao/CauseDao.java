package dao;

import models.Address;
import models.Business;
import models.Cause;

import java.util.List;

/**
 * Created by Guest on 1/24/18.
 */
public interface CauseDao {


    //create
    void add(Cause cause);
    void addAddressToCause(Cause cause, Address address);

    //read
    Cause findById(int id);
    List<Cause> getAll();
    List<Business> getAllBusinessesForCause(int id);
    List<Address> getAllAddressesForCause(int id);


    //update
    void update(int id, String name, String type, String description, String phone);


    //destroy
    void deleteById(int id);
    void deleteAll();

}
