package cuculi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cuculi.pojo.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee>{
}
