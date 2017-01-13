package hello;

public class Greeting {
    private final long id;
    private final String fullName;

    public Greeting(long id, String fullName) {
        this.id = id;
        this.fullName = fullName;
    }

    //    @ApiModelProperty(position = 1, required = true, value = "User Id")
    public long getId() {
        return id;
    }

    //    @ApiModelProperty(position = 2, required = true)
    public String getFullName() {
        return fullName;
    }

    @Override
    public String toString() {
        return "Greeting{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                '}';
    }
}
