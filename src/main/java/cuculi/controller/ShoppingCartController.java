package cuculi.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import cuculi.common.BaseContext;
import cuculi.common.R;
import cuculi.pojo.ShoppingCart;
import cuculi.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        List<ShoppingCart> shoppingCarts = shoppingCartService.list(queryWrapper);
        return R.success(shoppingCarts);
    }

    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        //设置用户id
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        //判断是dish还是setmeal
        if (shoppingCart.getDishId() == null){
            //是setmeal
            queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }else{
            //是dish
            queryWrapper.eq(ShoppingCart::getDishId, shoppingCart.getDishId());
        }
        ShoppingCart one = shoppingCartService.getOne(queryWrapper);
        //在购物车中，则数量加1
        if (one != null){
            int number = one.getNumber();
            one.setNumber(number + 1);
            shoppingCartService.updateById(one);
        }else {
            //不在购物车中，则添加
            shoppingCart.setNumber(1);
            shoppingCartService.save(shoppingCart);
            one = shoppingCart;
        }


        return R.success(one);
    }

    @DeleteMapping("/clean")
    public R<String> clean(){
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        shoppingCartService.remove(queryWrapper);
        return R.success("删除成功");
    }
}
