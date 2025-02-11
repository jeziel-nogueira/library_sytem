package org.v2com.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Valid;
import org.v2com.dto.BookDTO;
import org.v2com.entity.BookEntity;
import org.v2com.exceptions.BookNotFoundException;
import org.v2com.exceptions.DataPersistException;
import org.v2com.repository.BookRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class BookService {

    private final BookRepository repository;
    private final BookRepository bookRepository;

    public BookService(BookRepository repository, BookRepository bookRepository) {
        this.repository = repository;
        this.bookRepository = bookRepository;
    }

    public List<BookDTO> findAllBooks() throws Exception {
        List<BookDTO> books = bookRepository.findAllBooks().stream().map(BookDTO::fromEntity).collect(Collectors.toList());
        if (books.isEmpty()) {
            throw new BookNotFoundException();
        }
        return books;
    }

    public BookDTO findBookById(UUID id) throws Exception {
        try {
            BookEntity book = repository.findById(id);
            if (book == null) {
                throw new BookNotFoundException();
            }
            return BookDTO.fromEntity(book);
        } catch (BookNotFoundException ex) {
            throw ex;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public List<BookDTO> searchBooksByArgs(String title, String author, String tag) throws Exception {
        try {
            List<BookDTO> bookDTOS = repository.findBooksByArgs(title, author, tag).stream()
                    .map(BookDTO::fromEntity)
                    .collect(Collectors.toList());
            if (bookDTOS == null || bookDTOS.isEmpty()) {
                throw new BookNotFoundException();
            }
            return bookDTOS;
        } catch (BookNotFoundException ex) {
            throw ex;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }


    public BookDTO persistBook(@Valid BookDTO bookDTO) throws Exception {
        try {
            BookEntity bookEntity = bookDTO.toEntity();
            repository.persist(bookEntity);
            return BookDTO.fromEntity(bookEntity);
        } catch (Exception e) {
            throw new DataPersistException();
        }
    }

    public BookDTO updateBook(@Valid BookDTO bookDTO) throws Exception {

        try {
            BookEntity existingBookEntity = repository.findById(bookDTO.getId());

            if (existingBookEntity == null) {
                throw new BookNotFoundException();
            }

            bookRepository.updateBook(bookDTO.toEntity(), existingBookEntity);

            return BookDTO.fromEntity(existingBookEntity);
        } catch (BookNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new DataPersistException();
        }
    }

    public void deleteBook(UUID id) throws Exception {
        try {
            BookEntity bookEntity = repository.findById(id);
            if (bookEntity == null) {
                throw new BookNotFoundException();
            }
            repository.deleteById(id);
        } catch (BookNotFoundException ex) {
            throw ex;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}
