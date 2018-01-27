package model.domain;

public class User {
    private final int id;
    private String name;
    private String pan;

    public String getPan() {
        return pan;
    }

    public User setPan(String pan) {
        this.pan = pan;
        return this;
    }

    public User(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        if (id != user.id) return false;
        return name != null ? name.equals(user.name) : user.name == null;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
