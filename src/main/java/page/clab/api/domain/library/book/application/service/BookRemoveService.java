package page.clab.api.domain.library.book.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.library.book.application.port.in.RemoveBookUseCase;
import page.clab.api.domain.library.book.application.port.out.RegisterBookPort;
import page.clab.api.domain.library.book.application.port.out.RetrieveBookPort;
import page.clab.api.domain.library.book.domain.Book;

@Service
@RequiredArgsConstructor
public class BookRemoveService implements RemoveBookUseCase {

    private final RetrieveBookPort retrieveBookPort;
    private final RegisterBookPort registerBookPort;

    @Transactional
    @Override
    public Long removeBook(Long bookId) {
        Book book = retrieveBookPort.getById(bookId);
        book.delete();
        return registerBookPort.save(book).getId();
    }
}
