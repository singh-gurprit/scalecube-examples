package scalecube.scaleboot;

public class GreetingResponse {
    private String name;
    public GreetingResponse(String name) {
        this.name = name;
    }
    public String name() {
        return this.name;
    }
}
