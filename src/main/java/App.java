import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.javaws.ui.ApplicationIconGenerator;
import dao.Sql2oAddressDao;
import dao.Sql2oBusinessDao;
import dao.Sql2oCauseDao;
import dao.Sql2oTourDao;
import exceptions.ApiException;
import models.*;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class App {
    public static void main(String[] args) {
        Sql2oBusinessDao businessDao;
        Sql2oCauseDao causeDao;
        Sql2oAddressDao addressDao;
        Sql2oTourDao tourDao;
        Connection con;
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();

        String connectionString = "jdbc:h2:~/good-business-tracker.db;INIT=RUNSCRIPT from 'classpath:db/create.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");

        tourDao = new Sql2oTourDao(sql2o);
        businessDao = new Sql2oBusinessDao(sql2o);
        causeDao = new Sql2oCauseDao(sql2o);
        addressDao = new Sql2oAddressDao(sql2o);
        con = sql2o.open();

        //exception messages
        String badData = "Your post was invalid, please verify your data structure";
        // String badBusinessId = String.format("There is no business with the id < %s >", request.params("businessId"));
        // String badCauseId = String.format("There is no cause with the id < %s >", request.params("causeId")));
        // String badAddressId = String.format("There is no address with the id < %s >", request.params("addressId")));
        // String badTourId = String.format("There is no tour with the id < %s >", request.params("tourId")));

        //==================create=============================

        //post new business
        post("/businesses/new", "application/json", (request, response) -> {
            Business business = gson.fromJson(request.body(), Business.class);
            if (business == null) {
                throw new ApiException(500, badData);
            }
            businessDao.add(business);
            response.status(201);
            return gson.toJson(business);
        });

        //post new cause
        post("/causes/new", "application/json", (request, response) -> {
            Cause cause = gson.fromJson(request.body(), Cause.class);
            if (cause == null) {
                throw new ApiException(404, badData);
            }
            causeDao.add(cause);
            response.status(201);
            return gson.toJson(cause);
        });

        //address associated to business
        post("/businesses/:businessId/addresses/new", "application/json", (request, response) -> {
            int businessId = Integer.parseInt(request.params("businessId"));
            Business business = businessDao.findById(businessId);
            if (business == null) {
                throw new ApiException(404, String.format("There is no business with the id < %s >", request.params("businessId")));
            }
            Address address = gson.fromJson(request.body(), Address.class);
            if (address == null) {
                throw new ApiException(404, badData);
            }
            addressDao.add(address);
            businessDao.addAddressToBusiness(businessDao.findById(businessId), addressDao.findById(address.getId()));
            response.status(201);
            return gson.toJson(address);
        });

        //post new tour
        post("/tours/new", "application/json", (request, response) -> {
            Tour tour = gson.fromJson(request.body(), Tour.class);
            if (tour == null) {
                throw new ApiException(404, badData);
            }
            tourDao.add(tour);
            response.status(201);
            return gson.toJson(tour);
        });

        //address associated to cause
        post("/causes/:causeId/addresses/new", "application/json", (request, response) -> {
            int causeId = Integer.parseInt(request.params("causeId"));
            Cause cause = causeDao.findById(causeId);
            if (cause == null) {
                throw new ApiException(404, String.format("There is no cause with the id < %s >", request.params("causeId")));
            }
            Address address = gson.fromJson(request.body(), Address.class);
            if (address == null) {
                throw new ApiException(404, badData);
            }
            addressDao.add(address);
            causeDao.addAddressToCause(causeDao.findById(causeId), addressDao.findById(address.getId()));
            response.status(201);
            return gson.toJson(address);
        });

        //cause associated to business
        post("/businesses/:businessId/causes/:causeId", "application/json", (request, response) -> {
            int businessId = Integer.parseInt(request.params("businessId"));
            Business business = businessDao.findById(businessId);
            if (business == null) {
                throw new ApiException(404, String.format("There is no business with the id < %s >", request.params("businessId")));
            }
           int causeId = Integer.parseInt(request.params("causeId"));
            Cause cause = causeDao.findById(causeId);
            if (cause == null) {
                throw new ApiException(404, String.format("There is no cause with the id < %s >", request.params("causeId")));
            }
           businessDao.addCauseToBusiness(businessDao.findById(businessId), causeDao.findById(causeId));
           response.status(201);
           return gson.toJson(String.format("Cause '%s' has been associated with Bussiness '%s'", causeDao.findById(causeId).getName(), businessDao.findById(businessId).getName()));
        });
        //=====================read===========================

        //get all businesses
        get("/businesses", "application/json", (request, response) -> {
            response.status(201);
            return gson.toJson(businessDao.getAll());
        });
        //get a business
        get("/businesses/:businessId", "application/json", (request, response) -> {
            int businessId = Integer.parseInt(request.params("businessId"));
            Business businessToFind = businessDao.findById(businessId);
            if (businessToFind == null) {
                throw new ApiException(404, String.format("There is no business with the id < %s >", request.params("businessId")));
            }
            response.status(201);
            return gson.toJson(businessToFind);
        });
        //get all causes for a business
        get("/businesses/:businessId/causes", "application/json", (request, response) -> {
            int businessId = Integer.parseInt(request.params("businessId"));
            Business business = businessDao.findById(businessId);
            if (business == null) {
                throw new ApiException(404, String.format("There is no business with the id < %s >", request.params("businessId")));
            }
            response.status(201);
            return gson.toJson(businessDao.getAllCausesForABusiness(businessId));
        });
        //get all addresses for a business
        get("/businesses/:businessId/addresses", "application/json", (request, response) -> {
            int businessId = Integer.parseInt(request.params("businessId"));
            Business business = businessDao.findById(businessId);
            if (business == null) {
                throw new ApiException(404, String.format("There is no business with the id < %s >", request.params("businessId")));
            }
            response.status(201);
            return gson.toJson(businessDao.getAllAddressesForABusiness(businessId));
        });
        //get all causes
        get("/causes", "application/json", (request, response) -> {
            response.status(201);
            return gson.toJson(causeDao.getAll());
        });
        //get a cause
        get("/causes/:causeId", "application/json", (request, response) -> {
            int causeId = Integer.parseInt(request.params("causeId"));
            Cause cause = causeDao.findById(causeId);
            if (cause == null) {
                throw new ApiException(404, String.format("There is no cause with the id < %s >", request.params("causeId")));
            }
            response.status(201);
            return gson.toJson(cause);
        });
        //get all business for a cause
        get("/causes/:causeId/businesses", "application/json" ,(request, response) -> {
            int causeId = Integer.parseInt(request.params("causeId"));
            Cause cause = causeDao.findById(causeId);
            if (cause == null) {
                throw new ApiException(404, String.format("There is no cause with the id < %s >", request.params("causeId")));
            }
            response.status(201);
            return gson.toJson(causeDao.getAllBusinessesForCause(causeId));
        });
        //get all addresses for a cause
        get("/causes/:causeId/addresses", "application/json", (request, response) -> {
            int causeId = Integer.parseInt(request.params("causeId"));
            Cause cause = causeDao.findById(causeId);
            if (cause == null) {
                throw new ApiException(404, String.format("There is no cause with the id < %s >", request.params("causeId")));
            }
            response.status(201);
            return gson.toJson(causeDao.getAllAddressesForCause(causeId));
        });

        //get all tours
        get("/tours", "application/json", (request, response) -> {
            response.status(201);
            return gson.toJson(tourDao.getAllTours());
        });

        //get tours by id
        get("/tours/:tourId", "application/json", (request, response) -> {
            int tourId = Integer.parseInt(request.params("tourId"));
            Tour tour = tourDao.findById(tourId);
            if (tour == null){
                throw new ApiException(404, String.format("There is no tour with the id < %s >", request.params("causeId")));
            }
            response.status(201);
            return gson.toJson(tourDao.findById(tourId));
        });

        //get directions for a tour
        get("/tours/:tourId/directions", "application/json", (request, response) -> {
            int tourId = Integer.parseInt(request.params("tourId"));
            Tour tour = tourDao.findById(tourId);
            if (tour == null){
                throw new ApiException(404, String.format("There is no tour with the id < %s >", request.params("causeId")));
            }
            response.status(201);
            return tourDao.getDirectionsForATour(tourId);
        });

        //=======================update========================

        //update business info
        post("/businesses/:businessId/update", "application/json", (request, response) -> {
           int businessId = Integer.parseInt(request.params("businessId"));
            Business idCheck = businessDao.findById(businessId);
            if (idCheck == null) {
                throw new ApiException(404, String.format("There is no business with the id < %s >", request.params("businessId")));
            }
           Business business = gson.fromJson(request.body(), Business.class);
            if (business == null) {
                throw new ApiException(500, badData);
            }
           businessDao.update(businessId,business);
           response.status(201);
           return gson.toJson(businessDao.findById(businessId));
        });
        //update cause info
        post("/causes/:causeId/update", "application/json", (request, response) -> {
            int causeId = Integer.parseInt(request.params("causeId"));
            Cause idCheck = causeDao.findById(causeId);
            if (idCheck == null) {
                throw new ApiException(404, String.format("There is no cause with the id < %s >", request.params("causeId")));
            }
            Cause cause = gson.fromJson(request.body(), Cause.class);
            if (cause == null) {
                throw new ApiException(500, badData);
            }
            causeDao.update(causeId, cause.getName(), cause.getType(), cause.getDescription(), cause.getPhone());
            response.status(201);
            return gson.toJson(causeDao.findById(causeId));
        });

        //update address of a business
        post("/business/:businessId/address/:addressId/update", "application/json", (request, response) -> {
            int businessId = Integer.parseInt(request.params("businessId"));
            Business business = businessDao.findById(businessId);
            if (business == null) {
                throw new ApiException(404, String.format("There is no business with the id < %s >", request.params("businessId")));
            }
            int addressId = Integer.parseInt(request.params("addressId"));
            Address idCheck = addressDao.findById(addressId);
            if (idCheck == null) {
                throw new ApiException(404, String.format("There is no address with the id < %s >", request.params("addressId")));
            }
            Address address = gson.fromJson(request.body(), Address.class);
            if (address == null) {
                throw new ApiException(500, badData);
            }
            addressDao.update(addressId, address.getStreet(), address.getCity(), address.getState(), address.getZip());
            response.status(201);
            return gson.toJson(addressDao.findById(addressId));
        });
        //update address of a cause
        post("/causes/:causeId/address/:addressId/update", "application/json", (request, response) -> {
            int causeId = Integer.parseInt(request.params("causeId"));
            Cause cause = causeDao.findById(causeId);
            if (cause == null) {
                throw new ApiException(404, String.format("There is no cause with the id < %s >", request.params("causeId")));
            }
            int addressId = Integer.parseInt(request.params("addressId"));
            Address idCheck = addressDao.findById(addressId);
            if (idCheck == null) {
                throw new ApiException(404, String.format("There is no address with the id < %s >", request.params("addressId")));
            }
            Address address = gson.fromJson(request.body(), Address.class);
            if (address == null) {
                throw new ApiException(500, badData);
            }
            addressDao.update(addressId, address.getStreet(), address.getCity(), address.getState(), address.getZip());
            response.status(201);
            return gson.toJson(addressDao.findById(addressId));
        });
        //==========================delete=========================

        //delete a business
        post("/businesses/:businessId/delete", "application/json", (request, response) -> {
            int businessId = Integer.parseInt(request.params("businessId"));
            Business business = businessDao.findById(businessId);
            if (business == null) {
                throw new ApiException(404, String.format("There is no business with the id < %s >", request.params("businessId")));
            }
            businessDao.deleteById(businessId);
            response.status(201);
            return gson.toJson("Your business has been deleted");
        });
        //delete a cause
        post("/causes/:causeId/delete", "application/json", (request, response) -> {
            int causeId = Integer.parseInt(request.params("causeId"));
            Cause cause = causeDao.findById(causeId);
            if (cause == null) {
                throw new ApiException(404, String.format("There is no cause with the id < %s >", request.params("causeId")));
            }
            causeDao.deleteById(causeId);
            response.status(201);
            return gson.toJson("Your cause has been deleted");
        });
        //delete unused addresses
        post("/addresses/delete", "application/json", (request, response) -> {
            addressDao.deleteUnused();
            response.status(201);
            return gson.toJson("Unused addresses removed from database");
        });

        //history eraser button
        post("/delete", "application/json", (request, response) -> {
            addressDao.deleteAll();
            businessDao.deleteAll();
            causeDao.deleteAll();
            response.status(201);
            return gson.toJson("YOU HAVE JUST PRESSED THE HISTORY ERASER BUTTON! EVERYTHING IS DELETED!");
        });

        //==============================filters=============================

        exception(ApiException.class, ((exception, request, response) -> {
            ApiException error = (ApiException) exception;
            Map<String, Object> jsonMap = new HashMap<>();
            jsonMap.put("status", error.getStatusCode());
            jsonMap.put("errorMessage", error.getMessage());
            response.type("application/json");
            response.status(error.getStatusCode());
            response.body(gson.toJson(jsonMap));
        }));

        after((request, response) -> {
            response.type("application/json");
        });
    }

}
