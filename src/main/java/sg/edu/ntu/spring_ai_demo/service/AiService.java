package sg.edu.ntu.spring_ai_demo.service;

import sg.edu.ntu.spring_ai_demo.model.TicketAnalysis;

public interface AiService {

    String chat(String message);

    String support(String message);

    String recommendProduct(String message);

    String askStudyBuddy(String message);

    String suggestRecipe(String message);

    String askInterviewCoach(String message);

    String summarize(String text);

    String saveSummary(String summary);

    TicketAnalysis analyseTicket(String ticket);

}
