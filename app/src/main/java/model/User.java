package model;

public class User {
        private int idAuth;
        private int id;
        private Cars CarsWantToBuy;

    public User(int idAuth, int id, Cars carsWantToBuy, Cars carsInMyPossesion) {
        this.idAuth = idAuth;
        this.id = id;
        CarsWantToBuy = carsWantToBuy;
        CarsInMyPossesion = carsInMyPossesion;
    }

    public User() {
    }

    public int getIdAuth() {
        return idAuth;
    }

    public void setIdAuth(int idAuth) {
        this.idAuth = idAuth;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cars getCarsWantToBuy() {
        return CarsWantToBuy;
    }

    public void setCarsWantToBuy(Cars carsWantToBuy) {
        CarsWantToBuy = carsWantToBuy;
    }

    public Cars getCarsInMyPossesion() {
        return CarsInMyPossesion;
    }

    public void setCarsInMyPossesion(Cars carsInMyPossesion) {
        CarsInMyPossesion = carsInMyPossesion;
    }

    private Cars CarsInMyPossesion;

}
