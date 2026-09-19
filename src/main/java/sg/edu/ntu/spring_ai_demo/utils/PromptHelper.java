
package sg.edu.ntu.spring_ai_demo.utils;

public class PromptHelper {
    public static String getPrompt(String role, String scope, String tone, String boundaries) {
        return String.format("Role: %s\nScope: %s\nTone: %s\nBoundaries: %s",
                role, scope, tone, boundaries);
    }
}
