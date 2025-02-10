package org.v2com.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.v2com.Enums.BookStatus;
import org.v2com.entity.BookEntity;

import java.util.List;
import java.util.UUID;

import static jakarta.transaction.Transactional.TxType.REQUIRED;

@ApplicationScoped
@Transactional(REQUIRED)
public class BookRepository {

    @Inject
    EntityManager entityManager;

    public BookEntity findById(UUID id) {
        try {
            return entityManager.find(BookEntity.class, id);
        } catch (Exception e) {
            return null;
        }

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
        if (title != null && !title.isEmpty()) {
            queryBuilder.append(" OR b.title LIKE :title");
        }
        if (author != null && !author.isEmpty()) {
            queryBuilder.append(" OR b.author LIKE :author");
        }
        if (tag != null && !tag.isEmpty()) {
            queryBuilder.append(" OR b.tags LIKE :tag");
        }

        Query query = entityManager.createQuery(queryBuilder.toString());
        if (title != null && !title.isEmpty()) {
            query.setParameter("title", "%" + title + "%");
        }
        if (author != null && !author.isEmpty()) {
            query.setParameter("author", "%" + author + "%");
        }
        if (tag != null && !tag.isEmpty()) {
            query.setParameter("tag", "%" + tag + "%");
        }

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
