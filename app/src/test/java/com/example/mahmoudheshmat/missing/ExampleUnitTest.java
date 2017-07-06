package com.example.mahmoudheshmat.missing;

import com.example.mahmoudheshmat.missing.Activites.login;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;
import dalvik.annotation.TestTargetClass;

import static org.junit.Assert.*;

public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void login_isCorrect() throws Exception {
        //login log = mock(login.class);
        //Mockito.verify(log, times(0)).checkvervications("admin@yahoo.com", "12345678");

        /*login log = new login();
        String x = log.checkvervications("admin@yahoo.com", "123456789");
        assertEquals("found", x);*/
    }
}