package models;

public class Cafe extends Business {
    private String food;
    private boolean fairTrade;
    private static final String DATABASE_TYPE = "cafe";

    public Cafe(String name, String phone, String website, String hours, String food, boolean fairTrade) {
        this.setName(name);
        this.setPhone(phone);
        this.setWebsite(website);
        this.setHours(hours);
        this.food = food;
        this.fairTrade = fairTrade;
        this.setType(DATABASE_TYPE);
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public boolean isFairTrade() {
        return fairTrade;
    }

    public void setFairTrade(boolean fairTrade) {
        this.fairTrade = fairTrade;
    }

    public static String getDatabaseType() {
        return DATABASE_TYPE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Cafe cafe = (Cafe) o;

        if (fairTrade != cafe.fairTrade) return false;
        return food.equals(cafe.food);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + food.hashCode();
        result = 31 * result + (fairTrade ? 1 : 0);
        return result;
    }
}
