package scalecube.scaleboot;

public class GreetingRequest {
    private String name;

    public GreetingRequest(String name) {
        this.name = name;
    }
    public String name() {
        return this.name;
    }
}
