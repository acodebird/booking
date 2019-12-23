package com.booking.web;

import com.booking.domain.Comment;
import com.booking.domain.Order;
import com.booking.domain.User;
import com.booking.dto.UserQueryDTO;
import com.booking.service.CommentService;
import com.booking.service.OrderService;
import com.booking.service.UserService;
import com.booking.utils.STablePageRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import com.booking.utils.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    OrderService orderService;
    @Autowired
    CommentService commentService;

    // 根据用户 id 获取用户信息
    @GetMapping("/{uid}")
    public User findById (@PathVariable("uid") Long uid) {
        System.out.println("findById");
        return userService.getUserById(uid);
    }

    // 增加用户
    @PostMapping
    public ResponseEntity<String> save (@RequestBody User user) {
        System.out.println("save");
        if(null==user){
            return ResponseEntity.ofFailed().status(HttpStatus.BAD_REQUEST).data("parameter_error");
        }
        userService.save(user);
        return ResponseEntity.ofSuccess().status(HttpStatus.OK).data("success");
    }

    // 更新用户
    @PutMapping(value="{uid}")
    public ResponseEntity<String> update (@PathVariable("uid") Long uid, @RequestBody User user) {
        System.out.println("update");
        if(null==uid||null==user){
            return ResponseEntity.ofFailed().status(HttpStatus.BAD_REQUEST).data("parameter_error");
        }
        User target = userService.getUserById(uid);
        if(target==null) {
            return ResponseEntity.ofFailed().status(HttpStatus.BAD_REQUEST).data("user_not_existed");
        }
        BeanUtils.copyProperties(user, target);
        userService.save(target);
        return ResponseEntity.ofSuccess().status(HttpStatus.OK).data("Update User Success.");
    }

    // 批量更新用户
    @PutMapping
    public ResponseEntity<String> updateBatch (@RequestBody List<User> users) {
        System.out.println("updateBatch");
        if(users==null) {
            return ResponseEntity.ofFailed().status(HttpStatus.BAD_REQUEST).data("parameter_error");
        }
        userService.saveAll(users);
        return ResponseEntity.ofSuccess().status(HttpStatus.OK).data("Update User Success.");
    }

    // 删除用户
    @DeleteMapping(value = "{uid}")
    public ResponseEntity<String> delete(@PathVariable("uid") Long uid) {
        System.out.println("delete uid="+uid);
        if (uid == null) {
            return ResponseEntity.ofFailed().status(HttpStatus.BAD_REQUEST).data("parameter_error");
        }
        userService.deleteById(uid);
        return ResponseEntity.ofSuccess().status(HttpStatus.OK).data("Delete Order Success.");
    }

    // 批量删除用户
    @DeleteMapping
    public ResponseEntity<String> deleteBatch(@RequestBody List<Long> uids) {
        System.out.println("deleteBatch");
        if (uids == null) {
            return ResponseEntity.ofFailed().status(HttpStatus.BAD_REQUEST).data("parameter_error");
        }
        userService.deleteAll(uids);
        return ResponseEntity.ofSuccess().status(HttpStatus.OK).data("Delete Order Success.");
    }

    // 获取用户列表 { pageSize: 10, pageNo: 1 }
    @GetMapping
    public ResponseEntity<Page<User>> findAll(UserQueryDTO query , STablePageRequest spageable) {
        System.out.println("findAll");
        if(null==query||null==spageable){
            return ResponseEntity.ofFailed().status(HttpStatus.BAD_REQUEST);
        }
        spageable.setSortField("uid");
        Page<User> page = userService.findAll(UserQueryDTO.getWhereClause(query), spageable.getPageable());
        return ResponseEntity.ofSuccess().status(HttpStatus.OK).data(page);
    }
    // 获取用户订单列表
    @GetMapping("/order")
    public ResponseEntity<Page<Order>> findAllOrder(UserQueryDTO query , STablePageRequest spageable) {
        System.out.println("findAllOrder");
        if(null==query||null==spageable){
            return ResponseEntity.ofFailed().status(HttpStatus.BAD_REQUEST);
        }
        spageable.setSortField("oid");
        Page<Order> page = userService.findAllOrder(UserQueryDTO.getOrderSepcByUser(query), spageable.getPageable());
        return ResponseEntity.ofSuccess().status(HttpStatus.OK).data(page);
    }
}
