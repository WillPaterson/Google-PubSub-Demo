package c3280751.Sub;

import c3280751.Util.JsonUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Sub {

    private String remoteUrl;
    Sub(String remoteUrl) {
        this.remoteUrl = remoteUrl;
    }

    public int sendMessageToEndpoint(String message) {
        try {
            // Create a json message
            String messageOut = JsonUtil.createMessage(createOutputMessage(Integer.parseInt(message)));

            // Connect to remote url
            URL url = new URL(remoteUrl);
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.setRequestProperty("Accept", "application/json");
            http.setRequestProperty("Content-Type", "application/json");

            // Send message
            OutputStream stream = http.getOutputStream();
            stream.write(messageOut.getBytes(StandardCharsets.UTF_8));

            // Handle incoming message
            http.disconnect();

            return http.getResponseCode();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Generated a new answer message
     * @param question question to answer
     * @return output message of answer
     */
    private static OutputMessage createOutputMessage(int question) {
        OutputMessage outputMessage = new OutputMessage();
        // Start timer
        long start3 = System.nanoTime();
        // Calculate primes
        outputMessage.setAnswer(generatePrimes(question));
        // End timer
        long end3 = System.nanoTime();
        // Time taken in seconds
        outputMessage.setTime_taken((end3 - start3) / 1_000_000.0);

        return outputMessage;
    }


    // TODO Maybe replace with brute force
    //  would also need to change from nano second. Maybe?
    /**
     * Calculate primes
     * @param max max prime to find
     * @return array of primes
     */
    // https://stackoverflow.com/questions/586284/finding-prime-numbers-with-the-sieve-of-eratosthenes-originally-is-there-a-bet
    private static int [] generatePrimes(int max) {
        boolean[] isComposite = new boolean[max + 1];
        for (int i = 2; i * i <= max; i++) {
            if (!isComposite [i]) {
                for (int j = i; i * j <= max; j++) {
                    isComposite [i*j] = true;
                }
            }
        }
        int numPrimes = 0;
        for (int i = 2; i <= max; i++) {
            if (!isComposite [i]) numPrimes++;
        }
        int [] primes = new int [numPrimes];
        int index = 0;
        for (int i = 2; i <= max; i++) {
            if (!isComposite [i]) primes [index++] = i;
        }
        return primes;
    }
}
