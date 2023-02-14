package cuculi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cuculi.mapper.OrderDetailMapper;
import cuculi.pojo.OrderDetail;
import cuculi.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {

}