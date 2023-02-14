package cuculi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cuculi.pojo.Category;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
