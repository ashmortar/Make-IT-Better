package dao;

import models.Address;

import java.util.List;


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
