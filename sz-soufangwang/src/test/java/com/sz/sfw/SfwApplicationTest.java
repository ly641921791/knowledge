package com.sz.sfw;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@Configuration
@SpringBootTest
@ActiveProfiles("dev")
@RunWith(SpringRunner.class)
public class SfwApplicationTest {

}