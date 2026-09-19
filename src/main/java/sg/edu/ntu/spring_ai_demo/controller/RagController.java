package sg.edu.ntu.spring_ai_demo.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rag")
public class RagController {

    private final ChatClient chatClient;

    public RagController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    // Search the vector store with a filter expression based on the category tag
    // (e.g., "faq" or "products")
    private String searchWithFilterBuilder(String userQuery, String categoryTag) {

        // Programmatically construct "source == 'faq'" or "source == 'products'"
        // Must yield literally: source == 'faq' or source == 'products'
        String filterExpr = String.format("source == '%s'", categoryTag);

        return chatClient
                .prompt()
                .user(userQuery)
                .advisors(a -> a.param(QuestionAnswerAdvisor.FILTER_EXPRESSION, filterExpr))
                .call()
                .content();
    }

    @GetMapping("/faq")
    public String faq(@RequestParam String question) {
        return searchWithFilterBuilder(question, "faq");
    }

    @GetMapping("/product-info")
    public String productInfo(@RequestParam String question) {
        return searchWithFilterBuilder(question, "products");
    }

}
