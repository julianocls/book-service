package br.com.jcls.bookservice.service;

import br.com.jcls.bookservice.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class BookService {

    @Autowired
    private Environment environment;

    //@Autowired
    //private BookRepository repository;

    public Book findBook(Long id, String currency) {

        //var book = repository.findById(id);
        //if (!book.isPresent()) { throw new RuntimeException("Book not found!"); }

        var port = environment.getProperty("local.server.port");

        return new Book(1L, "Negel Poulton", new Date(), Double.valueOf(13.7), "Docker Deep Dive", currency, port);
    }

}
