package edu.effectuss.chatmsg.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import edu.effectuss.chatmsg.ClientStatus;

@Getter
@Setter
public class MessageResponse implements Response {
    @JsonProperty("nickname")
    private String nickname;
    @JsonProperty("chatMessage")
    private String message;
    @JsonProperty("clientStatus")
    private ClientStatus clientStatus;

    public MessageResponse(@JsonProperty("nickname") String nickname,
                           @JsonProperty("chatMessage") String message,
                           @JsonProperty("clientStatus") ClientStatus clientStatus) {
        this.nickname = nickname;
        this.message = message;
        this.clientStatus = clientStatus;
    }
}
