package models.wishlists;

public class WishItems {
    private String item_name;
    private double item_price;

    //Constructor
    public WishItems(String item_name, double item_price) {
        setItem_name(item_name);
        setItem_price(item_price);
    }

    //Getter
    public String getItem_name() {
        return item_name;
    }

    public double getItem_price() {
        return item_price;
    }


    //calculate percentage progressions
    public double calculatePercentage(double balance, double price) {
        if (item_price <= 0) return 0;
        double percentage = (balance / price) * 100;
        percentage = Math.min(percentage, 100);
        return Math.round(percentage * 100.0) / 100.0;
    }

    //Setters
    public void setItem_name(String item_name) {
        if (item_name == null || item_name.isBlank()) {
            System.out.println("You must name your item");
            return;
        }
        this.item_name = item_name;
    }

    public void setItem_price(double item_price) {
        if (item_price < 0) {
            System.out.println("Set price for your item");
            return;
        }
        this.item_price = item_price;
    }
  
}
