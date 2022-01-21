package uz.mvp.handlers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.cached.InlineQueryResultCachedDocument;
import uz.mvp.FreeBookBot;
import uz.mvp.entity.book.Book;
import uz.mvp.repository.book.BookRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Doston Bokhodirov, Tue 9:22 PM. 1/4/2022
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InlineHandler {
    private static final InlineHandler instance = new InlineHandler();
    private static final FreeBookBot BOT = FreeBookBot.getInstance();
    private static final BookRepository bookRepository = BookRepository.getInstance();

    public void handle(InlineQuery inlineQuery) {
        String text = inlineQuery.getQuery();
        AnswerInlineQuery answerInlineQuery = new AnswerInlineQuery();
        answerInlineQuery.setInlineQueryId(inlineQuery.getId());
        answerInlineQuery.setCacheTime(100000000);
        List<InlineQueryResult> results = new ArrayList<>();
        int i = 1;
        List<Book> books;
        if (text.equals("")) {
            books = bookRepository.getAllBooks(10, 0);
        }
        else {
            books = bookRepository.getBooksByName(text, 10, 0);
        }
        makeResult(results, i, books);
        answerInlineQuery.setResults(results);
        BOT.executeMessage(answerInlineQuery);
    }

    public void makeResult(List<InlineQueryResult> results, int i, List<Book> books) {
        for (Book book : books) {
            InlineQueryResultCachedDocument document = new InlineQueryResultCachedDocument();
            document.setId(Integer.toString(i));
            document.setDocumentFileId(book.getId());
            document.setTitle(book.getName());
            results.add(document);
            i++;
        }
    }

    public static InlineHandler getInstance() {
        return instance;
    }
}
