package org.v2com.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.v2com.Enums.BookStatus;
import org.v2com.entity.BookEntity;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {

    private UUID id;

    @NotBlank(message = "O autor é obrigatório")
    private String title;

    @NotBlank(message = "O título é obrigatório")
    private String author;

    @NotBlank(message = "Uma descrição é obrigatória")
    private String description;

    private String genre;
    private String isbn;
    private BookStatus status;
    private String publisher;
    private String tags;
    private String coverUrl;

    public static BookDTO fromEntity(BookEntity bookEntity) {
        return new BookDTO(
                bookEntity.getId(),
                bookEntity.getTitle(),
                bookEntity.getAuthor(),
                bookEntity.getDescription(),
                bookEntity.getGenre(),
                bookEntity.getIsbn(),
                bookEntity.getStatus(),
                bookEntity.getPublisher(),
                bookEntity.getTags(),
                bookEntity.getCoverUrl()
        );
    }

    public BookEntity toEntity() {
        BookEntity bookEntity = new BookEntity();
        bookEntity.id = this.id != null ? this.id : UUID.randomUUID(); // Garante um UUID válido
        bookEntity.title = this.title;
        bookEntity.author = this.author;
        bookEntity.description = this.description;
        bookEntity.genre = this.genre;
        bookEntity.isbn = this.isbn;
        bookEntity.status = this.status;
        bookEntity.publisher = this.publisher;
        bookEntity.tags = this.tags;
        bookEntity.coverUrl = this.coverUrl;
        return bookEntity;
    }
}
