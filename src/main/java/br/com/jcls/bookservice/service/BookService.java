package br.com.jcls.bookservice.service;

import br.com.jcls.bookservice.model.Book;
import br.com.jcls.bookservice.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    @Autowired
    private Environment environment;

    @Autowired
    private BookRepository repository;

    public Book findBook(Long id, String currency) {

        var bookOptional = repository.findById(id);
        if (!bookOptional.isPresent()) { throw new RuntimeException("Book not found!"); }
        var book = bookOptional.get();

        var port = environment.getProperty("local.server.port");
        book.setCurrency(currency);
        book.setEnvironment(port);

        return book;
    }

}
