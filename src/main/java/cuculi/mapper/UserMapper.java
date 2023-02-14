package cuculi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cuculi.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
