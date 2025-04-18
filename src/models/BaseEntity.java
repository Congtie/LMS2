package models;

public abstract class BaseEntity {
    protected int id;
    protected String createdAt;

    public BaseEntity(int id) {
        this.id = id;
        this.createdAt = java.time.LocalDateTime.now().toString();
    }

    public int getId() {
        return id;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}