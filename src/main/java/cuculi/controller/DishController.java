package cuculi.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cuculi.common.R;
import cuculi.dto.DishDto;
import cuculi.pojo.Dish;
import cuculi.service.CategoryService;
import cuculi.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 新建dish
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        dishService.saveDishDto(dishDto);
        return R.success("保存成功");
    }

    /**
     * 显示dish列表
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name), Dish::getName, name);
        queryWrapper.orderByDesc(Dish::getCreateTime);
        dishService.page(pageInfo, queryWrapper);
        //现在page的records中已经有了除 分类名 外的其他信息
        Page<DishDto> dtoPage = new Page<>(page, pageSize);
        BeanUtils.copyProperties(pageInfo, dtoPage, "records");
        List<Dish> records = pageInfo.getRecords();
        List<DishDto> dtoRecords = records.stream().map((item)->{
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId();
            String categoryName = categoryService.getById(categoryId).getName();
            dishDto.setCategoryName(categoryName);
            return dishDto;
        }).collect(Collectors.toList());
        dtoPage.setRecords(dtoRecords);
        return R.success(dtoPage);
    }

    /**
     * 按id查询dish
     */
    @GetMapping("/{id}")
    public R<Dish> selectById(@PathVariable Long id){
        DishDto dishDto = dishService.selectByIdWithFlavor(id);
        return R.success(dishDto);
    }

    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        dishService.updateWithFlavor(dishDto);
        return R.success("更新成功");
    }


//    @GetMapping("/list")
//    public R<List<Dish>> querryCategory(Long categoryId){
//        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(Dish::getCategoryId, categoryId);
//        queryWrapper.eq(Dish::getStatus, 1);
//        List<Dish> dishes = dishService.list(queryWrapper);
//        return R.success(dishes);
//    }
    @GetMapping("/list")
    public R<List<DishDto>> querryCategory(Dish dish){
        List<DishDto> dishDtos = dishService.querryCategory(dish);
        return R.success(dishDtos);
    }
}
