package model;

import android.util.Log;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Cars {
    public Cars() {
    }



    private Map<String,Car> carListToMaker = null;
    private Map<String,Car> carListToModel = null;

    public Cars(List<Car> carList)
    {
        carListToMaker=new HashMap<String, Car>();
        carListToModel=new HashMap<String, Car>();

        for(int i = 0; i < carList.size(); i++)
        {

            carListToMaker.put(carList.get(i).getCar_Maker(),carList.get(i));
            carListToModel.put(carList.get(i).getCar_Model(),carList.get(i));
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
            sb.append(entry.getValue().getCar_Model());
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
            sb.append(entry.getValue().getCar_Maker());
            sb.append("\n");
            if (iter.hasNext()) {
                sb.append(',').append(' ');
            }
        }
        return sb.toString();
    }
}
