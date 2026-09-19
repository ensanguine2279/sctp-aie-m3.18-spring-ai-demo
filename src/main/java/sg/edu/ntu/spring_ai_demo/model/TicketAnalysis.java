package sg.edu.ntu.spring_ai_demo.model;

public record TicketAnalysis(
        String category,
        String urgency,
        boolean refundRequested,
        String summary) {
}
