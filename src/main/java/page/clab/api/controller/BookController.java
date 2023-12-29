package page.clab.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.service.BookService;
import page.clab.api.type.dto.BookRequestDto;
import page.clab.api.type.dto.BookResponseDto;
import page.clab.api.type.dto.PagedResponseDto;
import page.clab.api.type.dto.ResponseModel;

import javax.validation.Valid;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
@Tag(name = "Book", description = "도서 관련 API")
@Slf4j
public class BookController {

    private final BookService bookService;

    @Operation(summary = "[A] 도서 등록", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @PostMapping("")
    public ResponseModel createBook(
            @Valid @RequestBody BookRequestDto bookRequestDto,
            BindingResult result
    ) throws MethodArgumentNotValidException {
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException(null, result);
        }
        Long id = bookService.createBook(bookRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

    @Operation(summary = "[U] 도서 목록", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("")
    public ResponseModel getBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<BookResponseDto> books = bookService.getBooks(pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(books);
        return responseModel;
    }

    @Operation(summary = "[U] 도서 검색", description = "ROLE_USER 이상의 권한이 필요함<br>" +
            "카테고리, 제목, 저자, 출판사, 대여자 ID를 기준으로 검색")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("/search")
    public ResponseModel searchBook(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<BookResponseDto> books = bookService.searchBook(keyword, pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(books);
        return responseModel;
    }

    @Operation(summary = "[A] 도서 정보 수정", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @PatchMapping("")
    public ResponseModel updateBookInfo(
            @RequestParam Long bookId,
            @Valid @RequestBody BookRequestDto bookRequestDto,
            BindingResult result
    ) throws MethodArgumentNotValidException {
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException(null, result);
        }
        Long id = bookService.updateBookInfo(bookId, bookRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

    @Operation(summary = "[A] 도서 삭제", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @DeleteMapping("/{bookId}")
    public ResponseModel deleteBook(
            @PathVariable("bookId") Long bookId
    ) {
        Long id = bookService.deleteBook(bookId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

}
