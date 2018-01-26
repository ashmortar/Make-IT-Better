package dao;

import models.*;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.ArrayList;
import java.util.List;

public class Sql2oBusinessDao implements BakeryDao, BarDao, CafeDao, RestaurantDao, BusinessDao {
    private final Sql2o sql2o;

    public Sql2oBusinessDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public void add(Business business) {
        String type = business.getType();
        switch (type){
            case "bakery":
                String bakerySQL = "INSERT INTO businesses (type, name, phone, website, hours, specialty, glutenFree) VALUES (:type, :name, :phone, :website, :hours, :specialty, :glutenFree)";
                try (Connection con = sql2o.open()) {
                    int id = (int) con.createQuery(bakerySQL)
                            .bind(business)
                            .executeUpdate()
                            .getKey();
                    business.setId(id);
                } catch (Sql2oException ex) {
                    System.out.println(ex);
                }
                break;
            case "bar":
                String barSQL = "INSERT INTO businesses (type, name, phone, website, hours, food, atmosphere, hasTaps, hasCocktails) VALUES (:type, :name, :phone, :website, :hours, :food, :atmosphere, :hasTaps, :hasCocktails)";
                try (Connection con = sql2o.open()) {
                    int id = (int) con.createQuery(barSQL)
                            .bind(business)
                            .executeUpdate()
                            .getKey();
                    business.setId(id);
                } catch (Sql2oException ex) {
                    System.out.println(ex);
                }
                break;
            case "cafe":
                String cafeSQL = "INSERT INTO businesses (type, name, phone, website, hours, food, fairTrade) VALUES (:type, :name, :phone, :website, :hours, :food, :fairTrade)";
                try (Connection con = sql2o.open()) {
                    int id = (int) con.createQuery(cafeSQL)
                            .bind(business)
                            .executeUpdate()
                            .getKey();
                    business.setId(id);
                } catch (Sql2oException ex) {
                    System.out.println(ex);
                }
                break;
            case "restaurant":
                String restaurantSQL = "INSERT INTO businesses (type, name, phone, website, hours, food, needReservation, hasBar) VALUES (:type, :name, :phone, :website, :hours, :food, :needReservation, :hasBar)";
                try (Connection con = sql2o.open()) {
                    int id = (int) con.createQuery(restaurantSQL)
                            .bind(business)
                            .executeUpdate()
                            .getKey();
                    business.setId(id);
                } catch (Sql2oException ex) {
                    System.out.println(ex);
                }
                break;
            default:
                System.out.println("not a businessType");
        }

    }

    @Override
    public void addAddressToBusiness(Business business, Address address) {
        String sql = "INSERT INTO addresses_businesses (addressid, businessid) VALUES (:addressId, :businessId)";
        try (Connection fred = sql2o.open()) {
            fred.createQuery(sql)
                    .addParameter("addressId", address.getId())
                    .addParameter("businessId", business.getId())
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void addCauseToBusiness(Business business, Cause cause) {
        String sql = "INSERT INTO causes_businesses (causeid, businessid) VALUES (:causeId, :businessId)";
        try (Connection poop = sql2o.open()) {
            poop.createQuery(sql)
                    .addParameter("causeId", cause.getId())
                    .addParameter("businessId", business.getId())
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public Business findById(int id) {
        String sql = "SELECT * FROM businesses WHERE id = :id";
        try (Connection fred = sql2o.open()) {
            return fred.createQuery(sql)
                    .addParameter("id", id)
                    .executeAndFetchFirst(Business.class);
        }
    }

    @Override
    public List<Business> getAll() {
        String sql = "SELECT * FROM businesses";
        try (Connection fred = sql2o.open()) {
            return fred.createQuery(sql)
                    .executeAndFetch(Business.class);
        }
    }

    @Override
    public List<Address> getAllAddressesForABusiness(int businessId) {
        List<Address> addresses = new ArrayList<>();
        String sql = "SELECT addressid FROM addresses_businesses WHERE businessid = :businessId";

        try (Connection fred = sql2o.open()) {
            List<Integer> allAddressIds = fred.createQuery(sql)
                    .addParameter("businessId", businessId)
                    .executeAndFetch(Integer.class);
                for (Integer addressId : allAddressIds) {
                    String query2 = "SELECT * FROM addresses WHERE id = :addressId";
                    addresses.add(
                            fred.createQuery(query2)
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
    public List<Cause> getAllCausesForABusiness(int businessId) {
        List<Cause> causes = new ArrayList<>();
        String sql = "SELECT causeid FROM causes_businesses WHERE businessid = :businessId";

        try (Connection poop = sql2o.open()) {
            List<Integer> allCauseIds = poop.createQuery(sql)
                    .addParameter("businessId", businessId)
                    .executeAndFetch(Integer.class);
            for (Integer causeId : allCauseIds) {
                String query2 = "SELECT * FROM causes WHERE id = :causeId";
                causes.add(
                        poop.createQuery(query2)
                        .addParameter("causeId", causeId)
                        .executeAndFetchFirst(Cause.class)
                );
            }
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
        return causes;
    }

    @Override
    public void update(int id, String name, String phone, String website) {
        String sql = "UPDATE businesses SET name = :name, type = :type, phone = :phone, website = :website WHERE id = :id";
        try (Connection fred = sql2o.open()) {
            fred.createQuery(sql)
                    .addParameter("name", name)
                    .addParameter("phone", phone)
                    .addParameter("website", website)
                    .addParameter("id", id)
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE FROM businesses WHERE id = :id";
        try (Connection fred = sql2o.open()) {
            fred.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate();
            fred.createQuery("DELETE FROM addresses_businesses WHERE businessid = :id")
                    .addParameter("id", id)
                    .executeUpdate();
            fred.createQuery("DELETE FROM causes_businesses WHERE businessid = :id")
                    .addParameter("id", id)
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void deleteAll() {
        String sql = "DELETE FROM businesses";
        try (Connection fred = sql2o.open()) {
            fred.createQuery(sql)
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

}
