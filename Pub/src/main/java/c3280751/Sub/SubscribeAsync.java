package c3280751.Sub;

import c3280751.Util.Message;
import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.gson.Gson;
import com.google.pubsub.v1.ProjectSubscriptionName;
import com.google.pubsub.v1.PubsubMessage;

/**
 * Subscribes to message queue waiting for questions to answer
 * Once answered sends a post request to remote url
 */
public class SubscribeAsync {
    /**
     * Main method for subscriber
     * @param args handles any cmd args
     */
    public static void main(String... args) {
        String projectId = System.getenv("PROJECT_ID");
        String subscriptionId = System.getenv("SUB_ID");
        String remoteUrl = System.getenv("REMOTE_URL");

        subscribeAsyncExample(projectId, subscriptionId, remoteUrl);
    }

    /**
     * Main subscriber method
     * Creates a subscriber to receive messages
     * Then sends answer as post to remote url
     * @param projectId google cloud project id
     * @param subscriptionId google cloud subscription queue id
     */
    public static void subscribeAsyncExample(String projectId, String subscriptionId, String remoteUrl) {

        System.out.println(projectId);
        System.out.println(subscriptionId);
        System.out.println(remoteUrl);

        ProjectSubscriptionName subscriptionName = ProjectSubscriptionName.of(projectId, subscriptionId);

        Gson gson = new Gson();

        // Instantiate an asynchronous message receiver.
        MessageReceiver receiver =
                (PubsubMessage pubsubMessage, AckReplyConsumer consumer) -> {

                    // Convert incoming message into Produce.Message object
                    Message message = new Gson().fromJson(pubsubMessage.getData().toStringUtf8(), Message.class);
                    Sub sub = new Sub(remoteUrl);

                    int response = sub.sendMessageToEndpoint(message.getQuestion());

                    System.out.printf("DEBUG: Id: %s %nReceived Data: %n%s %nResponse: %d%n%n", pubsubMessage.getMessageId(), pubsubMessage.getData().toStringUtf8(), response);
                    if (response == 200) {
                        // Ack the received message.
                        consumer.ack();
                    }
                };

        Subscriber subscriber  = Subscriber.newBuilder(subscriptionName, receiver).build();
        // Start the subscriber.
        subscriber.startAsync().awaitRunning();
        System.out.printf("Listening for messages on %s:%n", subscriptionName);
        subscriber.awaitTerminated();
    }
}