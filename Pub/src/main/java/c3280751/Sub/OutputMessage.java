package c3280751.Sub;

import lombok.Getter;
import lombok.Setter;

/**
 * Used to store message information of answered question
 */
@Setter
@Getter
public class OutputMessage {
    private int[] answer;
    private double time_taken;
}
