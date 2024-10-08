package page.clab.api.external.library.book.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.library.book.application.port.out.RetrieveBookPort;
import page.clab.api.domain.library.book.domain.Book;
import page.clab.api.external.library.book.application.port.ExternalRetrieveBookUseCase;

@Service
@RequiredArgsConstructor
public class ExternalBookRetrievalService implements ExternalRetrieveBookUseCase {

    private final RetrieveBookPort retrieveBookPort;

    @Transactional(readOnly = true)
    @Override
    public Book getById(Long bookId) {
        return retrieveBookPort.getById(bookId);
    }

    @Transactional(readOnly = true)
    @Override
    public int countByBorrowerId(String borrowerId) {
        return retrieveBookPort.countByBorrowerId(borrowerId);
    }
}
