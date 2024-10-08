package page.clab.api.domain.library.book.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.library.book.domain.Book;

public interface RetrieveBookPort {

    Book getById(Long bookId);

    Page<Book> findByConditions(String title, String category, String publisher, String borrowerId, String borrowerName, Pageable pageable);

    int countByBorrowerId(String borrowerId);
}
