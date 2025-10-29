package kfu.itis.maslennikov.financetracker.entity;

public class Tag {
    private Long id;
    private Long userId;
    private String name;
    private String color; // hex code, e.g "#FF6600"

    public Tag() {
    }

    public Tag(Long id, Long userId, String name, String color) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.color = color;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}