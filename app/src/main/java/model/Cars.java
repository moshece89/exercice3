package model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Cars {
    public Cars() {
    }



    private Map<String, Car> carListToMaker = null;
    private Map<String,Car> carListToModel = null;

    public Map<String, Car> getCatalogue() {
        return catalogue;
    }

    public void setCatalogue(Map<String, Car> catalogue) {
        this.catalogue = catalogue;
    }

    private Map<String, Car> catalogue =null;

    public Map<String, Car> getCarListToMaker() {
        return carListToMaker;
    }

    public void setCarListToMaker(Map<String, Car> carListToMaker) {
        this.carListToMaker = carListToMaker;
    }

    public Map<String, Car> getCarListToModel() {
        return carListToModel;
    }

    public void setCarListToModel(Map<String, Car> carListToModel) {
        this.carListToModel = carListToModel;
    }



    public Cars(List<Car> carList)
    {
        carListToMaker=new HashMap<String, Car>();
        carListToModel=new HashMap<String, Car>();
        catalogue=new HashMap<String, Car>();

        for(int i = 0; i < carList.size(); i++)
        {
            carListToMaker.put(carList.get(i).getCar_maker()+carList.get(i).getId(),carList.get(i));
            carListToModel.put(carList.get(i).getCar_model()+carList.get(i).getId(),carList.get(i));
            catalogue.put(carList.get(i).getId(),carList.get(i));
        }
        carListToModel = new TreeMap<String, Car>(carListToModel);
        carListToMaker = new TreeMap<String, Car>(carListToMaker);
    }

}
