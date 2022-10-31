package com.tweetapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import com.tweetapp.exception.UnauthorizedException;
import com.tweetapp.model.UserData;
import com.tweetapp.repository.UserRepository;
import com.tweetapp.serviceimpl.CustomerDetailsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ServiceTest {

    UserDetails userdetails;

    @Mock
    UserRepository userservice;

    @InjectMocks
    CustomerDetailsService custdetailservice;




    @Test
    void loadUserByUsernameTest() {

        UserData userData=new UserData("Sridevi","Koyilada","Sridevi@gmail.com","Sridevi2408","Sridevi@123","Sridevi@123",999999990);
        Optional<UserData> data =Optional.of(userData) ;
        when(userservice.findById("Sridevi2408")).thenReturn(data);
        UserDetails loadUserByUsername2 = custdetailservice.loadUserByUsername("Sridevi2408");
        assertEquals(userData.getUserName(),loadUserByUsername2.getUsername());
    }
    @Test
    void loadUserByUsernameTestFail() {

        Optional<UserData> data =Optional.ofNullable(null) ;
        when(userservice.findById("Sridevi2408")).thenReturn(data);
        assertThrows( UnauthorizedException.class,()->custdetailservice.loadUserByUsername("Sridevi2408"));
    }



    @Test
    void userNotFound() {

        when(userservice.findById("Sridevi2408")).thenReturn(null);
        assertThrows( UnauthorizedException.class,()->custdetailservice.loadUserByUsername("Sridevi2408"));
    }
}
