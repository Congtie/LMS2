package models;

public class Author extends BaseEntity {
    private String name;
    private String biography;

    public Author(int id, String name, String biography) {
        super(id);
        this.name = name;
        this.biography = biography;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    @Override
    public String toString() {
        return "Author{id=" + id + ", name='" + name + "'}";
    }
}