package c3280751.Util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Utility method for creating json messages from objects
 */
public class JsonUtil {
    /**
     * Construct a message with a question number to be sent
     * @return string representation of message
     */
    public static String createMessage(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
    }
}
