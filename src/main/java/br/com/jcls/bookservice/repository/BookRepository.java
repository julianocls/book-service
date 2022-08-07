package br.com.jcls.bookservice.repository;

import br.com.jcls.bookservice.model.Book;

import java.util.Optional;

//@Repository
public interface BookRepository  {

    Optional<Book> findById(Long id);

}
