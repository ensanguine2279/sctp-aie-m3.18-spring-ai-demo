package sg.edu.ntu.spring_ai_demo.service;

public interface MemoryChatService {
    String memoryChat(String message, String sessionId);

    String crmAssistant(String message, String sessionId);
}
