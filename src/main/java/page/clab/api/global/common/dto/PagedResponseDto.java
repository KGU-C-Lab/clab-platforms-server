package page.clab.api.global.common.dto;

import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import page.clab.api.global.exception.InvalidColumnException;

import java.util.Comparator;
import java.util.List;

/**
 * 페이지네이션 응답을 위한 제네릭 DTO 클래스입니다. 현재 페이지, 총 아이템 수 및
 * 페이지 관련 정보와 실제 콘텐츠를 제공합니다.
 *
 * @param <T> 페이지네이션 응답의 콘텐츠 타입
 */
@Getter
public class PagedResponseDto<T> {

    /**
     * 현재 페이지 번호 (0부터 시작하는 인덱스).
     */
    private final int currentPage;

    /**
     * 이전 페이지가 있는지 여부를 나타냅니다.
     */
    private final boolean hasPrevious;

    /**
     * 다음 페이지가 있는지 여부를 나타냅니다.
     */
    private final boolean hasNext;

    /**
     * 전체 페이지 수.
     */
    private final int totalPages;

    /**
     * 전체 아이템 수.
     */
    private final long totalItems;

    /**
     * 현재 페이지의 아이템 수.
     */
    private final int take;

    /**
     * 현재 페이지의 콘텐츠 아이템 리스트.
     */
    private final List<T> items;

    /**
     * Page 객체를 사용하여 PagedResponseDto를 생성하는 생성자입니다.
     *
     * @param page 페이지네이션된 데이터를 포함하는 Page 객체
     */
    public PagedResponseDto(Page<T> page) {
        this.currentPage = page.getNumber();
        this.hasPrevious = page.hasPrevious();
        this.hasNext = page.hasNext();
        this.totalPages = page.getTotalPages();
        this.totalItems = page.getTotalElements();
        this.take = page.getNumberOfElements();
        this.items = page.getContent();
    }

    /**
     * totalItems 및 numberOfElements를 추가로 설정할 수 있는 생성자입니다.
     *
     * @param page 페이지네이션된 데이터를 포함하는 Page 객체
     * @param totalItems 전체 아이템 수
     * @param numberOfElements 현재 페이지의 아이템 수
     */
    public PagedResponseDto(Page<T> page, long totalItems, int numberOfElements) {
        this.currentPage = page.getNumber();
        this.hasPrevious = page.hasPrevious();
        this.hasNext = page.hasNext();
        this.totalPages = page.getTotalPages();
        this.totalItems = totalItems;
        this.take = numberOfElements;
        this.items = page.getContent();
    }

    /**
     * List와 Pageable 객체를 사용하여 PagedResponseDto를 생성하는 생성자입니다.
     * 페이지네이션과 정렬이 적용됩니다.
     *
     * @param ts 콘텐츠 아이템 리스트
     * @param pageable 페이지네이션 정보와 정렬 기준을 포함하는 Pageable 객체
     * @param size 전체 아이템 수
     */
    public PagedResponseDto(List<T> ts, Pageable pageable, int size) {
        this.currentPage = pageable.getPageNumber();
        this.hasPrevious = pageable.getPageNumber() > 0;
        this.hasNext = ts.size() == size;
        this.totalPages = (size != 0) ? (int) Math.ceil((double) ts.size() / pageable.getPageSize()) : 0;
        this.totalItems = ts.size();
        this.take = size;
        this.items = applySortingIfNecessary(ts, pageable.getSort());
    }

    /**
     * List와 Pageable 객체를 사용하여 PagedResponseDto를 생성하는 생성자입니다.
     * 페이지네이션과 정렬이 적용됩니다.
     *
     * @param ts 콘텐츠 아이템 리스트
     * @param pageable 페이지네이션 정보와 정렬 기준을 포함하는 Pageable 객체
     */
    public PagedResponseDto(List<T> ts, Pageable pageable) {
        this.currentPage = pageable.getPageNumber();
        this.hasPrevious = pageable.getPageNumber() > 0;
        this.hasNext = pageable.getOffset() + pageable.getPageSize() < ts.size();
        this.totalPages = (!ts.isEmpty()) ? (int) Math.ceil((double) ts.size() / pageable.getPageSize()) : 0;
        this.totalItems = ts.size();
        this.take = slicePageFromList(ts, pageable).size();
        this.items = applySortingAndSlicingIfNecessary(ts, pageable, false);
    }

    /**
     * List와 Pageable 객체를 사용하여 PagedResponseDto를 생성하는 생성자입니다.
     * ActivityGroup 도메인에서 GroupMember 정렬 시 활용 가능합니다.
     * 페이지네이션과 정렬이 적용됩니다.
     *
     * @param ts 콘텐츠 아이템 리스트
     * @param pageable 페이지네이션 정보와 정렬 기준을 포함하는 Pageable 객체
     * @param leaderUp 리더를 우선 배치할지 여부를 나타내는 플래그 (true일 경우 리더 우선 정렬 적용)
     */
    public PagedResponseDto(List<T> ts, Pageable pageable, boolean leaderUp) {
        this.currentPage = pageable.getPageNumber();
        this.hasPrevious = pageable.getPageNumber() > 0;
        this.hasNext = pageable.getOffset() + pageable.getPageSize() < ts.size();
        this.totalPages = (!ts.isEmpty()) ? (int) Math.ceil((double) ts.size() / pageable.getPageSize()) : 0;
        this.totalItems = ts.size();
        this.take = slicePageFromList(ts, pageable).size();
        this.items = applySortingAndSlicingIfNecessary(ts, pageable, leaderUp);
    }

