package com.booking.service;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.booking.domain.*;
import com.booking.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringRunner;

import com.booking.enums.CommentTypeEnum;
import com.booking.enums.HotelTypeEnum;
import com.booking.enums.OrderStatusEnum;
import com.booking.enums.PayTypeEnum;
import com.booking.enums.RoomTypeEnum;
import com.booking.utils.SHA2;

/*
运行initDB初始化数据库
请先把原有数据库删除并新建一个
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class InitDB {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CommentRepository commentRepository;
//    @Autowired
//    private RoleRepository roleRepository;
//    @Autowired
//    private PermissionRepository permissionRepository;
    @Autowired
    private SHA2 sha;

    private int userNumber = 40;
    private int hotelNumberr = 30;
    private int roomPerHotelNumber = 5;
    private int orderPerUserNumber = 5;
    private int commentPerOrderNumber = 1;

    @Test
    public void init() throws UnsupportedEncodingException, NoSuchAlgorithmException {
//        saveRole();
//        savePermission();

        saveHotel();
        saveRoom();
        saveUser();
    //    bind();
        saveOrder();
        saveComment();
    }

    public void saveHotel() {
        List<Hotel> hotels = new ArrayList<Hotel>();
        for (int i = 0; i < hotelNumberr; i++) {
            Hotel hotel = new Hotel();
            hotel.setHname("维也纳国际酒店" + i);
            hotel.setAddress("广东省东莞市大学路" + i + "号");
            hotel.setDescription("于2010年开业，东莞市维也纳国际酒店旗舰店，适合广大人群入驻");
            hotel.setFacilities("停车场,餐厅");
            hotel.setService("接待外宾,叫醒服务");
            hotel.setPhone("12345678910");
            hotel.setType(HotelTypeEnum.APARTMENT);
            hotel.setRate(4.8f);
            hotel.setImg("/upload/hotel/e3f2b968-e07b-4dfa-b04a-accfb19431bf.jpg");
//			hotel.setRooms(null);
//			hotel.setOrders(null);
//			hotel.setComments(null);
            hotels.add(hotel);
        }
        hotelRepository.saveAll(hotels);
    }

    public void saveRoom() {
        List<Room> rooms = new ArrayList<Room>();
        List<Hotel> hotels = (List<Hotel>) hotelRepository.findAll();
        for (Hotel hotel : hotels) {
            for (int i = 0; i < roomPerHotelNumber; i++) {
                Room room = new Room();
                if (i % 2 == 0) {
                    room.setRname("精品单床房" + i);
                    room.setType(RoomTypeEnum.SUPERIOR);
                    room.setBreakfast("含早");
                    room.setCancel("免费取消");
                    room.setPeople(2);
                    room.setPrice(199.9);
                    room.setAssitions("床型：单床1.8米,面积：12㎡,不允许加床,外窗");
                } else {
                    room.setRname("豪华双床房" + i);
                    room.setType(RoomTypeEnum.DELUXE);
                    room.setBreakfast("不含早");
                    room.setCancel("限时取消");
                    room.setPeople(4);
                    room.setPrice(249.9);
                    room.setAssitions("床型：双床1.8米,面积：30㎡,不允许加床,外窗");
                }
                room.setImg("xxxxxxxxxx");
//				room.setOrders(null);
                room.setHotel(hotel);
                rooms.add(room);
            }
        }
        roomRepository.saveAll(rooms);
    }
    @Test
    public void saveUser() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String p = "0000000000";
        List<User> users = new ArrayList<User>();
        for (int i = 0; i < userNumber; i++) {
            String salt = sha.getSalt(128);
            String password = "password_" + i;
            password = sha.sha2(password + salt);
            User user = new User();
            user.setSalt(salt);
            user.setUpassword(password);
            user.setEmail(i + "_@example.com");
            user.setEnable((1 == (i % 2) ? false : true));
            user.setIcon("/upload/user/avatar/default_avatar.jpeg");
            user.setTelephone((String.valueOf(i) + p).substring(0, 11));
            user.setType(i % 2);

            user.setUname("name_" + i);
            users.add(user);
        }
        userRepository.saveAll(users);
    }
//    @Test
//    public void bind(){
//        RoleQueryDTO roleQueryDTO=new RoleQueryDTO();
//        roleQueryDTO.setName("USER");
//        Role userRole=roleRepository.findOne(RoleQueryDTO.getSpecification(roleQueryDTO)).get();
//        roleQueryDTO.setName("ADMIN");
//        Role adminRole=roleRepository.findOne(RoleQueryDTO.getSpecification(roleQueryDTO)).get();
//
//        List<User> users= (List<User>) userRepository.findAll();
//        for(int i=users.size()-1;i>=0;i--){
//            User user=users.get(i);
//            user.getAuthorities().add(userRole);
//            if(1==user.getType()){
//                user.getAuthorities().add(adminRole);
//            }
//            users.set(i,user);
//        }
//        userRepository.saveAll(users);
//    }

    public void saveOrder() {
        List<Order> orders = new ArrayList<Order>();
        List<User> users = (List<User>) userRepository.findAll();
        List<Hotel> hotels = (List<Hotel>) hotelRepository.findAll();
        int hcount = hotels.size();
        int j = 0;
        for (User user : users) {
            for (int i = 1; i <= orderPerUserNumber; i++) {
                Order order = new Order();
                order.setCount(2);
                order.setPrice(129.9);
                order.setTotalPrice(order.getCount() * order.getPrice());
                order.setTelephone(user.getTelephone());
                order.setPayType(PayTypeEnum.ONLINE);
                order.setCreateTime(new Date());
                order.setStartTime(new Date());
                order.setEndTime(new Date());
                if (i % 4 == 0) order.setStatus(OrderStatusEnum.UNPAY);
                else if (i % 3 == 0) order.setStatus(OrderStatusEnum.UNUSE);
                else if (i % 2 == 0) order.setStatus(OrderStatusEnum.SUCCESS);
                else order.setStatus(OrderStatusEnum.CANCEL);
                Hotel hotel = hotels.get(j >= hcount ? j % hcount : j);
                List<Room> rooms = findRoomsByHotel(hotel);
                order.setHotel(hotel);
                order.setRoom(rooms.get(j % roomPerHotelNumber));
                order.setUser(user);
                order.setRemark("无烟房,外窗");
                order.setCheckInPerson(order.getUser().getUname());
                orders.add(order);
                j++;
            }
            ;
        }
        orderRepository.saveAll(orders);
    }

    public void saveComment() {
        List<Comment> comments = new ArrayList<Comment>();
        List<Order> orders = (List<Order>) orderRepository.findAll();
        for (Order order : orders) {
            for (int i = 0; i < commentPerOrderNumber; i++) {
                Comment comment=new Comment();
                comment.setContent("这是我住过的最差劲的酒店，一晚上将近1000的房子，到了晚上七点多外面就开始唱歌，唱到11点才结束，我想休息一下都不行，还说这个没办法解决。我告诉你们怎么解决，把所有的城景套房全部下架，你们既然定了这么高的价格，就要给消费者相对应的服务和体验。 洗手间里水管是漏的，一用洗手间就满地的水。热水也要放很久很久水才有，真不知道这酒店凭什么是这个价格。真牛逼"+i);
                comment.setDate(new Date());
                comment.setOrder(order);
                comment.setRate(5f);
                comment.setType(CommentTypeEnum.AVERAGE);
                comment.setUser(order.getUser());
                comment.setHotel(order.getHotel());
                comments.add(comment);
            }
        }
        commentRepository.saveAll(comments);
    }

    public List<Room> findRoomsByHotel(Hotel hotel) {
        if (null == hotel) {
            return new ArrayList<Room>();
        }
        return roomRepository.findAll(getRoomSepcByHotel(hotel.getHid()));
    }

    public static Specification<Room> getRoomSepcByHotel(final long hid) {
        return new Specification<Room>() {
            public Predicate toPredicate(Root<Room> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<>();

                Join<Order, User> join = root.join("hotel", JoinType.LEFT);
                predicate.add(cb.equal(join.get("hid").as(Long.class), hid));
                Predicate[] pre = new Predicate[predicate.size()];

                return query.where(predicate.toArray(pre)).getRestriction();// and
            }
        };
    }
//
//    public void saveRole() {
//        Role roleNormal=new Role();
//        Role roleAdmin=new Role();
//        roleNormal.setName("USER");
//        roleAdmin.setName("ADMIN");
//
//        roleRepository.save(roleNormal);
//        roleRepository.save(roleAdmin);
//    }
//
//    @Test
//    public void savePermission() {
//        RoleQueryDTO roleQueryDTO=new RoleQueryDTO();
//        roleQueryDTO.setName("USER");
//        Role userRole=roleRepository.findOne(RoleQueryDTO.getSpecification(roleQueryDTO)).get();
//        roleQueryDTO.setName("ADMIN");
//        Role adminRole=roleRepository.findOne(RoleQueryDTO.getSpecification(roleQueryDTO)).get();
//
//        String[]userPermissionUrl={"/login/update/*"};
//        String[]userPermissionDescription={"更新用户个人信息、密码、头像"};
//        String[]userPermissionName={"UpdateUserInfo"};
//
//        String[]adminPermissionUrl={"/user/*"};
//        String[]adminPermissionDescription={"管理员管理用户"};
//        String[]adminPermissionName={"ManageUser"};
//
//        List<Permission> permissions=new ArrayList<Permission>();
//        for(int i=userPermissionUrl.length-1;i>=0;i--){
//            Permission permission=new Permission();
//            permission.setDescription(userPermissionDescription[i]);
//            permission.setUrl(userPermissionUrl[i]);
//            permission.setRoles(userRole);
//            permission.setName(userPermissionName[i]);
//            permissions.add(permission);
//        }
//        for(int i=adminPermissionUrl.length-1;i>=0;i--){
//            Permission permission=new Permission();
//            permission.setDescription(adminPermissionDescription[i]);
//            permission.setUrl(adminPermissionUrl[i]);
//            permission.setRoles(adminRole);
//            permission.setName(adminPermissionName[i]);
//            permissions.add(permission);
//        }
//        permissionRepository.saveAll(permissions);
//    }
}
