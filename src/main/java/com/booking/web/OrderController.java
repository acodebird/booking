package com.booking.web;

import com.booking.domain.Comment;
import com.booking.domain.Order;
import com.booking.domain.Room;
import com.booking.domain.User;
import com.booking.dto.OrderConfirmDTO;
import com.booking.dto.OrderEditDTO;
import com.booking.dto.OrderQueryDTO;
import com.booking.enums.OrderStatusEnum;
import com.booking.service.CommentService;
import com.booking.service.OrderService;
import com.booking.service.RoomService;
import com.booking.service.UserService;
import com.booking.utils.ResponseEntity;
import com.booking.utils.STablePageRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/orderManage")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    /**
     * 订单预订
     *
     * @param orderConfirmDTO
     * @param request
     * @return
     */
    @PutMapping
    public ResponseEntity add(@RequestBody OrderConfirmDTO orderConfirmDTO, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        Order order = new Order();
        order.setUser(user);
        Room room = roomService.findById(orderConfirmDTO.getRid());
        order.setRoom(room);
        order.setHotel(room.getHotel());
        order.setCreateTime(new Date());
        order.setCancelTime(new Date(order.getCreateTime().getTime() + 30 * 60 * 1000)); //订单失效时间为半小时后
        order.setPrice(room.getPrice());
        order.setStatus(OrderStatusEnum.UNPAY);
        BeanUtils.copyProperties(orderConfirmDTO, order);
        //该房型库存量减去房间的预订数量
        room.setStock(room.getStock() - order.getCount());
        roomService.save(room);
        //返回订单id
        Long oid = orderService.save(order).getOid();
        HashMap<String, Long> data = new HashMap<>();
        data.put("oid", oid);

        return ResponseEntity.ofSuccess().status(HttpStatus.OK).data(data);
    }

    /**
     * 订单付款
     *
     * @param id
     * @return
     */
    @PutMapping("/pay/{id}")
    public ResponseEntity pay(@PathVariable("id") Long id) {
        Order order = orderService.findById(id);
        orderService.isCancel(order);
        if (order.getStatus() == OrderStatusEnum.UNPAY) {
            order.setStatus(OrderStatusEnum.UNUSE);
            order.setCancelTime(order.getEndTime()); //订单自动取消日期改为离店日期
            orderService.save(order);
            return ResponseEntity.ofSuccess().status(HttpStatus.OK).data("付款成功");
        } else if (order.getStatus() == OrderStatusEnum.CANCEL) {
            return ResponseEntity.ofFailed().status(HttpStatus.OK).data("订单已取消，请重新预订");
        } else if (order.getStatus() == OrderStatusEnum.UNUSE) {
            return ResponseEntity.ofFailed().status(HttpStatus.OK).data("订单已支付");
        } else {
            return ResponseEntity.ofFailed().status(HttpStatus.OK).data("该订单已完成，不能支付");
        }
    }

    /**
     * 根据用户id列出其所有订单，可根据订单状态筛选
     *
     * @param uid
     * @param orderStatus
     * @return
     */
    @GetMapping("/user/{id}")
    public ResponseEntity<List<Order>> myOrders(@PathVariable("id") Long uid, OrderStatusEnum orderStatus) {
        List<Order> orders = orderService.findAll(new Specification<Order>() {
            @Override
            public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicate = new ArrayList<>();
                //拼接用户id
                predicate.add(criteriaBuilder.equal(root.get("user").get("uid").as(Long.class), uid));
                //拼接订单状态
                if (null != orderStatus && "ALL" != orderStatus.name()) {
                    predicate.add(criteriaBuilder.equal(root.get("status").as(OrderStatusEnum.class), orderStatus));
                }

                Predicate[] predicates = new Predicate[predicate.size()];
                //拼接排序规则
                return criteriaQuery.where(predicate.toArray(predicates))
                        .orderBy(criteriaBuilder.desc(root.get("createTime").as(Date.class)))
                        .getRestriction();
            }
        });

        return ResponseEntity.ofSuccess().status(HttpStatus.OK).data(orders);
    }

    /**
     * 根据订单id查询
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    private ResponseEntity<Order> findById(@PathVariable("id") Long id) {
        Order order = orderService.findById(id);
        if (null != order)
            return ResponseEntity.ofSuccess().status(HttpStatus.OK).data(order);
        else
            return ResponseEntity.ofFailed().status(HttpStatus.OK).data("该订单不存在");
    }

    /*--------------------------------------------------------------------------------------------------------------------*/

    /**
     * 获取一页订单
     *
     * @param pageable
     * @return
     */
    @GetMapping
    public ResponseEntity<Page<Order>> findAll(STablePageRequest pageable, OrderQueryDTO orderQueryDTO) {
        if (StringUtils.isBlank(pageable.getSortField())) {
            pageable.setSortField("status");
        }
        Page<Order> page = orderService.findAll(OrderQueryDTO.getWhereClause(orderQueryDTO), pageable.getPageable());
        return ResponseEntity.ofSuccess().status(HttpStatus.OK).data(page);
    }

    /**
     * 删除一条订单
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity deleteOrder(@PathVariable("id") Long id) {
        Comment comment = commentService.findAllByOid(id);
        if (comment != null)
            commentService.deleteByEntity(comment);

        orderService.deleteById(id);
        return ResponseEntity.ofSuccess().status(HttpStatus.OK);
    }

    /**
     * 批量删除订单
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    public ResponseEntity deleteByIds(@RequestBody List<Long> ids) {
        List<Order> orders = orderService.findAllById(ids);
        if (orders.size() != 0) {
            orders.forEach(order -> {
                Comment comment = commentService.findAllByOid(order.getOid());
                if (comment != null)
                    commentService.deleteByEntity(comment);
            });
            orderService.deleteAll(orders);
        }
        return ResponseEntity.ofSuccess().status(HttpStatus.OK);
    }

    /**
     * 编辑订单
     *
     * @param id
     * @param orderEditDTO
     * @return
     */
    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable("id") Long id, @RequestBody OrderEditDTO orderEditDTO) {
        Order target = orderService.findById(id);
        if (target != null) {
            target.setStartTime(orderEditDTO.getStartTime());
            target.setEndTime(orderEditDTO.getEndTime());
            target.setPrice(orderEditDTO.getPrice());
            target.setCount(orderEditDTO.getCount());
            target.setTotalPrice(orderEditDTO.getTotalPrice());
            target.setStatus(orderEditDTO.getStatus());
            target.setCheckInPerson(orderEditDTO.getCheckInPerson());
            target.setRemark(orderEditDTO.getRemark());
            target.getHotel().setHname(orderEditDTO.getHotel().getHname());
            target.getRoom().setRname(orderEditDTO.getRoom().getRname());
            orderService.save(target);
        }

        return ResponseEntity.ofSuccess().status(HttpStatus.OK).data("更新成功");
    }
}
