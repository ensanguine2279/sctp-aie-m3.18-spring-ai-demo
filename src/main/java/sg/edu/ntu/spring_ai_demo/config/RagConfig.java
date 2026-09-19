package sg.edu.ntu.spring_ai_demo.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RagConfig {

    @Bean
    public VectorStore vectorStore(EmbeddingModel embeddingModel) {
        // Create an in-memory vector store
        SimpleVectorStore store = SimpleVectorStore.builder(embeddingModel).build();

        // Read the FAQ file directly using the classpath path string
        TextReader readerFaq = new TextReader("classpath:faq.txt");
        List<Document> documentsFaq = readerFaq.get();

        TextReader readerProducts = new TextReader("classpath:products.txt");
        List<Document> documentsProducts = readerProducts.get();

        // Split into smaller chunks for better retrieval
        List<Document> chunksFaq = TokenTextSplitter.builder().build().apply(documentsFaq);
        List<Document> chunksProducts = TokenTextSplitter.builder().build().apply(documentsProducts);

        List<Document> chunks = new ArrayList<>();
        chunks.addAll(chunksFaq);
        chunks.addAll(chunksProducts);

        chunks.addAll(TokenTextSplitter.builder().build().apply(chunks));

        // Load chunks into the vector store
        // This is where embeddings are generated — each chunk is converted to a vector
        store.add(chunks);

        System.out.println("✅ FAQ data loaded into vector store — " + chunks.size() + " chunks");
        return store;
    }
}
