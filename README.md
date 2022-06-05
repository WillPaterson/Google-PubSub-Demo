# Google-PubSub-Demo
A publisher/subscriber demo built using Google Cloud Pub/Sub

### How it works
- Actual goal is for the Publisher to generate a random number. 
- Random number is then pushed to a Google Cloud Pub/Sub topic in format:
```Json
{
  "question": "7"
}
```
- The Subscriber will then pull this message from the Google Cloud Pub/Sub topic
- The Subscriber will then calculate the array of prime numbers up to the recieved number
- After which is will send the calculated number and time taken to a remote url endpoint
- Message format for Subscriber:
```Json
{
 "answer": [2, 3, 5, 7],
 "time_taken": 600
}
```

## To get it running
1. Create a Google Cloud Project
   1. https://console.cloud.google.com/projectcreate 
   2. Enter **Project Name**
   3. Click **Create**

2. Create a Pub/Sub topic
   1. https://console.cloud.google.com/cloudpubsub/topic/list?modal=create_topic
   2. Enter **Topic ID**
   3. Click **Create Topic**

3. Create a Service Account
   1. https://console.cloud.google.com/projectselector/iam-admin/serviceaccounts/create?supportedpurview=project
   2. Select the **project** that was just created.
   3.	In the **Service account name** field, enter a name.
   4.	Click **Create and continue**. 
   5.	To provide access to your project, **grant** the following role(s) to your service account: **Pub/Sub > Pub/Sub Admin**.
   6.	Click **Continue**. 
   7.	Click **Done** to finish creating the service account.

4.	Create a Service Account key
    1. In the Cloud console, **click** the email address for the **service account** that you created. 
    2. Click **Keys**. 
    3. Click **Add key**, and then click **Create new key**. 
    4. Click **Create**. 
    5. Ensure **JSON key** is selected.
    6. Note the downloaded location path.
    7. Click **Close**.

5. Running the Publisher
   1. Ensure docker is installed
   2. Run “run publisher.bat”, running the bat will request inputs
   3. Input Project ID
   4. Input Topic ID
   5. Input auth.json key file.
      - Note this must be an absolute path
   6. Optional
      - Enter max question
      - Enter wait time between messages

6. Running the Subscriber
   1.	Ensure docker is installed
   2.	Run “run subscriber.bat”, running the bat will request inputs
   3.	Input Project ID
   4.	Input Subscription ID
   5. Input auth.json key file.
      - Note this must be an absolute path
   7.	Optional
    	- Enter remote URL


