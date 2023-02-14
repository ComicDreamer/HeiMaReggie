package cuculi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cuculi.dto.SetmealDto;
import cuculi.pojo.Setmeal;

public interface SetmealService extends IService<Setmeal> {
    void addSetmeal(SetmealDto setmealDto);

    void deleteSetmeal(Long ids);
}
