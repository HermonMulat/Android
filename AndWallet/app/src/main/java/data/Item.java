package data;

public class Item {

    private String title;
    private double price;
    private boolean isIncome;

    public Item(String title, double price, boolean isIncome) {
        this.title = title;
        this.price = price;
        this.isIncome = isIncome;
    }

    public boolean isIncome() {
        return isIncome;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPriceDisplay(){
        return String.format("%.2f",price) + "$";
    }




}
