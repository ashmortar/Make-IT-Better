package models;

public class Bar extends Business{
    private String food;
    private String atmosphere;
    private boolean hasTaps;
    private boolean hasCocktails;
    private static final String DATABASE_TYPE = "bar";

    public Bar(String name, String phone, String website, String hours, String food, String atmosphere, boolean hasTaps, boolean hasCocktails)  {
        this.setName(name);
        this.setPhone(phone);
        this.setWebsite(website);
        this.setHours(hours);
        this.food = food;
        this.atmosphere = atmosphere;
        this.hasTaps = hasTaps;
        this.hasCocktails = hasCocktails;
        this.setType(DATABASE_TYPE);
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public String getAtmosphere() {
        return atmosphere;
    }

    public void setAtmosphere(String atmosphere) {
        this.atmosphere = atmosphere;
    }

    public boolean isHasTaps() {
        return hasTaps;
    }

    public void setHasTaps(boolean hasTaps) {
        this.hasTaps = hasTaps;
    }

    public boolean isHasCocktails() {
        return hasCocktails;
    }

    public void setHasCocktails(boolean hasCocktails) {
        this.hasCocktails = hasCocktails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Bar bar = (Bar) o;

        if (hasTaps != bar.hasTaps) return false;
        if (hasCocktails != bar.hasCocktails) return false;
        if (!food.equals(bar.food)) return false;
        return atmosphere.equals(bar.atmosphere);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + food.hashCode();
        result = 31 * result + atmosphere.hashCode();
        result = 31 * result + (hasTaps ? 1 : 0);
        result = 31 * result + (hasCocktails ? 1 : 0);
        return result;
    }
}
