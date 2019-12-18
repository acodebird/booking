package com.booking.service;

import com.booking.domain.User;
import com.booking.dto.UserQueryDTO;
import com.booking.utils.ResponseEntity;
import com.booking.utils.STablePageRequest;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestUserService {
    @Autowired
    UserService userService;

    @Test
    public void saveUsers(){
        String p="0000000000";
        for(int i=0;i<100;i++){
            User user=new User();
            user.setSalt("salt_"+i);
            user.setUpassword("password_"+i);
            user.setEmail(i+"_@example.com");
            user.setEnable((1==(i%2)?false:true));
            user.setIcon("/upload/user/avatar/default_avatar.jpeg");
            user.setTelephone((String.valueOf(i)+p).substring(0,11));
            user.setType(i%2);
            user.setUname("name_"+i);
            userService.save(user);
        }
    }
    @Test
    public void findUsers(){
        STablePageRequest pageable=new STablePageRequest();
        UserQueryDTO queryDTO=new UserQueryDTO();
        Page<User> page = userService.findAll(queryDTO.getWhereClause(queryDTO),pageable.getUserPageable());
    }
}
