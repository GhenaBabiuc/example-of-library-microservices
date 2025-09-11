package org.example.booksservice.mapper;

import org.example.booksservice.model.Author;
import org.example.booksservice.model.dto.AuthorRecord;
import org.example.booksservice.model.dto.CreateAuthorRequest;
import org.example.booksservice.model.dto.UpdateAuthorRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    AuthorRecord toRecord(Author author);

    @Mapping(target = "id", ignore = true)
    Author toEntity(CreateAuthorRequest request);

    @Mapping(target = "id", ignore = true)
    void updateEntity(UpdateAuthorRequest request, @MappingTarget Author author);
}
