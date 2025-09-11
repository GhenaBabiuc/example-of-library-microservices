package org.example.booksservice.mapper;

import org.example.booksservice.model.Author;
import org.example.booksservice.model.Book;
import org.example.booksservice.model.dto.BookRecord;
import org.example.booksservice.model.dto.CreateBookRequest;
import org.example.booksservice.model.dto.UpdateBookRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface BookMapper {

    @Mapping(target = "authorIds", expression = "java(mapAuthorsToIds(book.getAuthors()))")
    BookRecord toRecord(Book book);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "authors", ignore = true)
    Book toEntity(CreateBookRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "authors", ignore = true)
    void updateEntity(UpdateBookRequest request, @MappingTarget Book book);

    default Set<Long> mapAuthorsToIds(Set<Author> authors) {
        if (authors == null) {
            return null;
        }
        return authors.stream()
                .map(Author::getId)
                .collect(Collectors.toSet());
    }
}
