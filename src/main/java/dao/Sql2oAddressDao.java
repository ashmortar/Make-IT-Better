package dao;

import models.Address;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guest on 1/24/18.
 */
public class Sql2oAddressDao implements AddressDao {

    private final Sql2o sql2o;

    public Sql2oAddressDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public void add(Address address) {
        String sql = "INSERT INTO addresses (street, city, state, zip) VALUES (:street, :city, :state, :zip)";
        try (Connection con = sql2o.open()) {
            int id = (int) con.createQuery(sql)
                    .bind(address)
                    .executeUpdate()
                    .getKey();
                address.setId(id);
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public Address findById(int id) {
        String sql = "SELECT * FROM addresses WHERE id = :id";
        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .addParameter("id", id)
                    .executeAndFetchFirst(Address.class);
        }
    }

    @Override
    public List<Address> getAll() {
        String sql = "SELECT * FROM addresses";
        try (Connection con = sql2o.open()){
            return con.createQuery(sql)
                    .executeAndFetch(Address.class);
        }
    }

    @Override
    public void update(int id, String street, String city, String state, String zip) {
        String sql = "UPDATE addresses SET street = :street, city = :city, state = :state, zip = :zip WHERE id = :id";
        try (Connection con = sql2o.open()){
            con.createQuery(sql)
                    .addParameter("id", id)
                    .addParameter("street", street)
                    .addParameter("city", city)
                    .addParameter("state", state)
                    .addParameter("zip", zip)
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE FROM addresses WHERE id = :id";
        try (Connection con = sql2o.open()){
            con.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate();
        }catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }
    @Override
    public void deleteAll() {
        String sql = "DELETE FROM addresses";
        try (Connection con = sql2o.open()){
            con.createQuery(sql)
                    .executeUpdate();
        }catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }
    @Override
    public void deleteUnused() {
        try (Connection fuck = sql2o.open()) {
            List<Integer> firstCull = new ArrayList<>();
            List<Integer> secondCull = new ArrayList<>();
            List<Integer> allAddressIds = fuck.createQuery("SELECT id FROM addresses")
                    .executeAndFetch(Integer.class);
            for (Integer addressId : allAddressIds) {
                String businessQuery = "SELECT id FROM addresses_businesses WHERE addressId = :addressId";

                Integer someInt  = fuck.createQuery(businessQuery)
                        .addParameter("addressId", addressId)
                        .executeAndFetchFirst(Integer.class);

                if (someInt == null) {
                    firstCull.add(addressId);
                }
            }
            for (Integer id : firstCull) {
                String causeQuery = "SELECT id FROM addresses_causes WHERE addressId = :id";
                Integer anotherInt = fuck.createQuery(causeQuery)
                        .addParameter("id", id)
                        .executeAndFetchFirst(Integer.class);
                if (anotherInt == null){
                    secondCull.add(id);
                }
            }
            for (Integer toDelete : secondCull) {
                String deleteString = "DELETE FROM addresses WHERE id = :toDelete";
                fuck.createQuery(deleteString)
                        .addParameter("toDelete", toDelete)
                        .executeUpdate();
            }
        }
    }
}
//AIzaSyDku9juGsfsA3ZqSf4vvMc4qy33P6rRDpk