package edu.effectuss.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import edu.effectuss.chatmsg.ChatMessage;
import edu.effectuss.serialization.exception.MessageSerializationException;

@Log4j2
public final class MessageSerializationUtil {
    private static final String ERROR_MESSAGE_SERIALIZATION = "Can't serialize message: ";
    private static final String ERROR_MESSAGE_DESERIALIZATION = "Can't deserialize message: ";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static synchronized String serializeMessage(ChatMessage request)
            throws MessageSerializationException {
        try {
            return OBJECT_MAPPER.writeValueAsString(request);
        } catch (IllegalArgumentException | JsonProcessingException e) {
            log.error(ERROR_MESSAGE_SERIALIZATION + "{}", request, e);
            throw new MessageSerializationException(ERROR_MESSAGE_SERIALIZATION, e);
        }
    }

    public static synchronized <T extends ChatMessage> T deserializeMessage(String message,
                                                                            Class<T> clazz)
            throws MessageSerializationException {
        try {
            return OBJECT_MAPPER.readValue(message, clazz);
        } catch (IllegalArgumentException | JsonProcessingException e) {
            log.error(ERROR_MESSAGE_DESERIALIZATION + "{}", message, e);
            throw new MessageSerializationException(
                    ERROR_MESSAGE_DESERIALIZATION + message, e
            );
        }
    }

    private MessageSerializationUtil() {

    }
}
