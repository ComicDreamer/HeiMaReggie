package cuculi.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cuculi.common.R;
import cuculi.pojo.Category;
import cuculi.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    public CategoryService categoryService;

    /**
     * 添加分类
     * @param category
     * @return
     */
    @PostMapping
    public R<String> addCategory(@RequestBody Category category){
        categoryService.save(category);
        return R.success("添加分类成功");
    }

    /**
     * 显示分类列表
     */
    @GetMapping("/page")
    public R<Page> pageQuery(int page, int pageSize){
        Page pageInfo = new Page(page, pageSize);

        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Category::getSort);
        categoryService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

    /**
     * 删除分类
     * @return
     */
    @DeleteMapping
    public R<String> delete(Long ids){
        log.info(ids.toString());
        categoryService.remove(ids);
        return R.success("删除成功");
    }

    /**
     * 查询列表
     */
    @GetMapping("/list")
    public R<List<Category>> list(Category category){
        log.info(category.toString());
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(category.getType() != null,Category::getType, category.getType());
        queryWrapper.orderByDesc(Category::getSort).orderByAsc(Category::getCreateTime);
        List<Category> list = categoryService.list(queryWrapper);
        return R.success(list);
    }
}
