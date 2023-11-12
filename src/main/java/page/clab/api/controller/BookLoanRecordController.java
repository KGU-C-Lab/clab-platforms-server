package page.clab.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.service.BookLoanRecordService;
import page.clab.api.type.dto.BookLoanRecordRequestDto;
import page.clab.api.type.dto.BookLoanRecordResponseDto;
import page.clab.api.type.dto.ResponseModel;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/book-loan-records")
@RequiredArgsConstructor
@Tag(name = "BookLoanRecord", description = "도서 대출 관련 API")
@Slf4j
public class BookLoanRecordController {

    private final BookLoanRecordService bookLoanRecordService;

    @Operation(summary = "[U] 도서 대출", description = "ROLE_USER 이상의 권한이 필요함")
    @PostMapping("/borrow")
    public ResponseModel borrowBook(
            @Valid @RequestBody BookLoanRecordRequestDto bookLoanRecordRequestDto,
            BindingResult result
    ) throws MethodArgumentNotValidException {
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException(null, result);
        }
        bookLoanRecordService.borrowBook(bookLoanRecordRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "[U] 도서 반납", description = "ROLE_USER 이상의 권한이 필요함")
    @PostMapping("/return")
    public ResponseModel returnBook(
            @RequestBody BookLoanRecordRequestDto bookLoanRecordRequestDto
    ) {
        bookLoanRecordService.returnBook(bookLoanRecordRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "[U] 도서 대출 연장", description = "ROLE_USER 이상의 권한이 필요함")
    @PostMapping("/extend")
    public ResponseModel extendBookLoan(
            @RequestBody BookLoanRecordRequestDto bookLoanRecordRequestDto
    ) {
        bookLoanRecordService.extendBookLoan(bookLoanRecordRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "[U] 도서 대출 내역 조회", description = "ROLE_USER 이상의 권한이 필요함")
    @GetMapping("")
    public ResponseModel getBookLoanRecords() {
        List<BookLoanRecordResponseDto> bookLoanRecords = bookLoanRecordService.getBookLoanRecords();
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(bookLoanRecords);
        return responseModel;
    }

    @Operation(summary = "[U] 도서 대출 내역 검색", description = "ROLE_USER 이상의 권한이 필요함<br>" +
            "도서 ID, 대출자 ID를 기준으로 검색")
    @GetMapping("/search")
    public ResponseModel searchBookLoanRecord(
            @RequestParam(required = false) Long bookId,
            @RequestParam(required = false) String borrowerId
    ) {
        List<BookLoanRecordResponseDto> bookLoanRecords = bookLoanRecordService.searchBookLoanRecord(bookId, borrowerId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(bookLoanRecords);
        return responseModel;
    }

    @Operation(summary = "[U] 대출 상태의 도서 조회", description = "ROLE_USER 이상의 권한이 필요함")
    @GetMapping("/unreturned")
    public ResponseModel getUnreturnedBooks() {
        List<BookLoanRecordResponseDto> unreturnedBooks = bookLoanRecordService.getUnreturnedBooks();
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(unreturnedBooks);
        return responseModel;
    }

}
