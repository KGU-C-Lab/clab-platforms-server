package page.clab.api.domain.library.book.application.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.library.book.application.dto.response.BookResponseDto;
import page.clab.api.domain.library.book.application.port.in.RetrieveBooksByConditionsUseCase;
import page.clab.api.domain.library.book.application.port.out.RetrieveBookPort;
import page.clab.api.domain.library.book.domain.Book;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberBasicInfoDto;
import page.clab.api.external.library.bookLoanRecord.application.port.ExternalRetrieveBookLoanRecordUseCase;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;
import page.clab.api.global.common.dto.PagedResponseDto;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BooksByConditionsRetrievalService implements RetrieveBooksByConditionsUseCase {

    private final RetrieveBookPort retrieveBookPort;
    private final ExternalRetrieveBookLoanRecordUseCase externalRetrieveBookLoanRecordUseCase;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<BookResponseDto> retrieveBooks(String title, String category, String publisher, String borrowerId, String borrowerName, Pageable pageable) {
        Page<Book> books = retrieveBookPort.findByConditions(title, category, publisher, borrowerId, borrowerName, pageable);
        return new PagedResponseDto<>(books.map(this::mapToBookResponseDto));
    }

    @NotNull
    private BookResponseDto mapToBookResponseDto(Book book) {
        String borrowerId = externalRetrieveBookLoanRecordUseCase.getBorrowerIdForBook(book.getId());
        MemberBasicInfoDto memberBasicInfo = externalRetrieveMemberUseCase.getMemberBasicInfoById(borrowerId);
        LocalDateTime dueDate = externalRetrieveBookLoanRecordUseCase.getDueDateForBook(book.getId());
        return BookResponseDto.toDto(book, memberBasicInfo.getMemberName(), dueDate);
    }
}
