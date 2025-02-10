package org.v2com.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Valid;
import org.v2com.dto.BookDTO;
import org.v2com.entity.BookEntity;
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
            throw new IllegalArgumentException("Nemhum livro encontrado");
        }
        return books;
    }

    public BookDTO findBookById(UUID id) throws Exception {
        try {
            BookEntity book = repository.findById(id);
            if (book == null) {
                throw new IllegalArgumentException("Livro nao encontrado para o ID fornecido.");
            }
            return BookDTO.fromEntity(book);
        } catch (Exception e) {
            throw new Exception("Erro ao realizar busca.", e);
        }
    }

    public List<BookDTO> searchBooksByArgs(String title, String author, String tag) throws Exception {
        try {
            List<BookDTO> bookDTOS = repository.findBooksByArgs(title, author, tag).stream()
                    .map(BookDTO::fromEntity)
                    .collect(Collectors.toList());
            if(bookDTOS == null || bookDTOS.isEmpty()){
                throw new IllegalArgumentException("Nemhum resultado para a pesquisa");
            }
            return bookDTOS;
        } catch (Exception e) {
            throw new Exception("Erro ao realizar busca.", e);
        }
    }


    public BookDTO persistBook(@Valid BookDTO bookDTO) throws Exception {
        try {
            BookEntity bookEntity = bookDTO.toEntity();
            repository.persist(bookEntity);
            return BookDTO.fromEntity(bookEntity);
        } catch (Exception e) {
            throw new Exception("Erro ao registrar", e);
        }
    }

    public BookDTO updateBook(@Valid BookDTO bookDTO) throws Exception {
        BookEntity existingBookEntity = repository.findById(bookDTO.getId());

        if (existingBookEntity == null) {
            throw new IllegalArgumentException("Livro nao encontrado para o ID fornecido.");
        }
        try {
            bookRepository.updateBook(bookDTO.toEntity(), existingBookEntity);

            return BookDTO.fromEntity(existingBookEntity);
        } catch (Exception e) {
            throw new Exception("Erro ao atualizar", e);
        }
    }

    public void deleteBook(UUID id) throws Exception {
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new Exception("Erro ao deletar", e);
        }
    }
}
