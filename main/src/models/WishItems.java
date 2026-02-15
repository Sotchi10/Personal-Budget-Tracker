package models;

public class WishItems {
    private String item_name;
    private double item_price;

    public void WishItem(String item_name, double item_price) {
        setItem_name(item_name);
        setItem_price(item_price);
    }


    

    public String getItem_name() {
        return item_name;
    }

    public double getItem_price() {
        return item_price;
    }


    //calculate percentage progressions
    public double calculatePercentage(double balance, double price) {
        double percentage = (balance / price) * 100;
        percentage = Math.min(percentage, 100);
        return Math.round(percentage * 100.0) / 100.0;
    }




    //Setters
    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public void setItem_price(double item_price) {
        this.item_price = item_price;
    }

    
}
