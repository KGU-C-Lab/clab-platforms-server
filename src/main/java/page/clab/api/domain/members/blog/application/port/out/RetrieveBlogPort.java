package page.clab.api.domain.members.blog.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.members.blog.domain.Blog;

public interface RetrieveBlogPort {

    Blog getById(Long blogId);

    Page<Blog> findByConditions(String title, String memberName, Pageable pageable);

    Page<Blog> findAllByIsDeletedTrue(Pageable pageable);
}
