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
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
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
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("causeId", cause.getId())
                    .addParameter("businessId", business.getId())
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public Business findById(int id) {
        String sql = "SELECT type FROM businesses WHERE id = :id";
        String type = "";
        Business bakery = null;
        Business bar = null;
        Business cafe = null;
        Business restaurant = null;
        try (Connection con = sql2o.open()) {
            type = con.createQuery(sql)
                    .addParameter("id", id)
                    .executeAndFetchFirst(String.class);
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
        switch (type){
            case "bakery":
                String bakerySQL = "SELECT name, phone, website, hours, specialty, glutenFree FROM businesses WHERE id = :id";
                try (Connection con = sql2o.open()) {
                    bakery = con.createQuery(bakerySQL)
                            .addParameter("id", id)
                            .executeAndFetchFirst(Bakery.class);
                }
                break;
            case "bar":
                String barSQL = "SELECT name, phone, website, hours, food, atmosphere, hasTaps, hasCocktails FROM businesses WHERE id = :id";
                try (Connection con = sql2o.open()) {
                    bar = con.createQuery(barSQL)
                            .addParameter("id", id)
                            .executeAndFetchFirst(Bar.class);
                }
                break;
            case "cafe":
                String cafeSQL = "SELECT name, phone, website, hours, food, fairTrade FROM businesses WHERE id = :id";
                try (Connection con = sql2o.open()) {
                    cafe =  con.createQuery(cafeSQL)
                            .addParameter("id", id)
                            .executeAndFetchFirst(Cafe.class);
                }
                break;
            case "restaurant":
                String restaurantSQL = "SELECT name, phone, website, hours, food, needReservation, atmosphere, hasBar FROM businesses WHERE id = :id";
                try (Connection con = sql2o.open()) {
                    restaurant = con.createQuery(restaurantSQL)
                            .addParameter("id", id)
                            .executeAndFetchFirst(Restaurant.class);
                }
                break;
            default:
                System.out.println("not a businessType");
                break;
        }
        if (bakery != null){
            return bakery;
        } else if (bar != null) {
            return bar;
        } else if (cafe != null) {
            return cafe;
        } else if (restaurant != null) {
            return restaurant;
        } else {
            return null;
        }
    }

    @Override
    public List<Business> getAll() {
        String sql = "SELECT type FROM businesses";
        String type = "";
        List<Integer> ids = new ArrayList<>();
        List<Business> businesses = new ArrayList<>();
        try (Connection con = sql2o.open()) {
            ids = con.createQuery("SELECT id FROM businesses")
                    .executeAndFetch(Integer.class);
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
        for (Integer id : ids) {
            try (Connection con = sql2o.open()) {
                     type = con.createQuery(sql)
                        .executeAndFetchFirst(String .class);
                     switch (type) {
                         case "bakery":
                             String bakerySQL = "SELECT name, phone, website, hours, specialty, glutenFree FROM businesses WHERE id = :id";
                             businesses.add(con.createQuery(bakerySQL)
                                     .addParameter("id", id)
                                     .executeAndFetchFirst(Bakery.class));
                             break;
                         case "bar":
                             String barSQL = "SELECT name, phone, website, hours, food, atmosphere, hasTaps, hasCocktails FROM businesses WHERE id = :id";
                             businesses.add(con.createQuery(barSQL)
                                     .addParameter("id", id)
                                     .executeAndFetchFirst(Bar.class));
                             break;
                         case "cafe":
                             String cafeSQL = "SELECT name, phone, website, hours, food, fairTrade FROM businesses WHERE id = :id";
                             businesses.add(con.createQuery(cafeSQL)
                                     .addParameter("id", id)
                                     .executeAndFetchFirst(Cafe.class));
                             break;
                         case "restaurant":
                             String restaurantSQL = "SELECT name, phone, website, hours, food, needReservation, atmosphere, hasBar FROM businesses WHERE id = :id";
                             businesses.add(con.createQuery(restaurantSQL)
                                     .addParameter("id", id)
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

    @Override
    public List<Bakery> getAllBakeries() {
        String sql = "SELECT name, phone, website, hours, specialty, glutenFree FROM businesses WHERE type = :type";
        try (Connection con = sql2o.open()){
            return con.createQuery(sql)
                    .addParameter("type", "bakery")
                    .executeAndFetch(Bakery.class);
        }
    }

    @Override
    public List<Bar> getAllBars() {
        String sql = "SELECT name, phone, website, hours, food, atmosphere, hasTaps, hasCocktails FROM businesses WHERE type = :type";
        try (Connection con = sql2o.open()){
            return con.createQuery(sql)
                    .addParameter("type", "bar")
                    .executeAndFetch(Bar.class);
        }
    }

    @Override
    public List<Cafe> getAllCafes() {
        String sql = "SELECT name, phone, website, hours, food, fairTrade  FROM businesses WHERE type = :type";
        try (Connection con = sql2o.open()){
            return con.createQuery(sql)
                    .addParameter("type", "cafe")
                    .executeAndFetch(Cafe.class);
        }
    }

    @Override
    public List<Restaurant> getAllRestaurants() {
        String sql = "SELECT name, phone, website, hours, food, needReservation, atmosphere, hasBar  FROM businesses WHERE type = :type";
        try (Connection con = sql2o.open()){
            return con.createQuery(sql)
                    .addParameter("type", "restaurant")
                    .executeAndFetch(Restaurant.class);
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
    public void update(int id, Business business) {
        String type = business.getType();
        switch (type) {
            case "bakery":
                String bakerySQL = "UPDATE businesses SET name = :name, phone = :phone, website = :website, hours = :hours, specialty = :specialty, glutenFree = :glutenFree WHERE id = :id";
                try (Connection con = sql2o.open()){
                    con.createQuery(bakerySQL)
                            .bind(business)
                            .addParameter("id", id)
                            .executeUpdate();
                    break;
                } catch (Sql2oException ex) {
                    System.out.println(ex);
                    break;
                }
            case "bar":
                String barSQL = "UPDATE businesses SET name = :name, phone = :phone, website = :website, hours = :hours, food = :food, atmosphere = :atmosphere, hasTaps = :hasTaps, hasCocktails = :hasCocktails WHERE id = :id";
                try (Connection con = sql2o.open()){
                    con.createQuery(barSQL)
                            .bind(business)
                            .addParameter("id", id)
                            .executeUpdate();
                    break;
                } catch (Sql2oException ex){
                    System.out.println(ex);
                    break;
                }
            case "cafe":
                String cafeSQL = "UPDATE businesses SET name = :name, phone = :phone, website = :website, hours = :hours, food = :food, fairTrade = :fairTrade WHERE id = :id";
                try (Connection con = sql2o.open()){
                    con.createQuery(cafeSQL)
                            .bind(business)
                            .addParameter("id", id)
                            .executeUpdate();
                    break;
                }catch (Sql2oException ex) {
                    System.out.println(ex);
                    break;
                }
            case "restaurant":
                String restaurantSQL = "UPDATE businesses SET name = :name, phone = :phone, website = :website, hours = :hours, food = :food, needReservation = :needReservation, atmosphere = :atmosphere, hasBar = :hasBar WHERE id = :id";
                try (Connection con = sql2o.open()){
                    con.createQuery(restaurantSQL)
                            .bind(business)
                            .addParameter("id", id)
                            .executeUpdate();
                    break;
                } catch (Sql2oException ex) {
                    System.out.println(ex);
                    break;
                }
                default:
                    System.out.println("Not a business type");
                    break;
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
    public void deleteAllBakeries() {
        String sql = "DELETE FROM businesses WHERE type = :type";
        try (Connection con = sql2o.open()){
            con.createQuery(sql)
                    .addParameter("type", "bakery")
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }
    @Override
    public void deleteAllBars() {
        String sql = "DELETE FROM businesses WHERE type = :type";
        try (Connection con = sql2o.open()){
            con.createQuery(sql)
                    .addParameter("type", "bar")
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void deleteAllCafes() {
        String sql = "DELETE FROM businesses WHERE type = :type";
        try (Connection con = sql2o.open()){
            con.createQuery(sql)
                    .addParameter("type", "cafe")
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void deleteAllRestaurants() {
        String sql = "DELETE FROM businesses WHERE type = :type";
        try (Connection con = sql2o.open()){
            con.createQuery(sql)
                    .addParameter("type", "restaurant")
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
