package dao;

import models.Address;

import java.util.List;

/**
 * Created by Guest on 1/24/18.
 */
public interface AddressDao {


    //create
    void add(Address address);

    //read
    Address findById(int id);
    List<Address> getAll();

    //update
    void update(int id, String street, String city, String state, String zip);

    //destroy
    void deleteById(int id);
    void deleteAll();
    void deleteUnused();

}
