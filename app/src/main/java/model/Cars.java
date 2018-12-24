package model;

import java.util.List;

public class Cars {
    public Cars() {
    }

    public List<Car> getCarList() {
        return carList;
    }

    public void setCarList(List<Car> carList) {
        this.carList = carList;
    }

    private List<Car> carList = null;

    public Cars(List<Car> carList) {
        this.carList = carList;
    }
}
