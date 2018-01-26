package models;

/**
 * Created by Guest on 1/24/18.
 */
public class Cause {
    private int id;
    private String name;
    private String type;
    private String description;
    private String phone;

    public Cause(String name, String type, String description, String phone) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.phone = phone;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cause cause = (Cause) o;

        if (!name.equals(cause.name)) return false;
        if (type != null ? !type.equals(cause.type) : cause.type != null) return false;
        if (description != null ? !description.equals(cause.description) : cause.description != null) return false;
        return phone != null ? phone.equals(cause.phone) : cause.phone == null;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        return result;
    }
}
