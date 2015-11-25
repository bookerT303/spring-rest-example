package hello;

public class Greeting {
    private final long id;
    private final String content;

    public Greeting(long id, String content) {
        this.id = id;
        this.content = content;
    }

    //    @ApiModelProperty(position = 1, required = true, value = "User Id")
    public long getId() {
        return id;
    }

    //    @ApiModelProperty(position = 2, required = true)
    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "Greeting{" +
                "id=" + id +
                ", content='" + content + '\'' +
                '}';
    }
}
