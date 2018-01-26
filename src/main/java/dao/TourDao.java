package dao;

import models.Address;
import models.Tour;

import java.util.List;

/**
 * Created by Guest on 1/25/18.
 */
public interface TourDao {

    //create
     void add(Tour tour);


    //read
    List<Tour> getAllTours();
    Object getDirectionsForATour(int tourId);
     Tour findById(int tourId);

    //update
    void update(int id, Integer startPoint, Integer endPoint, List<String> waypoints);

    //destroy
    void deleteById(int tourId);
    void deleteAll();
}

