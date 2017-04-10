package com.teamtreehouse.web.controller;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testing for {@link WeatherController}.
 */
public class WeatherControllerTest {

    private WeatherController controller;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        controller = new WeatherController();
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testGetHome() throws Exception {
        mockMvc
                .perform(get("/"))
                .andExpect(view().name("weather/detail"));

    }

    @Test
    public void testSearchShouldRedirectWithPathParam() throws Exception {
        mockMvc.perform(get("/search").param("q", "28117"))
                .andExpect(redirectedUrl("/search/28117"));
    }

}
