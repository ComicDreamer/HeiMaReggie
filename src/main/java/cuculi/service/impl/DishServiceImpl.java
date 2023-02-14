package cuculi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cuculi.common.R;
import cuculi.dto.DishDto;
import cuculi.mapper.DishMapper;
import cuculi.pojo.Dish;
import cuculi.pojo.DishFlavor;
import cuculi.service.DishFlavorService;
import cuculi.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    @Transactional
    public void saveDishDto(DishDto dishDto) {
        String key = "dish_" + dishDto.getCategoryId();
        redisTemplate.delete(key);
        //dish保存完成
        this.save(dishDto);
        //保存dishflavor
        Long dishId = dishDto.getId();
        List<DishFlavor> dishFlavors = dishDto.getFlavors();
        dishFlavors = dishFlavors.stream().map((item) -> {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(dishFlavors);
    }

    @Override
    public DishDto selectByIdWithFlavor(Long id) {
        Dish dish = this.getById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dish.getId());
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(flavors);
        return dishDto;
    }

    @Override
    public void updateWithFlavor(DishDto dishDto){
        String key = "dish_" + dishDto.getCategoryId();
        redisTemplate.delete(key);

        this.updateById(dishDto);

        //删除原来的口味数据
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dishDto.getId());
        dishFlavorService.remove(queryWrapper);

        //添加新数据
        List<DishFlavor> flavors = dishDto.getFlavors();
        //添加dishId
        flavors = flavors.stream().map((item)->{
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);
    }

    @Override
    public List<DishDto> querryCategory(Dish dish){
        List<DishDto> dishDtos = null;
        //获取key
        String key = "dish_" + dish.getCategoryId();
        //根据key查询缓存，如果有直接返回
        dishDtos = (List<DishDto>) redisTemplate.opsForValue().get(key);
        if (dishDtos != null){
            return dishDtos;
        }

        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getCategoryId, dish.getCategoryId());
        queryWrapper.eq(Dish::getStatus, 1);
        List<Dish> dishes = this.list(queryWrapper);



        queryWrapper.clear();

        dishDtos = dishes.stream().map((item)->{
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            dishDto.setFlavors(this.selectByIdWithFlavor(item.getId()).getFlavors());
            return dishDto;
        }).collect(Collectors.toList());

        redisTemplate.opsForValue().set(key, dishDtos, 60, TimeUnit.MINUTES);
        return dishDtos;
    }
}
