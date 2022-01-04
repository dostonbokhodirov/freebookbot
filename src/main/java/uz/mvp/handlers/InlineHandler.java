package uz.mvp.handlers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultDocument;
import uz.mvp.FreeBookBot;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Doston Bokhodirov, Tue 9:22 PM. 1/4/2022
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InlineHandler {
    private static final InlineHandler instance = new InlineHandler();
    private static final FreeBookBot BOT = FreeBookBot.getInstance();

    public void handle(InlineQuery inlineQuery) {
        AnswerInlineQuery answerInlineQuery = new AnswerInlineQuery();
        answerInlineQuery.setInlineQueryId(inlineQuery.getId());
        List<InlineQueryResult> results = new ArrayList<>();
        InlineQueryResultDocument inlineQueryResultDocument = new InlineQueryResultDocument();
        inlineQueryResultDocument.setMimeType("application/pdf");
        inlineQueryResultDocument.setId("12");
        inlineQueryResultDocument.setDocumentUrl("www.google.com");
        inlineQueryResultDocument.setTitle("Document");
        answerInlineQuery.setResults(results);
        BOT.executeMessage(answerInlineQuery);
    }

    public static InlineHandler getInstance() {
        return instance;
    }
}
