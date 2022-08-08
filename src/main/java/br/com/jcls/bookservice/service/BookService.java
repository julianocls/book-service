package br.com.jcls.bookservice.service;

import br.com.jcls.bookservice.model.Book;
import br.com.jcls.bookservice.proxy.CambioProxy;
import br.com.jcls.bookservice.repository.BookRepository;
import br.com.jcls.bookservice.response.Cambio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@Service
public class BookService {

    @Autowired
    private Environment environment;

    @Autowired
    private BookRepository repository;

    @Autowired
    private CambioProxy cambioProxy;

    public Book findBook(Long id, String currency) {

        var bookOptional = repository.findById(id);
        if (!bookOptional.isPresent()) {
            throw new RuntimeException("Book not found!");
        }
        var book = bookOptional.get();

        var cambio = cambioProxy.getCambio(book.getPrice(), "USD", currency);

        var port = environment.getProperty("local.server.port");
        book.setCurrency(currency);
        book.setEnvironment(port);
        book.setPrice(cambio.getConvertedValue());

        return book;
    }

}
