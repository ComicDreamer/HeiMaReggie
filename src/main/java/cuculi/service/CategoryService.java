package cuculi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cuculi.pojo.Category;

public interface CategoryService extends IService<Category> {
    void remove(Long id);
}
