package sg.edu.ntu.spring_ai_demo.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ai.chat.client.ChatClient;

import sg.edu.ntu.spring_ai_demo.model.TicketAnalysis;
import sg.edu.ntu.spring_ai_demo.service.AiService;
import sg.edu.ntu.spring_ai_demo.utils.PromptHelper;

@Service
public class AiServiceImpl implements AiService {

        Logger logger = LoggerFactory.getLogger(AiServiceImpl.class);

        // Study buddy system prompt properties
        @Value("${spring.ai.demo.study.prompt.system.role}")
        private String studySystemPromptRole;

        @Value("${spring.ai.demo.study.prompt.system.scope}")
        private String studySystemPromptScope;

        @Value("${spring.ai.demo.study.prompt.system.tone}")
        private String studySystemPromptTone;

        @Value("${spring.ai.demo.study.prompt.system.boundaries}")
        private String studySystemPromptBoundaries;

        // Product recommender system prompt properties
        @Value("${spring.ai.demo.product.prompt.system.role}")
        private String pdtSystemPromptRole;

        @Value("${spring.ai.demo.product.prompt.system.scope}")
        private String pdtSystemPromptScope;

        @Value("${spring.ai.demo.product.prompt.system.tone}")
        private String pdtSystemPromptTone;

        @Value("${spring.ai.demo.product.prompt.system.boundaries}")
        private String pdtSystemPromptBoundaries;

        // Recipe suggester system prompt properties
        @Value("${spring.ai.demo.recipe.prompt.system.role}")
        private String recipeSystemPromptRole;

        @Value("${spring.ai.demo.recipe.prompt.system.scope}")
        private String recipeSystemPromptScope;

        @Value("${spring.ai.demo.recipe.prompt.system.tone}")
        private String recipeSystemPromptTone;

        @Value("${spring.ai.demo.recipe.prompt.system.boundaries}")
        private String recipeSystemPromptBoundaries;

        // Interview coach system prompt properties
        @Value("${spring.ai.demo.interview.prompt.system.role}")
        private String interviewSystemPromptRole;

        @Value("${spring.ai.demo.interview.prompt.system.scope}")
        private String interviewSystemPromptScope;

        @Value("${spring.ai.demo.interview.prompt.system.tone}")
        private String interviewSystemPromptTone;

        @Value("${spring.ai.demo.interview.prompt.system.boundaries}")
        private String interviewSystemPromptBoundaries;

        // Summarizer system prompt properties
        @Value("${spring.ai.demo.summarizer.prompt.system.role}")
        private String summarizerSystemPromptRole;

        @Value("${spring.ai.demo.summarizer.prompt.system.scope}")
        private String summarizerSystemPromptScope;

        @Value("${spring.ai.demo.summarizer.prompt.system.tone}")
        private String summarizerSystemPromptTone;

        @Value("${spring.ai.demo.summarizer.prompt.system.boundaries}")
        private String summarizerSystemPromptBoundaries;

        @Value("${spring.ai.demo.csv.filepath}")
        private String CSV_FILE_PATH;

        @Value("${spring.ai.demo.ticket.prompt.user}")
        private String ticketUserPrompt;

        // Chat client is the main interface for interacting with the AI models
        private final ChatClient chatClient;

        public AiServiceImpl(ChatClient.Builder chatClientBuilder) {
                this.chatClient = chatClientBuilder.build();
        }

        public String chat(String message) {
                return chatClient.prompt()
                                .user(message)
                                .call()
                                .content();
        }

        public String support(String message) {
                return chatClient.prompt()
                                .system("You are a friendly and professional customer support assistant for a CRM software company. "
                                                +
                                                "You help users with questions about managing customers, contacts, and sales pipelines. "
                                                +
                                                "Keep your answers concise and practical. " +
                                                "If a question is not related to CRM or customer management, politely redirect the user.")
                                .user(message)
                                .call()
                                .content();
        }

        public String recommendProduct(String message) {
                return chatClient.prompt()
                                .system(PromptHelper.getPrompt(
                                                pdtSystemPromptRole,
                                                pdtSystemPromptScope,
                                                pdtSystemPromptTone,
                                                pdtSystemPromptBoundaries))
                                .user(message)
                                .call()
                                .content();
        }

        public String askStudyBuddy(String message) {
                return chatClient.prompt()
                                .system(PromptHelper.getPrompt(
                                                studySystemPromptRole,
                                                studySystemPromptScope,
                                                studySystemPromptTone,
                                                studySystemPromptBoundaries))
                                .user(message)
                                .call()
                                .content();
        }

        public String suggestRecipe(String message) {
                return chatClient.prompt()
                                .system(PromptHelper.getPrompt(
                                                recipeSystemPromptRole,
                                                recipeSystemPromptScope,
                                                recipeSystemPromptTone,
                                                recipeSystemPromptBoundaries))
                                .user(message)
                                .call()
                                .content();
        }

        public String askInterviewCoach(String message) {
                return chatClient.prompt()
                                .system(PromptHelper.getPrompt(
                                                interviewSystemPromptRole,
                                                interviewSystemPromptScope,
                                                interviewSystemPromptTone,
                                                interviewSystemPromptBoundaries))
                                .user(message)
                                .call()
                                .content();
        }

        public String summarize(String text) {
                return chatClient.prompt()
                                .system(PromptHelper.getPrompt(
                                                summarizerSystemPromptRole,
                                                summarizerSystemPromptScope,
                                                summarizerSystemPromptTone,
                                                summarizerSystemPromptBoundaries))
                                .user(text)
                                .call()
                                .content();
        }

        public String saveSummary(String summary) {
                try {
                        // File saved to resource path specified in application.properties
                        Files.writeString(Path.of(CSV_FILE_PATH), summary);
                        return "File saved successfully.";
                } catch (IOException e) {
                        return "Could not save the file: " + e.getMessage();
                }
        }

        public TicketAnalysis analyseTicket(String ticket) {
                logger.info("Analysing ticket: {}, with user prompt: {}", ticket, ticketUserPrompt);
                return chatClient.prompt()
                                .user(u -> u.text(ticketUserPrompt)
                                                .param("ticket", ticket))
                                .call()
                                .entity(TicketAnalysis.class);
        }
}
