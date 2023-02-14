package cuculi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cuculi.dto.SetmealDto;
import cuculi.mapper.SetmealMapper;
import cuculi.pojo.Setmeal;
import cuculi.pojo.SetmealDish;
import cuculi.service.SetmealDishService;
import cuculi.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

    @Override
    public void addSetmeal(SetmealDto setmealDto){
        this.save(setmealDto);
        List<SetmealDish>  dishes = setmealDto.getSetmealDishes();
        dishes = dishes.stream().map((item)->{
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(dishes);
    }

    @Override
    public void deleteSetmeal(Long ids){
        this.removeById(ids);
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, ids);
        setmealDishService.remove(queryWrapper);
    }
}
