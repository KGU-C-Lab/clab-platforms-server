package page.clab.api.external.library.book.application.port;

import page.clab.api.domain.library.book.domain.Book;

public interface ExternalRetrieveBookUseCase {

    Book getById(Long bookId);

    int countByBorrowerId(String borrowerId);
}
