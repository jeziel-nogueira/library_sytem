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

    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    public List<BookDTO> findAllBooks() {
        return repository.findAllBooks().stream()
                .map(BookDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public BookDTO findBookById(UUID id) {
        BookEntity book = repository.findById(id);
        if (book == null) {
            throw new IllegalArgumentException("Livro nao encontrado para o ID fornecido.");
        }
        return BookDTO.fromEntity(book);
    }

    public List<BookDTO> searchBooksByArgs(String title, String author, String tag) {
        List<BookDTO> bookDTOS = repository.findBooksByArgs(title, author, tag).stream()
                .map(BookDTO::fromEntity)
                .collect(Collectors.toList());
        if(bookDTOS == null || bookDTOS.isEmpty()){
            throw new IllegalArgumentException("Nemhum resultado para a pesquisa");
        }
        return bookDTOS;
    }


    public BookDTO persistBook(@Valid BookDTO bookDTO) {
        BookEntity bookEntity = bookDTO.toEntity();
        repository.persist(bookEntity);
        return BookDTO.fromEntity(bookEntity);
    }

    public BookDTO updateBook(@Valid BookDTO bookDTO) {
        BookEntity existingBookEntity = repository.findById(bookDTO.getId());

        if (existingBookEntity == null) {
            throw new IllegalArgumentException("Livro nao encontrado para o ID fornecido.");
        }

        existingBookEntity.setTitle(bookDTO.getTitle());
        existingBookEntity.setAuthor(bookDTO.getAuthor());
        existingBookEntity.setDescription(bookDTO.getDescription());
        existingBookEntity.setGenre(bookDTO.getGenre());
        existingBookEntity.setIsbn(bookDTO.getIsbn());
        existingBookEntity.setStatus(bookDTO.getStatus());
        existingBookEntity.setPublisher(bookDTO.getPublisher());
        existingBookEntity.setTags(bookDTO.getTags());
        existingBookEntity.setCoverUrl(bookDTO.getCoverUrl());

        return BookDTO.fromEntity(existingBookEntity);
    }

    public void deleteBook(UUID id) {
        BookEntity bookEntity = repository.findById(id);
        if (bookEntity != null) {
            repository.deleteById(id);
        }
    }
}
