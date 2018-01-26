package models;

public class Bakery extends Business {
    private String specialty;
    private boolean glutenFree;
    private static final String DATABASE_TYPE = "bakery";

    public Bakery(String name, String phone, String website, String hours, String specialty, boolean glutenFree) {
        this.setName(name);
        this.setPhone(phone);
        this.setWebsite(website);
        this.setHours(hours);
        this.specialty = specialty;
        this.glutenFree = glutenFree;
        this.setType(DATABASE_TYPE);
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public boolean isGlutenFree() {
        return glutenFree;
    }

    public void setGlutenFree(boolean glutenFree) {
        this.glutenFree = glutenFree;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Bakery bakery = (Bakery) o;

        if (glutenFree != bakery.glutenFree) return false;
        return specialty.equals(bakery.specialty);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + specialty.hashCode();
        result = 31 * result + (glutenFree ? 1 : 0);
        return result;
    }
}
