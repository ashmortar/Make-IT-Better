package dao;

import models.*;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.ArrayList;
import java.util.List;

public class Sql2oCauseDao implements CauseDao{

    private final Sql2o sql2o;

    public Sql2oCauseDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public void add(Cause cause) {
        String sql = "INSERT INTO causes (name, type, description, phone) VALUES (:name, :type, :description, :phone)";
        try (Connection fred = sql2o.open()) {
            int id = (int) fred.createQuery(sql)
                    .bind(cause)
                    .executeUpdate()
                    .getKey();
            cause.setId(id);
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void addAddressToCause(Cause cause, Address address) {
        String sql = "INSERT INTO addresses_causes (addressid, causeid) VALUES (:addressId, :causeId)";
        try (Connection con = sql2o.open()){
            con.createQuery(sql)
                    .addParameter("addressId", address.getId())
                    .addParameter("causeId", cause.getId())
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public Cause findById(int id) {
        String sql = "SELECT * FROM causes WHERE id = :id";
        try (Connection fred = sql2o.open()){
            return fred.createQuery(sql)
                    .addParameter("id", id)
                    .executeAndFetchFirst(Cause.class);
        }
    }

    @Override
    public List<Cause> getAll() {
        String sql = "SELECT * FROM causes";
        try (Connection fred = sql2o.open()){
            return fred.createQuery(sql)
                    .executeAndFetch(Cause.class);
        }
    }

    @Override
    public List<Business> getAllBusinessesForCause(int causeId) {
        String typeSQL = "SELECT type FROM businesses";
        String type = "";
        String sql = "SELECT businessid FROM causes_businesses WHERE causeid = :causeId";
        List<Business> businesses = new ArrayList<>();
        try (Connection fred = sql2o.open()) {
            List<Integer> allBusinessIds = fred.createQuery(sql)
                    .addParameter("causeId", causeId)
                    .executeAndFetch(Integer.class);
            for (Integer businessId : allBusinessIds) {
                try (Connection con = sql2o.open()) {
                    type = con.createQuery(typeSQL)
                            .executeAndFetchFirst(String.class);
                    switch (type) {
                        case "bakery":
                            String bakerySQL = "SELECT name, phone, website, hours, specialty, glutenFree FROM businesses WHERE id = :id";
                            businesses.add(con.createQuery(bakerySQL)
                                    .addParameter("id", businessId)
                                    .executeAndFetchFirst(Bakery.class));
                            break;
                        case "bar":
                            String barSQL = "SELECT name, phone, website, hours, food, atmosphere, hasTaps, hasCocktails FROM businesses WHERE id = :id";
                            businesses.add(con.createQuery(barSQL)
                                    .addParameter("id", businessId)
                                    .executeAndFetchFirst(Bar.class));
                            break;
                        case "cafe":
                            String cafeSQL = "SELECT name, phone, website, hours, food, fairTrade FROM businesses WHERE id = :id";
                            businesses.add(con.createQuery(cafeSQL)
                                    .addParameter("id", businessId)
                                    .executeAndFetchFirst(Cafe.class));
                            break;
                        case "restaurant":
                            String restaurantSQL = "SELECT name, phone, website, hours, food, needReservation, atmosphere, hasBar FROM businesses WHERE id = :id";
                            businesses.add(con.createQuery(restaurantSQL)
                                    .addParameter("id", businessId)
                                    .executeAndFetchFirst(Restaurant.class));
                            break;
                        default:
                            System.out.println("not a businessType");
                            break;
                    }
                }
            }
            return businesses;
        }
    }

    @Override
    public List<Address> getAllAddressesForCause(int causeId) {
        String sql = "SELECT addressid FROM addresses_causes WHERE causeid = :causeId";
        List<Address> addresses = new ArrayList<>();
        try (Connection fuck = sql2o.open()){
            List<Integer> allAddressIds = fuck.createQuery(sql)
                    .addParameter("causeId", causeId)
                    .executeAndFetch(Integer.class);
                for (Integer addressId : allAddressIds) {
                    String shit = "SELECT * FROM addresses WHERE id = :addressId";
                    addresses.add(
                            fuck.createQuery(shit)
                                .addParameter("addressId", addressId)
                                .executeAndFetchFirst(Address.class)
                    );

                }
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
        return addresses;
    }

    @Override
    public void update(int id, String name, String type, String description, String phone) {
        String sql = "UPDATE causes SET name = :name, type = :type, description = :description, phone = :phone WHERE id = :id";
        try (Connection fred = sql2o.open()){
            fred.createQuery(sql)
                    .addParameter("name", name)
                    .addParameter("type", type)
                    .addParameter("description", description)
                    .addParameter("phone", phone)
                    .addParameter("id", id)
                    .executeUpdate();
        } catch (Sql2oException ex ) {
            System.out.println(ex);
        }
    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE FROM causes WHERE id = :id";
        try (Connection fred = sql2o.open()){
            fred.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate();
            fred.createQuery("DELETE FROM causes_businesses WHERE causeid = :id")
                    .addParameter("id", id)
                    .executeUpdate();
            fred.createQuery("DELETE FROM addresses_causes WHERE causeid = :id")
                    .addParameter("id", id)
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void deleteAll() {
        String sql = "DELETE FROM causes";
        try (Connection sally = sql2o.open()){
            sally.createQuery(sql)
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }
}
