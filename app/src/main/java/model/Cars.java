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

    private Map<String,Car> carListToModel = null;

    public Cars(List<Car> carList)
    {
        carListToMaker=new HashMap<String, Car>();
        carListToModel=new HashMap<String, Car>();

        for(int i = 0; i < carList.size(); i++)
        {
            carListToMaker.put(carList.get(i).getCar_maker()+carList.get(i).getId(),carList.get(i));
            carListToModel.put(carList.get(i).getCar_model()+carList.get(i).getId(),carList.get(i));
        }
        carListToModel = new TreeMap<String, Car>(carListToModel);
        carListToMaker = new TreeMap<String, Car>(carListToMaker);
    }

    public String printMaker()
    {
        StringBuilder sb = new StringBuilder();
        Iterator<Map.Entry<String, Car>> iter = carListToMaker.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, Car> entry = iter.next();
            sb.append(entry.getKey());
            sb.append('=').append('"');
            sb.append(entry.getValue().getCar_model());
            sb.append('"');
            if (iter.hasNext()) {
                sb.append(',').append(' ');
            }
        }
        return sb.toString();
    }

    public String printModel()
    {
        StringBuilder sb = new StringBuilder();
        Iterator<Map.Entry<String, Car>> iter = carListToModel.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, Car> entry = iter.next();
            sb.append(entry.getKey());
            sb.append(':');
            sb.append(entry.getValue().getCar_maker());
            sb.append("\n");
            if (iter.hasNext()) {
                sb.append(',').append(' ');
            }
        }
        return sb.toString();
    }

}
