package page.clab.api.domain.library.bookLoanRecord.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.library.book.domain.Book;
import page.clab.api.domain.library.bookLoanRecord.application.dto.request.BookLoanRecordRequestDto;
import page.clab.api.domain.library.bookLoanRecord.application.exception.BookAlreadyAppliedForLoanException;
import page.clab.api.domain.library.bookLoanRecord.application.exception.MaxBorrowLimitExceededException;
import page.clab.api.domain.library.bookLoanRecord.application.port.in.RequestBookLoanUseCase;
import page.clab.api.domain.library.bookLoanRecord.application.port.out.RegisterBookLoanRecordPort;
import page.clab.api.domain.library.bookLoanRecord.application.port.out.RetrieveBookLoanRecordPort;
import page.clab.api.domain.library.bookLoanRecord.domain.BookLoanRecord;
import page.clab.api.domain.library.bookLoanRecord.domain.BookLoanStatus;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberBorrowerInfoDto;
import page.clab.api.external.library.book.application.port.ExternalRetrieveBookUseCase;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;
import page.clab.api.external.memberManagement.notification.application.port.ExternalSendNotificationUseCase;
import page.clab.api.global.common.slack.application.SlackService;
import page.clab.api.global.common.slack.domain.SlackBookLoanRecordInfo;
import page.clab.api.global.exception.CustomOptimisticLockingFailureException;

@Service
@RequiredArgsConstructor
public class BookLoanRequestService implements RequestBookLoanUseCase {

    private final RetrieveBookLoanRecordPort retrieveBookLoanRecordPort;
    private final RegisterBookLoanRecordPort registerBookLoanRecordPort;
    private final ExternalRetrieveBookUseCase externalRetrieveBookUseCase;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;
    private final ExternalSendNotificationUseCase externalSendNotificationUseCase;
    private final SlackService slackService;

    @Transactional
    @Override
    public Long requestBookLoan(BookLoanRecordRequestDto requestDto) throws CustomOptimisticLockingFailureException {
        try {
            MemberBorrowerInfoDto borrowerInfo = externalRetrieveMemberUseCase.getCurrentMemberBorrowerInfo();

            borrowerInfo.checkLoanSuspension();
            validateBorrowLimit(borrowerInfo.getMemberId());

            Book book = externalRetrieveBookUseCase.getById(requestDto.getBookId());
            checkIfLoanAlreadyApplied(book.getId(), borrowerInfo.getMemberId());

            BookLoanRecord bookLoanRecord = BookLoanRecord.create(book.getId(), borrowerInfo);

            externalSendNotificationUseCase.sendNotificationToMember(borrowerInfo.getMemberId(), "[" + book.getTitle() + "] 도서 대출 신청이 완료되었습니다.");

            SlackBookLoanRecordInfo bookLoanRecordInfo = SlackBookLoanRecordInfo.create(book, borrowerInfo);
            slackService.sendNewBookLoanRequestNotification(bookLoanRecordInfo);

            return registerBookLoanRecordPort.save(bookLoanRecord).getId();
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new CustomOptimisticLockingFailureException("도서 대출 신청에 실패했습니다. 다시 시도해주세요.");
        }
    }

    private void validateBorrowLimit(String borrowerId) {
        int borrowedBookCount = externalRetrieveBookUseCase.countByBorrowerId(borrowerId);
        int maxBorrowableBookCount = 3;
        if (borrowedBookCount >= maxBorrowableBookCount) {
            throw new MaxBorrowLimitExceededException("대출 가능한 도서의 수를 초과했습니다.");
        }
    }

    private void checkIfLoanAlreadyApplied(Long bookId, String borrowerId) {
        retrieveBookLoanRecordPort.findByBookIdAndBorrowerIdAndStatus(bookId, borrowerId, BookLoanStatus.PENDING)
                .ifPresent(bookLoanRecord -> {
                    throw new BookAlreadyAppliedForLoanException("이미 대출 신청한 도서입니다.");
                });
    }
}
