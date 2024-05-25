package page.clab.api.global.util;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.PathBuilder;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderSpecifierUtil {

    public static OrderSpecifier<?>[] getOrderSpecifiers(Pageable pageable, EntityPathBase<?> q) {
        List<OrderSpecifier<?>> orderSpecifierList = new ArrayList<>();

        pageable.getSort().stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String property = order.getProperty();
            log.info(direction + property);
            PathBuilder path = new PathBuilder(q.getType(), q.getMetadata());
            log.info(path.get(property).toString());
            orderSpecifierList.add(new OrderSpecifier(direction, path.get(property)));
        });
        return orderSpecifierList.toArray(new OrderSpecifier[0]);
    }

}
