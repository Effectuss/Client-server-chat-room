package edu.effectuss.chatmsg.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class RegistrationRequest implements Request {
    @JsonProperty("nickname")
    private String nickname;

    public RegistrationRequest(@JsonProperty("nickname") String nickname) {
        this.nickname = nickname;
    }
}
