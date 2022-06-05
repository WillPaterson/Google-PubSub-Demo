package c3280751.Pub;

import c3280751.Util.JsonUtil;
import c3280751.Util.Message;
import com.google.api.core.ApiFuture;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.TopicName;

import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Produces questions and sends to subscriber
 */
public class Producer {
    // Default settings
    private static int maxRandomQuestionNumber = 1000000;
    private static int waitTimeBetweenMessages = 1000;
    /**
     * Main method for producer controller
     * @param args handles any cmd args
     */
    public static void main(String... args) {
        String waitTimeEnvVar = System.getenv("WAIT_TIME");
        String MaxQuestionEnvVar = System.getenv("MAX_QUESTION");

        // Set wait time between messages
        if (waitTimeEnvVar != null) {
            waitTimeBetweenMessages = Integer.parseInt(waitTimeEnvVar);
            System.out.printf("Set wait between messages: %d%n", waitTimeBetweenMessages);
        }

        // Set max question random number
        if (MaxQuestionEnvVar != null) {
            maxRandomQuestionNumber = Integer.parseInt(MaxQuestionEnvVar);
            System.out.printf("Set max random question number: %d%n", maxRandomQuestionNumber);
        }

        // Add a Producer.Producer on a schedule timer
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    new Producer();

                } catch (IOException | ExecutionException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, 0, waitTimeBetweenMessages);
    }


    /**
     * Default constructor for
     */
    Producer() throws IOException, ExecutionException, InterruptedException {
        String projectId = System.getenv("PROJECT_ID");
        String topicId = System.getenv("TOPIC_ID");

        publish(projectId, topicId);
    }

    /**
     * Publish random number message
     * @param projectId id of project to publish to
     * @param topicId id of topic in project to publish to
     */
    private void publish(String projectId, String topicId) throws IOException, ExecutionException, InterruptedException {
        TopicName topicName = TopicName.of(projectId, topicId);

        com.google.cloud.pubsub.v1.Publisher publisher = null;
        try {
            // Create a publisher instance with default settings bound to the topic
            publisher = com.google.cloud.pubsub.v1.Publisher.newBuilder(topicName).build();

            String message = JsonUtil.createMessage(new Message(getRandom()));
            ByteString data = ByteString.copyFromUtf8(message);
            PubsubMessage pubsubMessage = PubsubMessage.newBuilder().setData(data).build();

            // Once published, returns a server-assigned message id (unique within the topic)
            ApiFuture<String> messageIdFuture = publisher.publish(pubsubMessage);
            String messageId = messageIdFuture.get();
            System.out.printf("Published message ID: %s %n%s %n", messageId, message);
        } finally {
            if (publisher != null) {
                // When finished with the publisher, shutdown to free up resources.
                publisher.shutdown();
                publisher.awaitTermination(1, TimeUnit.MINUTES);
            }
        }
    }

    /**
     * Get a random number between 1 and maxRandomQuestionNumber
     * @return String representation of random number
     */
    private String getRandom() {
        return String.valueOf(new Random().nextInt(maxRandomQuestionNumber) + 1);
    }
}
