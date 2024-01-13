package edu.effectuss.chatmsg.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RegistrationResponse implements Response {
    @JsonProperty("success")
    private boolean success;
    @JsonProperty("activeUsers")
    private List<String> activeUsers;
    @JsonProperty("nickname")
    private String nickname;

    public RegistrationResponse(@JsonProperty("success") boolean success,
                                @JsonProperty("activeUsers") List<String> activeUsers,
                                @JsonProperty("nickname") String nickname) {
        this.success = success;
        this.activeUsers = activeUsers;
        this.nickname = nickname;
    }
}
