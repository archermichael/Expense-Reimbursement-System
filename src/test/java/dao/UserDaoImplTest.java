package dao;

import models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class UserDaoImplTest {
    UserDao userDao;

    @BeforeEach
    void setup(){
        userDao = Mockito.mock(UserDaoImpl.class);
    }

    @Test
    void login() {
        //Assign
        User user = new User();
        user.setUsername("PStar");
        user.setPassword("Mike1455.");
        User expectedResult = user;

        Mockito.when(userDao.login(user)).thenReturn(user);
        //Act
        User actualResult = userDao.login(user);

        //Assert
        assertEquals(expectedResult.getUsername(), actualResult.getUsername());
        System.out.println(actualResult);
    }

    @Test
    void register() {
        User user = new User(null, //usersId may be set to null; schema will assign an ID
                "SomethingRandom",
                "password123",
                "John",
                "Doe",
                "somerandomemail@gmail.com",
                2);

        User expectedResult = user;

        Mockito.when(userDao.register(user)).thenReturn(user);
        //Act
        User actualResult = userDao.register(user);

        //Assert
        assertEquals(expectedResult.toString(), actualResult.toString());
        System.out.println(expectedResult == actualResult);
    }
}