package model;

import java.util.List;

public class ListOfProducts {
    private List<Product> productList = null;

    public ListOfProducts(List<Product> productList) {
        this.productList = productList;
    }

    public void view()
    {
        for(int i = 0; i< productList.size(); i++)
        {
            productList.get(i).print();
        }
    }
}
