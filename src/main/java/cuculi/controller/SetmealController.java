package cuculi.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cuculi.common.R;
import cuculi.dto.SetmealDto;
import cuculi.pojo.Setmeal;
import cuculi.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @GetMapping("/page")
    public R<Page> pageList(int page, int pageSize, String name){
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotEmpty(name), Setmeal::getName, name);
        setmealService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

    @PostMapping
    @CacheEvict(value = "setmealCache", allEntries = true)
    public R<String> addSetmeal(@RequestBody SetmealDto setmealDto){
        setmealService.addSetmeal(setmealDto);
        return R.success("添加套餐成功");
    }

    @DeleteMapping
    @CacheEvict(value = "setmealCache", allEntries = true)
    public R<String> delete(Long ids){
        setmealService.deleteSetmeal(ids);
        return R.success("删除成功");
    }

    @GetMapping("/list")
    @Cacheable(value = "setmealCache", key = "#setmeal.categoryId")
    public R<List<Setmeal>> list(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Setmeal::getCategoryId,setmeal.getCategoryId());
        List<Setmeal> setmeals = setmealService.list(queryWrapper);
        return R.success(setmeals);
    }
}
