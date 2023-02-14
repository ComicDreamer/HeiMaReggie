package cuculi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cuculi.pojo.Dish;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
