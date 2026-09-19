package sg.edu.ntu.spring_ai_demo.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rag")
public class RagController {

    private final ChatClient clientFaq;
    private final ChatClient clientProducts;

    public RagController(ChatClient.Builder chatClientBuilder, VectorStore vectorStore) {
        this.clientFaq = chatClientBuilder
                .defaultSystem("You are a helpful customer support assistant for ACME CRM. " +
                        "Answer questions based only on the provided context. " +
                        "If the answer is not in the context, say you don't have that information " +
                        "and suggest contacting support@acmecrm.com.")
                .defaultAdvisors(QuestionAnswerAdvisor.builder(vectorStore).build())
                .build();

        this.clientProducts = chatClientBuilder
                .defaultSystem("You are a friendly product advisor for ACME Tech. " +
                        "Answer the user's questions using only the information provided in the context. " +
                        "Do not use outside knowledge or make assumptions. " +
                        "If the requested product or detail is not included in the context, " +
                        " clearly say that you do not have that information rather than guessing. " +
                        "Be helpful, concise, and polite.")
                .defaultAdvisors(QuestionAnswerAdvisor.builder(vectorStore).build())
                .build();
    }

    @GetMapping("/faq")
    public String faq(@RequestParam String question) {
        return clientFaq.prompt()
                .user(question)
                .call()
                .content();
    }

    @GetMapping("/product-info")
    public String productInfo(@RequestParam String question) {
        return clientProducts.prompt()
                .user(question)
                .call()
                .content();
    }

}
