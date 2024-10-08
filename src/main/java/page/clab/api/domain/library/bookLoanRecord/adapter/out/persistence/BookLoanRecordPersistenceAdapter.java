package page.clab.api.domain.library.bookLoanRecord.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import page.clab.api.domain.library.bookLoanRecord.application.dto.response.BookLoanRecordOverdueResponseDto;
import page.clab.api.domain.library.bookLoanRecord.application.dto.response.BookLoanRecordResponseDto;
import page.clab.api.domain.library.bookLoanRecord.application.port.out.RegisterBookLoanRecordPort;
import page.clab.api.domain.library.bookLoanRecord.application.port.out.RetrieveBookLoanRecordPort;
import page.clab.api.domain.library.bookLoanRecord.domain.BookLoanRecord;
import page.clab.api.domain.library.bookLoanRecord.domain.BookLoanStatus;
import page.clab.api.global.exception.NotFoundException;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BookLoanRecordPersistenceAdapter implements
        RegisterBookLoanRecordPort,
        RetrieveBookLoanRecordPort {

    private final BookLoanRecordRepository bookLoanRecordRepository;
    private final BookLoanRecordMapper bookLoanRecordMapper;

    @Override
    public BookLoanRecord save(BookLoanRecord bookLoanRecord) {
        BookLoanRecordJpaEntity entity = bookLoanRecordMapper.toEntity(bookLoanRecord);
        BookLoanRecordJpaEntity savedEntity = bookLoanRecordRepository.save(entity);
        return bookLoanRecordMapper.toDomain(savedEntity);
    }

    @Override
    public BookLoanRecord getById(Long bookLoanRecordId) {
        return bookLoanRecordRepository.findById(bookLoanRecordId)
                .map(bookLoanRecordMapper::toDomain)
                .orElseThrow(() -> new NotFoundException("[BookLoanRecord] id: " + bookLoanRecordId + "에 해당하는 대출 기록이 존재하지 않습니다."));
    }

    @Override
    public Page<BookLoanRecordResponseDto> findByConditions(Long bookId, String borrowerId, BookLoanStatus status, Pageable pageable) {
        return bookLoanRecordRepository.findByConditions(bookId, borrowerId, status, pageable);
    }

    @Override
    public Page<BookLoanRecordOverdueResponseDto> findOverdueBookLoanRecords(Pageable pageable) {
        return bookLoanRecordRepository.findOverdueBookLoanRecords(pageable);
    }

    @Override
    public Optional<BookLoanRecord> findByBookIdAndReturnedAtIsNullAndStatus(Long bookId, BookLoanStatus bookLoanStatus) {
        return bookLoanRecordRepository.findByBookIdAndReturnedAtIsNullAndStatus(bookId, bookLoanStatus)
                .map(bookLoanRecordMapper::toDomain);
    }

    @Override
    public BookLoanRecord getByBookIdAndReturnedAtIsNullAndStatus(Long bookId, BookLoanStatus bookLoanStatus) {
        return bookLoanRecordRepository.findByBookIdAndReturnedAtIsNullAndStatus(bookId, bookLoanStatus)
                .map(bookLoanRecordMapper::toDomain)
                .orElseThrow(() -> new NotFoundException("[Book] id: " + bookId + "에 해당하는 대출 기록이 존재하지 않습니다."));
    }

    @Override
    public Optional<BookLoanRecord> findByBookIdAndBorrowerIdAndStatus(Long bookId, String borrowerId, BookLoanStatus bookLoanStatus) {
        return bookLoanRecordRepository.findByBookIdAndBorrowerIdAndStatus(bookId, borrowerId, bookLoanStatus)
                .map(bookLoanRecordMapper::toDomain);
    }
}
