package model;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String idAuth;
    private String idFirebase;
    private int id;
    private List<String> CarsInMyPossesion= new ArrayList<>();

    public User(String idAuth, String idFirebase, int id, List<String> carsInMyPossesion) {
        this.idAuth = idAuth;
        this.idFirebase = idFirebase;
        this.id = id;
        CarsInMyPossesion = carsInMyPossesion;
    }

    public String getIdFirebase() {
        return idFirebase;
    }

    public void setIdFirebase(String idFirebase) {
        this.idFirebase = idFirebase;
    }

    public User() {
    }

    public String getIdAuth() {
        return idAuth;
    }

    public void setIdAuth(String idAuth) {
        this.idAuth = idAuth;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getCarsInMyPossesion() {
        return CarsInMyPossesion;
    }

    public void setCarsInMyPossesion(List<String> carsInMyPossesion) {
        CarsInMyPossesion = carsInMyPossesion;
    }


    @Exclude
    public void newUser(String id)
    {
        this.idAuth = id;
    }
    @Exclude
    public void updateTheCarInPossession(String carId)
    {
        CarsInMyPossesion.add(carId);
    }


}
