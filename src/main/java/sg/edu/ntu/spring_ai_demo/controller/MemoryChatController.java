package sg.edu.ntu.spring_ai_demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import sg.edu.ntu.spring_ai_demo.service.MemoryChatService;

@RestController
@RequestMapping("/memory")
public class MemoryChatController {

    private final MemoryChatService memoryChatService;

    public MemoryChatController(MemoryChatService memoryChatService) {
        this.memoryChatService = memoryChatService;
    }

    @GetMapping("/chat")
    public String memoryChat(
            @RequestParam String message,
            @RequestParam(defaultValue = "default-session") String sessionId) {
        return memoryChatService.memoryChat(message, sessionId);
    }

    @GetMapping("/crm/assistant")
    public String crmAssistant(
            @RequestParam String message,
            @RequestParam(defaultValue = "default-session") String sessionId) {
        return memoryChatService.crmAssistant(message, sessionId);
    }

}
