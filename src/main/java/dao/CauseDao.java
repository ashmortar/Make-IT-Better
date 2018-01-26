package dao;

import models.*;

import java.util.List;

public interface CauseDao {


    //create
    void add(Cause cause);
    void addAddressToCause(Cause cause, Address address);

    //read
    Cause findById(int charityId);
    List<Cause> getAll();
    List<Business> getAllBusinessesForCause(int charyityId);
//    List<Bakery>getAllBakeriesForACause(int charityId);
//    List<Bar>getAllBarsForACause(int charityId);
//    List<Restaurant>getAllRestaurantsForACause(int charityId);
    List<Address> getAllAddressesForCause(int charityId);


    //update
    void update(int id, String name, String type, String description, String phone);


    //destroy
    void deleteById(int id);
    void deleteAll();

}
