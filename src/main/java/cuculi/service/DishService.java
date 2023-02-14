package cuculi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cuculi.dto.DishDto;
import cuculi.pojo.Dish;

import java.util.List;

public interface DishService extends IService<Dish> {
    public void saveDishDto(DishDto dishDto);

    public DishDto selectByIdWithFlavor(Long id);

    void updateWithFlavor(DishDto dishDto);

    List<DishDto> querryCategory(Dish dish);
}
