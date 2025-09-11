package org.example.borrowingservice.mapper;

import org.example.borrowingservice.model.Borrowing;
import org.example.borrowingservice.model.dto.BorrowingRecord;
import org.example.borrowingservice.model.dto.CreateBorrowingRequest;
import org.example.borrowingservice.model.dto.UpdateBorrowingRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BorrowingMapper {

    BorrowingRecord toRecord(Borrowing borrowing);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "borrowDate", ignore = true)
    @Mapping(target = "returnDate", ignore = true)
    @Mapping(target = "status", ignore = true)
    Borrowing toEntity(CreateBorrowingRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "bookId", ignore = true)
    @Mapping(target = "borrowDate", ignore = true)
    void updateEntity(UpdateBorrowingRequest request, @MappingTarget Borrowing borrowing);
}
