package sg.edu.ntu.spring_ai_demo.config;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import sg.edu.ntu.spring_ai_demo.utils.PromptHelper;

@Configuration
public class RagConfig {
    Logger logger = LoggerFactory.getLogger(RagConfig.class);

    @Value("${spring.ai.demo.rag.prompt.system.role}")
    private String promptSystemRole;

    @Value("${spring.ai.demo.rag.prompt.system.scope}")
    private String promptSystemScope;

    @Value("${spring.ai.demo.rag.prompt.system.tone}")
    private String promptSystemTone;

    @Value("${spring.ai.demo.rag.prompt.system.boundaries}")
    private String promptSystemBoundaries;

    // Convert a resource file into chunks of documents with metadata indicating the
    // source
    private List<Document> toChunks(String resourceUrl, String source) {
        TextReader reader = new TextReader(resourceUrl);
        List<Document> docs = reader.get();

        // Add the source metadata to each document
        docs.forEach(d -> d.getMetadata().put("source", source));

        TokenTextSplitter splitter = TokenTextSplitter.builder().build();
        return splitter.apply(docs);
    }

    @Bean
    public VectorStore vectorStore(EmbeddingModel embeddingModel) {
        logger.info("Initializing vector store...");

        // Create an in-memory vector store
        SimpleVectorStore store = SimpleVectorStore.builder(embeddingModel).build();
        TokenTextSplitter splitter = TokenTextSplitter.builder().build();

        // Read the FAQ file directly using the classpath path string
        List<Document> chunksFaq = toChunks("classpath:faq.txt", "faq");
        logger.info("FAQ chunks created: " + chunksFaq.size());

        // Read the products catalogue file directly using the classpath path string
        List<Document> chunksProducts = toChunks("classpath:products.txt", "products");
        logger.info("Product chunks created: " + chunksProducts.size());

        List<Document> allChunks = new ArrayList<>();
        allChunks.addAll(chunksFaq);
        allChunks.addAll(chunksProducts);

        // Load chunks into the vector store
        // This is where embeddings are generated — each chunk is converted to a vector
        store.add(allChunks);

        logger.info("✅ FAQ and Product data loaded into vector store — " + allChunks.size() + " chunks");

        return store;
    }

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder, VectorStore vectorStore) {

        // Configure the chat client with the system prompt and the
        // question-answering advisor based on the vector store
        return builder
                .defaultSystem(
                        PromptHelper.getPrompt(
                                promptSystemRole, promptSystemScope, promptSystemTone,
                                promptSystemBoundaries))
                .defaultAdvisors(
                        QuestionAnswerAdvisor.builder(vectorStore)
                                .searchRequest(SearchRequest.builder().topK(4).build())
                                .build())
                .build();
    }
}
