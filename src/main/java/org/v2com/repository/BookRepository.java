package org.v2com.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.v2com.Enums.BookStatus;
import org.v2com.entity.BookEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static jakarta.transaction.Transactional.TxType.REQUIRED;

@ApplicationScoped
@Transactional(REQUIRED)
public class BookRepository {

    @Inject
    EntityManager entityManager;

    public BookEntity findById(UUID id) {
        return entityManager.find(BookEntity.class, id);
    }

    public List<BookEntity> findAllBooks() {
        try {
            return entityManager.createQuery("SELECT b FROM BookEntity b", BookEntity.class).getResultList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public BookEntity persist(BookEntity bookEntity) {
        entityManager.persist(bookEntity);
        return bookEntity;
    }

    public void deleteById(UUID id) {
        BookEntity bookEntity = entityManager.find(BookEntity.class, id);
        if (bookEntity != null) {
            entityManager.remove(bookEntity);
        }
    }

    public List<BookEntity> findBooksByArgs(String title, String author, String tag) {
        StringBuilder queryBuilder = new StringBuilder("SELECT b FROM BookEntity b WHERE 1=1");
        Map<String, Object> params = new HashMap<>();

        if (title != null && !title.isEmpty()) {
            queryBuilder.append(" AND LOWER(b.title) LIKE :title");
            params.put("title", "%" + title.toLowerCase() + "%");
        }
        if (author != null && !author.isEmpty()) {
            queryBuilder.append(" AND LOWER(b.author) LIKE :author");
            params.put("author", "%" + author.toLowerCase() + "%");
        }
        if (tag != null && !tag.isEmpty()) {
            queryBuilder.append(" AND LOWER(b.tags) LIKE :tag");
            params.put("tag", "%" + tag.toLowerCase() + "%");
        }

        TypedQuery<BookEntity> query = entityManager.createQuery(queryBuilder.toString(), BookEntity.class);

        // Substituir os parâmetros
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        // Log da query final com valores reais
        System.out.println("Query Final: " + queryBuilder);
        System.out.println("Parâmetros: " + params);

        return query.getResultList();
    }




    public void changeBookStatus(BookEntity bookEntity, BookStatus status){
        bookEntity = findById(bookEntity.id);
        bookEntity.setStatus(status);
        entityManager.merge(bookEntity);
    }

    public BookEntity updateBook(BookEntity bookEntity, BookEntity existingBookEntity){
        existingBookEntity.setTitle(bookEntity.getTitle());
        existingBookEntity.setAuthor(bookEntity.getAuthor());
        existingBookEntity.setDescription(bookEntity.getDescription());
        existingBookEntity.setIsbn(bookEntity.getIsbn());
        existingBookEntity.setGenre(bookEntity.getGenre());
        existingBookEntity.setPublisher(bookEntity.getPublisher());
        existingBookEntity.setTags(bookEntity.getTags());
        existingBookEntity.setCoverUrl(bookEntity.getCoverUrl());
        entityManager.merge(existingBookEntity);
        return existingBookEntity;
    }
}
