package com.chatbot;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ChatbotTest
{
    static String token;
    static String url;
    private static ObjectMapper objectMapper = new ObjectMapper();

    private static Map<String, Object> chatFile;
    static class Chat
    {
        boolean newChat;
        String chat;

        String expectedResponse;

        public Chat(boolean isNew, String chat, String expectedResponse) {
            this.newChat = isNew;
            this.chat = chat;
            this.expectedResponse = expectedResponse;
        }

        public Chat() {
        }

        public boolean isNewChat() {
            return newChat;
        }

        public void setNewChat(boolean newChat) {
            this.newChat = newChat;
        }

        public String getChat() {
            return chat;
        }

        public void setChat(String chat) {
            this.chat = chat;
        }

        public String getExpectedResponse() {
            return expectedResponse;
        }

        public void setExpectedResponse(String expectedResponse) {
            this.expectedResponse = expectedResponse;
        }
    }

    public static List<Chat> getChatList()
    {
        List<Chat> chatList = new ArrayList<>();
        try {
            List<Chat> tests = getTestList();
            for (Chat test : tests) {
                chatList.add(test);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return chatList;
    }

    @BeforeAll
    public static void setUp()
    {
        try {
            chatFile = objectMapper.readValue(new File("/Users/antjain/scode/selflearn/junit-chatbot/input.json"), new TypeReference<Map<String, Object>>() {
            });
            setUpEnv();
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    private static void setUpEnv()
    {
        Map<String, String> envMap = (Map<String, String> )chatFile.get("env");
        token = envMap.get("token");
        url = envMap.get("url");
    }

    public static List<Chat> getTestList() throws IOException {
        return (List<Chat>) ((List) chatFile.get("test")).stream()
                .map(map -> objectMapper.convertValue(map, Chat.class))
                .collect(Collectors.toList());
    }

    @ParameterizedTest
    @MethodSource("getChatList")
    public void testChatbot(Chat chat)
    {
        assertTrue(chat.newChat);
    }

    public static Map<String, Object> sendPostRequest(String url, Map<String, Object> requestBody) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        // Convert request body to JSON string (optional)
        // You might need a JSON library like Jackson for this
        // String requestBodyJson = new ObjectMapper().writeValueAsString(requestBody);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(null))  // Send request body as map
                .header("Content-Type", "application/json") // Set content type (optional, might be inferred)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int statusCode = response.statusCode();
        String responseBody = response.body();

        if (statusCode != 200) {
            throw new RuntimeException("Error: Status code " + statusCode + ", Response: " + responseBody);
        }

        // Parse JSON response to a Map (optional)
        // You might need a JSON library like Jackson for this
        // Map<String, Object> responseMap = new ObjectMapper().readValue(responseBody, Map.class);

        return Map.of("statusCode", statusCode, "responseBody", responseBody);  // Return basic response info
    }
}
