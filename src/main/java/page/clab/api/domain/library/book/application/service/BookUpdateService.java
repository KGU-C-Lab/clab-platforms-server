package page.clab.api.domain.library.book.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.library.book.application.dto.request.BookUpdateRequestDto;
import page.clab.api.domain.library.book.application.port.in.UpdateBookUseCase;
import page.clab.api.domain.library.book.application.port.out.RegisterBookPort;
import page.clab.api.domain.library.book.application.port.out.RetrieveBookPort;
import page.clab.api.domain.library.book.domain.Book;

@Service
@RequiredArgsConstructor
public class BookUpdateService implements UpdateBookUseCase {

    private final RetrieveBookPort retrieveBookPort;
    private final RegisterBookPort registerBookPort;

    @Transactional
    @Override
    public Long updateBookInfo(Long bookId, BookUpdateRequestDto requestDto) {
        Book book = retrieveBookPort.getById(bookId);
        book.update(requestDto);
        return registerBookPort.save(book).getId();
    }
}
