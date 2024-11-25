package org.example;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.dialogflow.v2.*;
import com.google.protobuf.ByteString;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

public class DialogflowService {
    private final SessionsClient sessionsClient;
    private final String projectId;
    private final String sessionId;

    public DialogflowService(String projectId, String keyFilePath) throws IOException {
        this.projectId = projectId;
        this.sessionId = UUID.randomUUID().toString();

        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(keyFilePath));
        SessionsSettings sessionsSettings = SessionsSettings.newBuilder()
                .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                .build();
        this.sessionsClient = SessionsClient.create(sessionsSettings);
    }

    public String getBotResponse(String text) throws IOException {
        SessionName session = SessionName.of(projectId, sessionId);

        TextInput.Builder textInput = TextInput.newBuilder().setText(text).setLanguageCode("en-US");
        QueryInput queryInput = QueryInput.newBuilder().setText(textInput).build();

        DetectIntentRequest request = DetectIntentRequest.newBuilder()
                .setSession(session.toString())
                .setQueryInput(queryInput)
                .build();

        DetectIntentResponse response = sessionsClient.detectIntent(request);
        QueryResult queryResult = response.getQueryResult();

        return queryResult.getFulfillmentText();
    }
}
