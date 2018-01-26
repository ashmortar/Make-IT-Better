package dao;

import com.google.gson.Gson;

import models.*;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.io.InputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLDataException;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Created by Guest on 1/25/18.
 */
public class Sql2oTourDao implements TourDao {

    private final Sql2o sql2o;

    public Sql2oTourDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public void add(Tour tour) {
        String sql = "INSERT INTO tours (startPoint, endPoint, waypoints) VALUES (:startPoint, :endPoint, :waypoints)";
        try (Connection con = sql2o.open()) {
            int id = (int) con.createQuery(sql)
                    .bind(tour)
                    .executeUpdate()
                    .getKey();
            tour.setId(id);
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public List<Tour> getAllTours() {
        try (Connection sonofabitch = sql2o.open()) {
            return sonofabitch.createQuery("SELECT * FROM tours")
                    .executeAndFetch(Tour.class);
        }
    }

    @Override
    public Tour findById(int id) {
        String sql = "SELECT * FROM tours WHERE id = :id";
        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .addParameter("id", id)
                    .executeAndFetchFirst(Tour.class);
        }
    }

    @Override
    public void update(int id, Integer startPoint, Integer endPoint, List<String> waypoints) {
        Tour updateTour = new Tour(startPoint, endPoint, waypoints);
        String sql = "UPDATE tours SET startPoint = :startPoint, endPoint = :endPoint, waypoints = :waypoints WHERE id = :id";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("id", id)
                    .bind(updateTour)
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE FROM tours WHERE id = :id";
        try (Connection poop = sql2o.open()) {
            poop.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void deleteAll() {
        try (Connection con = sql2o.open()) {
            con.createQuery("DELETE FROM tours")
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public Object getDirectionsForATour(int tourId) {
        try (Connection connection = sql2o.open()) {
            Object directions = new Object();
            ArrayList<Address> waypointAddresses = new ArrayList<>();
            Tour currentTour = connection.createQuery("SELECT * FROM tours WHERE id = :tourId")
                    .addParameter("tourId", tourId)
                    .executeAndFetchFirst(Tour.class);
            Address startPoint = connection.createQuery("SELECT * FROM addresses WHERE id = :id")
                    .addParameter("id", currentTour.getStartPoint())
                    .executeAndFetchFirst(Address.class);
            Address endPoint = connection.createQuery("SELECT * FROM addresses WHERE id = :id")
                    .addParameter("id", currentTour.getEndPoint())
                    .executeAndFetchFirst(Address.class);
            if (currentTour.getWaypoints() != null) {
                List<String> waypoints = Arrays.asList(currentTour.getWaypoints().split(", "));

                for (String waypoint : waypoints) {
                    Integer addressId = Integer.parseInt(waypoint);
                    waypointAddresses.add(
                        connection.createQuery("SELECT * FROM addresses WHERE id = :addressId")
                                .addParameter("addressId", addressId)
                                .executeAndFetchFirst(Address.class)
                    );
                }
            }
            //format addresses into get url -- https://maps.googleapis.com/maps/api/directions/json?
            String root = "https://maps.googleapis.com/maps/api/directions/json?";
            // origin=400+SW+6th+Ave+Portland,+OR
             String origin = String.format("origin=%s+%s,+%s", startPoint.getStreet(), startPoint.getCity(), startPoint.getState());
            // &destination=1536+N+Schofield+St.+Portland,+OR
            String destination = String.format("&destination=%s+%s,+%s", endPoint.getStreet(), endPoint.getCity(), endPoint.getState());
            // &waypoints=831+SW+VISTA+Ave+Portland,+OR
            String waypoints = "";
            if (waypointAddresses.size() > 0) {
                waypoints += "&waypoints=optimize:true";
                for (Address address : waypointAddresses) {
                    waypoints += String.format("|%s+%s,+%s", address.getStreet(), address.getCity(), address.getState());
                }
            }

            // &key=AIzaSyDku9juGsfsA3ZqSf4vvMc4qy33P6rRDpk
             String key = "&key=AIzaSyDku9juGsfsA3ZqSf4vvMc4qy33P6rRDpk";

            //assembled url
            String apiRequest = (root + origin + destination + waypoints + key).replaceAll(" ", "+");

            //preparing http query
            String url = apiRequest;
            String charset = "UTF-8";

            try {
                URLConnection connection1 = new URL(url).openConnection();
                connection1.setRequestProperty("Accept-Charset", charset);
                InputStream response = connection1.getInputStream();
                try (Scanner scanner = new Scanner(response)){
                    directions = scanner.useDelimiter("//A").next();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return directions;
        }
    }
}