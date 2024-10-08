package page.clab.api.domain.library.bookLoanRecord.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.library.bookLoanRecord.application.dto.response.BookLoanRecordOverdueResponseDto;
import page.clab.api.domain.library.bookLoanRecord.application.dto.response.BookLoanRecordResponseDto;
import page.clab.api.domain.library.bookLoanRecord.domain.BookLoanRecord;
import page.clab.api.domain.library.bookLoanRecord.domain.BookLoanStatus;

import java.util.Optional;

public interface RetrieveBookLoanRecordPort {

    BookLoanRecord getById(Long bookLoanRecordId);

    Page<BookLoanRecordResponseDto> findByConditions(Long bookId, String borrowerId, BookLoanStatus status, Pageable pageable);

    Page<BookLoanRecordOverdueResponseDto> findOverdueBookLoanRecords(Pageable pageable);

    Optional<BookLoanRecord> findByBookIdAndReturnedAtIsNullAndStatus(Long bookId, BookLoanStatus bookLoanStatus);

    BookLoanRecord getByBookIdAndReturnedAtIsNullAndStatus(Long bookId, BookLoanStatus bookLoanStatus);

    Optional<BookLoanRecord> findByBookIdAndBorrowerIdAndStatus(Long bookId, String borrowerId, BookLoanStatus bookLoanStatus);
}
