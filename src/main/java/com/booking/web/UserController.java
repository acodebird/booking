package com.booking.web;

import com.booking.domain.Order;
import com.booking.domain.User;
import com.booking.dto.UserQueryDTO;
import com.booking.service.CommentService;
import com.booking.service.LoginServiceImpl;
import com.booking.service.OrderService;
import com.booking.service.UserService;
import com.booking.utils.STablePageRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import com.booking.utils.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
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
    // uid:用户uid
    // return:返回用户信息User
    @GetMapping("/{uid}")
    public User findById (@PathVariable("uid") Long uid) throws Exception {
        System.out.println("findById");
        if(null==uid){
            throw new Exception("parameter_error");
        }
        return userService.getUserById(uid);
    }

//    @PostMapping
//    public ResponseEntity<String> save (@RequestBody User user) {
//        System.out.println("save");
//        if(null==user){
//            return ResponseEntity.ofFailed().status(HttpStatus.BAD_REQUEST).data("parameter_error");
//        }
//        userService.save(user);
//        return ResponseEntity.ofSuccess().status(HttpStatus.OK).data("success");
//    }

    // 更新用户
    // uid:用户uid
    // user:包含要更新信息的User
    // return:返回储存相关代码(views/utils/errorTips.js)的响应实体ResponseEntity<String>
    @PutMapping(value="{uid}")
    public ResponseEntity<String> update (@PathVariable("uid") Long uid, @RequestBody User user,HttpSession session) {
        System.out.println("update");
        if(null==uid||null==user){
            return ResponseEntity.ofFailed().status(HttpStatus.BAD_REQUEST).data("parameter_error");
        }
        User target = userService.getUserById(uid);
        if(target==null) {
            return ResponseEntity.ofFailed().status(HttpStatus.BAD_REQUEST).data("user_not_existed");
        }
        User loginUser= (User) session.getAttribute(LoginServiceImpl.LOGIN_SESSION_TOKEN);
        if(uid==loginUser.getUid()){
            return ResponseEntity.ofFailed().status(HttpStatus.BAD_REQUEST).data("change_self");
        }
        BeanUtils.copyProperties(user, target);
        userService.save(target);
        return ResponseEntity.ofSuccess().status(HttpStatus.OK).data("Update User Success.");
    }

    // 批量更新用户状态
    // uid:批量用户uid
    // return:返回储存相关代码(views/utils/errorTips.js)的响应实体ResponseEntity<String>
    @PutMapping
    public ResponseEntity<String> updateStatusBatch (@RequestBody List<Long> uids, @RequestHeader("status") Boolean newStatus,HttpSession session) {
        System.out.println("updateStatusBatch");
        if(null==uids||null==newStatus) {
            return ResponseEntity.ofFailed().status(HttpStatus.BAD_REQUEST).data("parameter_error");
        }
        User loginUser= (User) session.getAttribute(LoginServiceImpl.LOGIN_SESSION_TOKEN);
        if(uids.contains(loginUser.getUid())){
            return ResponseEntity.ofFailed().status(HttpStatus.BAD_REQUEST).data("change_self");
        }
        List<User>users=userService.findAll(uids);
        for(User user:users){
            user.setEnable(newStatus);
        }
        userService.saveAll(users);
        return ResponseEntity.ofSuccess().status(HttpStatus.OK).data("Update User Success.");
    }

    // 删除用户
    // uid:被删除的用户的uid
    // return:返回储存相关代码(views/utils/errorTips.js)的响应实体ResponseEntity<String>
    @DeleteMapping(value = "{uid}")
    public ResponseEntity<String> delete(@PathVariable("uid") Long uid, HttpSession session) {
        System.out.println("delete uid="+uid);
        if (null==uid) {
            return ResponseEntity.ofFailed().status(HttpStatus.BAD_REQUEST).data("parameter_error");
        }
        User user= (User) session.getAttribute(LoginServiceImpl.LOGIN_SESSION_TOKEN);
        if(user.getUid()==uid){
            return ResponseEntity.ofFailed().status(HttpStatus.BAD_REQUEST).data("change_self");
        }
        userService.deleteById(uid);
        return ResponseEntity.ofSuccess().status(HttpStatus.OK).data("Delete Order Success.");
    }

    // 批量删除用户
    // uid:被删除的用户uid
    // return:返回储存相关代码(views/utils/errorTips.js)的响应实体ResponseEntity<String>
    @DeleteMapping
    public ResponseEntity<String> deleteBatch(@RequestBody List<Long> uids, HttpSession session) {
        System.out.println("deleteBatch");
        if (null==uids) {
            return ResponseEntity.ofFailed().status(HttpStatus.BAD_REQUEST).data("parameter_error");
        }
        User user= (User) session.getAttribute(LoginServiceImpl.LOGIN_SESSION_TOKEN);
        if(uids.contains(user.getUid())){
            return ResponseEntity.ofFailed().status(HttpStatus.BAD_REQUEST).data("change_self");
        }
        userService.deleteAll(uids);
        return ResponseEntity.ofSuccess().status(HttpStatus.OK).data("Delete Order Success.");
    }

    // 获取用户列表 { pageSize: 10, pageNo: 1 }
    // query:查询实体, 用于提供查询条件
    // spageable:分页实体, 用于提供分页条件
    // return:返回储存已经分页的用户信息Page<User>的响应实体ResponseEntity<Page<User>>
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
    // 获取某个用户订单列表
    // query:查询实体, 用于提供查询条件
    // spageable:分页实体, 用于提供分页条件
    // return:返回储存已经分页的用户信息Page<User>的响应实体ResponseEntity<Page<User>>
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
