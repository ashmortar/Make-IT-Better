package models;

import java.util.ArrayList;
import java.util.List;

public class Tour {
    private int id;
    private Integer startPoint;
    private Integer endPoint;
    private String waypoints;
    private String directions;

    public Tour(Integer startPoint, Integer endPoint) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }

    public Tour(Integer startPoint, Integer endPoint, List<String> waypoints) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.waypoints = waypoints.toString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(Integer startPoint) {
        this.startPoint = startPoint;
    }

    public Integer getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(Integer endPoint) {
        this.endPoint = endPoint;
    }

    public String getWaypoints() {
        return waypoints;
    }

    public void setWaypoints(String waypoints) {
        this.waypoints = waypoints;
    }

    public String getDirections() {
        return directions;
    }

    public void setDirections(String directions) {
        this.directions = directions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tour tour = (Tour) o;

        if (id != tour.id) return false;
        if (!startPoint.equals(tour.startPoint)) return false;
        if (!endPoint.equals(tour.endPoint)) return false;
        if (waypoints != null ? !waypoints.equals(tour.waypoints) : tour.waypoints != null) return false;
        return directions != null ? directions.equals(tour.directions) : tour.directions == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + startPoint.hashCode();
        result = 31 * result + endPoint.hashCode();
        result = 31 * result + (waypoints != null ? waypoints.hashCode() : 0);
        result = 31 * result + (directions != null ? directions.hashCode() : 0);
        return result;
    }
}
