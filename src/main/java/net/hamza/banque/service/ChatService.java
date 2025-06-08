package net.hamza.banque.service;


import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final ChatLanguageModel chatModel;
    private final String systemPrompt = "You are a helpful AI assistant powered by Google Gemini";

    public ChatService(ChatLanguageModel chatModel) {
        this.chatModel = chatModel;
    }

    public String chat(String userMessage) {
        return chatModel.generate(
                SystemMessage.from(systemPrompt),
                UserMessage.from(userMessage)
        ).content().text();
    }

    public String analyze(String content) {
        return chatModel.generate(
                SystemMessage.from("You are an expert analyst. Provide detailed analysis."),
                UserMessage.from(content)
        ).content().text();
    }

    public String generateCreative(String prompt) {
        return chatModel.generate(
                SystemMessage.from("You are a creative writing assistant"),
                UserMessage.from(prompt)
        ).content().text();
    }
}