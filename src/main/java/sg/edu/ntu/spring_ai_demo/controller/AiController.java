package sg.edu.ntu.spring_ai_demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import sg.edu.ntu.spring_ai_demo.model.TicketAnalysis;
import sg.edu.ntu.spring_ai_demo.service.AiService;

@RestController
public class AiController {

        // AI service for handling tasks such as saving summaries
        private final AiService aiService;

        public AiController(AiService aiService) {
                this.aiService = aiService;
        }

        @GetMapping("/chat")
        public String chat(@RequestParam String message) {
                return aiService.chat(message);
        }

        @GetMapping("/support")
        public String support(@RequestParam String message) {
                return aiService.support(message);
        }

        // Product recommender endpoint
        @GetMapping("/product")
        public String recommendProduct(@RequestParam String message) {
                return aiService.recommendProduct(message);
        }

        // Study buddy endpoint
        @GetMapping("/study")
        public String studyBuddy(@RequestParam String message) {
                return aiService.askStudyBuddy(message);
        }

        // Recipe suggester endpoint
        @GetMapping("/recipe")
        public String recipeSuggester(@RequestParam String message) {
                return aiService.suggestRecipe(message);
        }

        // Interview coach endpoint
        @GetMapping("/interview")
        public String interviewCoach(@RequestParam String message) {
                return aiService.askInterviewCoach(message);
        }

        // Summarizer endpoint
        @GetMapping("/summarizer")
        public String summarizer(@RequestParam String text) {
                String summary = aiService.summarize(text);

                // Save the summary to the CSV file using AiService
                aiService.saveSummary(summary);

                return summary;
        }

        @GetMapping("/ticket/analyze")
        public String analyseTicket(@RequestParam String ticket) {
                TicketAnalysis analysis = aiService.analyseTicket(ticket);

                if (analysis.refundRequested()) {
                        return "Routed to FINANCE team — " + analysis.summary();
                }

                if (analysis.urgency().equalsIgnoreCase("HIGH")) {
                        return "Escalated to SENIOR SUPPORT — " + analysis.summary();
                }

                return "Added to standard " + analysis.category() + " queue — " + analysis.summary();
        }

}