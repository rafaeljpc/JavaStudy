package io.github.rafaeljpc.server.oauthserver.resources;

public class Welcome {

    private static final String GREETINGS_FORMAT = "Welcome %s!";

    private String greetings;

    public Welcome() {}

    public Welcome(String who) {
        this.greetings = String.format(GREETINGS_FORMAT, who);
    }

    public String getGreetings() {
        return greetings;
    }

    public void setGreetings(String greetings) {
        this.greetings = greetings;
    }
}
