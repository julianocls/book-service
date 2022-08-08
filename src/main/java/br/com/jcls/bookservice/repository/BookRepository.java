package br.com.jcls.bookservice.repository;

import br.com.jcls.bookservice.model.Book;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Cacheable( cacheNames = "Book", key = "#id")
    Optional<Book> findById(Long id);

}
