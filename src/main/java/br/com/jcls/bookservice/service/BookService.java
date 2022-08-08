package br.com.jcls.bookservice.service;

import br.com.jcls.bookservice.model.Book;
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

    public Book findBook(Long id, String currency) {

        var bookOptional = repository.findById(id);
        if (!bookOptional.isPresent()) {
            throw new RuntimeException("Book not found!");
        }
        var book = bookOptional.get();

        var params = new HashMap<String, String>();
        params.put("amount", book.getPrice().toString());
        params.put("from", "USD");
        params.put("to", currency);

        var response = new RestTemplate().
                getForEntity("http://localhost:8000/cambio-service/{amount}/{from}/{to}", Cambio.class, params);

        var cambio = response.getBody();

        var port = environment.getProperty("local.server.port");
        book.setCurrency(currency);
        book.setEnvironment(port);
        book.setPrice(cambio.getConvertedValue());

        return book;
    }

}
