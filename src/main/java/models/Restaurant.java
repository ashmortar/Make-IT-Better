package models;

import javax.print.DocFlavor;

public class Restaurant extends Business {
    private String food;
    private boolean needReservation;
    private String atmosphere;
    private boolean hasBar;
    private static final String DATABASE_TYPE = "restaurant";

    public Restaurant(String name, String phone, String website, String hours, String food, boolean needReservation, String atmosphere, boolean hasBar) {
        this.setName(name);
        this.setPhone(phone);
        this.setWebsite(website);
        this.setHours(hours);
        this.food = food;
        this.needReservation = needReservation;
        this.atmosphere = atmosphere;
        this.hasBar = hasBar;
        this.setType(DATABASE_TYPE);
    }

    public String getfood() {
        return food;
    }

    public void setfood(String food) {
        this.food = food;
    }

    public boolean isNeedReservation() {
        return needReservation;
    }

    public void setNeedReservation(boolean needReservation) {
        this.needReservation = needReservation;
    }

    public String getAtmosphere() {
        return atmosphere;
    }

    public void setAtmosphere(String atmosphere) {
        this.atmosphere = atmosphere;
    }

    public boolean isHasBar() {
        return hasBar;
    }

    public void setHasBar(boolean hasBar) {
        this.hasBar = hasBar;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Restaurant that = (Restaurant) o;

        if (needReservation != that.needReservation) return false;
        if (hasBar != that.hasBar) return false;
        if (!food.equals(that.food)) return false;
        return atmosphere.equals(that.atmosphere);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + food.hashCode();
        result = 31 * result + (needReservation ? 1 : 0);
        result = 31 * result + atmosphere.hashCode();
        result = 31 * result + (hasBar ? 1 : 0);
        return result;
    }
}
