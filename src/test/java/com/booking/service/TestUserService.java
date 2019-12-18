package com.booking.service;

import com.booking.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
}
