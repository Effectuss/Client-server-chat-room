package edu.effectuss.chatmsg.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import edu.effectuss.chatmsg.ClientStatus;

@Getter
@Setter
public class MessageRequest implements Request {
    @JsonProperty("nickname")
    private String nickname;
    @JsonProperty("message")
    private String message;
    @JsonProperty("clientStatus")
    private ClientStatus clientStatus;

    public MessageRequest(@JsonProperty("message") String message,
                          @JsonProperty("nickname") String nickname,
                          @JsonProperty("clientStatus") ClientStatus clientStatus) {
        this.message = message;
        this.nickname = nickname;
        this.clientStatus = clientStatus;
    }
}
