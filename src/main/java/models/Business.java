package models;

/**
 * Created by Guest on 1/24/18.
 */
public class Business {
    private int id;
    private String name;
    private String type;
    private String phone;
    private String website;

    public Business(String name, String type, String phone, String website) {
        this.name = name;
        this.type = type;
        this.phone = phone;
        this.website = website;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Business business = (Business) o;

        if (!name.equals(business.name)) return false;
        if (type != null ? !type.equals(business.type) : business.type != null) return false;
        if (phone != null ? !phone.equals(business.phone) : business.phone != null) return false;
        return website != null ? website.equals(business.website) : business.website == null;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (website != null ? website.hashCode() : 0);
        return result;
    }
}
