package cuculi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cuculi.common.CustomException;
import cuculi.mapper.CategoryMapper;
import cuculi.pojo.Category;
import cuculi.pojo.Dish;
import cuculi.pojo.Setmeal;
import cuculi.service.CategoryService;
import cuculi.service.DishService;
import cuculi.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService{

    @Autowired
    public DishService dishService;
    @Autowired
    public SetmealService setmealService;

    /**
     * 删除分类
     * @param id
     * @return
     */
    @Override
    public void remove(Long id){
        //查询是否有菜品在分类中
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);
        int count = dishService.count(dishLambdaQueryWrapper);
        if (count > 0){
            throw new CustomException("有菜品在分类中，无法删除");
        }

        //查询是否有套餐在分类中
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
        int count2 = setmealService.count(setmealLambdaQueryWrapper);
        if (count2 > 0){
            throw new CustomException("有套餐在分类中，无法删除");
        }
        return;
    }
}