    /**
     * 정렬이 필요한 경우 아이템 리스트에 정렬을 적용하는 메서드입니다.
     *
     * @param items 정렬되지 않은 아이템 리스트
     * @param sort 정렬 기준을 포함한 Sort 객체
     * @return 정렬된 아이템 리스트
     */
    private List<T> applySortingIfNecessary(List<T> items, Sort sort) {
        if (sort.isSorted()) {
            return sortItems(items, sort);
        }
        return items;
    }

    /**
     * 정렬 및 페이지네이션(슬라이싱)을 적용하는 메서드입니다.
     * 주어진 아이템 리스트에 대해 필요한 경우 정렬을 먼저 적용하고, 그 후 페이지네이션을 적용하여 슬라이스된 리스트를 반환합니다.
     *
     * @param items 정렬 및 페이지네이션이 적용되지 않은 아이템 리스트
     * @param pageable 페이지네이션 정보와 정렬 기준을 포함한 Pageable 객체
     * @param leaderUp 리더를 우선 배치할지 여부를 나타내는 플래그 (true일 경우 리더 우선 정렬 적용)
     * @return 정렬 및 페이지네이션이 적용된 아이템 리스트
     */
    private List<T> applySortingAndSlicingIfNecessary(List<T> items, Pageable pageable, boolean leaderUp) {
        Sort sort = pageable.getSort();
        if (sort.isSorted()) {
            items = leaderUp ? sortItemsLeaderUp(items, sort) : sortItems(items, sort);
        }
        return slicePageFromList(items, pageable);
    }

    /**
     * Sort 객체를 사용하여 아이템 리스트를 정렬하는 메서드입니다.
     *
     * @param items 정렬되지 않은 아이템 리스트
     * @param sort 정렬 기준을 포함한 Sort 객체
     * @return 정렬된 아이템 리스트
     */
    private List<T> sortItems(List<T> items, Sort sort) {
        Comparator<T> comparator = sort.stream()
                .map(order -> {
                    Comparator<T> itemComparator = Comparator.comparing(
                            item -> {
                                try {
                                    return (Comparable) extractFieldValue(item, order.getProperty());
                                } catch (InvalidColumnException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                    );
                    return order.isAscending() ? itemComparator : itemComparator.reversed();
                })
                .reduce(Comparator::thenComparing)
                .orElseThrow(IllegalArgumentException::new);

        return items.stream()
                .sorted(comparator)
                .toList();
    }

    /**
     * Sort 객체를 사용하여 아이템 리스트를 정렬하는 메서드입니다.
     * ActivityGroup의 Leader가 최상단에 위치하도록 정렬됩니다.
     *
     * @param items 정렬되지 않은 아이템 리스트
     * @param sort 정렬 기준을 포함한 Sort 객체
     * @return 정렬된 아이템 리스트
     */
    private List<T> sortItemsLeaderUp(List<T> items, Sort sort) {
        Comparator<T> leaderComparator = Comparator.comparing(item -> {
            try {
                return "LEADER".equals(extractFieldValue(item, "role")) ? 0 : 1;
            } catch (InvalidColumnException e) {
                throw new RuntimeException(e);
            }
        });

        Comparator<T> comparator = sort.stream()
                .map(order -> {
                    Comparator<T> itemComparator = Comparator.comparing(
                            item -> {
                                try {
                                    return (Comparable) extractFieldValue(item, order.getProperty());
                                } catch (InvalidColumnException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                    );
                    return order.isAscending() ? itemComparator : itemComparator.reversed();
                })
                .reduce(Comparator::thenComparing)
                .orElseThrow(IllegalArgumentException::new);

        Comparator<T> finalComparator = leaderComparator.thenComparing(comparator);

        return items.stream()
                .sorted(finalComparator)
                .toList();
    }

    /**
     * 주어진 아이템 리스트에 슬라이싱을 적용하여 해당 페이지의 아이템을 반환하는 메서드입니다.
     *
     * @param items 슬라이싱이 적용되지 않은 전체 아이템 리스트
     * @param pageable 페이지네이션 정보와 페이지 크기를 포함한 Pageable 객체
     * @return 해당 페이지에 속하는 아이템 리스트
     */
    private List<T> slicePageFromList(List<T> items, Pageable pageable) {
        return items.stream()
                .skip(pageable.getOffset())
                .limit(pageable.getPageSize())
                .toList();
    }

    /**
     * 리플렉션을 사용하여 객체의 특정 필드 값을 추출하는 메서드입니다.
     *
     * @param item 값을 추출할 객체
     * @param fieldName 추출할 필드 이름
     * @return 추출된 필드 값
     */
    private Object extractFieldValue(T item, String fieldName) throws InvalidColumnException {
        try {
            var field = item.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(item);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new InvalidColumnException("잘못된 필드 이름: " + fieldName);
        }
    }
}
