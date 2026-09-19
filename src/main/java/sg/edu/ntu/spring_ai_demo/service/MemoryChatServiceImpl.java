
package sg.edu.ntu.spring_ai_demo.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import sg.edu.ntu.spring_ai_demo.utils.PromptHelper;

@Service
public class MemoryChatServiceImpl implements MemoryChatService {

    @Value("${spring.ai.demo.crm.prompt.system.role}")
    private String crmAssistantSystemRole;

    @Value("${spring.ai.demo.crm.prompt.system.scope}")
    private String crmAssistantSystemScope;

    @Value("${spring.ai.demo.crm.prompt.system.tone}")
    private String crmAssistantSystemTone;

    @Value("${spring.ai.demo.crm.prompt.system.boundaries}")
    private String crmAssistantSystemBoundaries;

    private final ChatClient chatClient;

    public MemoryChatServiceImpl(ChatClient.Builder chatClientBuilder, ChatMemory chatMemory) {
        this.chatClient = chatClientBuilder
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }

    @Override
    public String memoryChat(String message, String sessionId) {
        return chatClient.prompt()
                .user(message)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, sessionId))
                .call()
                .content();
    }

    @Override
    public String crmAssistant(String message, String sessionId) {
        return chatClient.prompt()
                .system(PromptHelper.getPrompt(
                        crmAssistantSystemRole,
                        crmAssistantSystemScope,
                        crmAssistantSystemTone,
                        crmAssistantSystemBoundaries))
                .user(message)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, sessionId))
                .call()
                .content();
    }
}
