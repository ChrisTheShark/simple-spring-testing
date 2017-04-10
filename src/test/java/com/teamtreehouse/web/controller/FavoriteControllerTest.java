package com.teamtreehouse.web.controller;

import com.teamtreehouse.domain.Favorite;
import com.teamtreehouse.service.FavoriteNotFoundException;
import com.teamtreehouse.service.FavoriteService;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.when;

/**
 * Testing for {@link WeatherController}.
 */
@RunWith(MockitoJUnitRunner.class)
public class FavoriteControllerTest {

    @InjectMocks
    private FavoriteController controller;

    @Mock
    private FavoriteService service;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testIndexIncludesFavoritesInModel() throws Exception {
        List<Favorite> favorites = Arrays.asList(
                new Favorite.FavoriteBuilder(1L)
                    .withAddress("Charlotte")
                    .withPlaceId("clt")
                    .build(),
                new Favorite.FavoriteBuilder(2L)
                    .withAddress("Miami")
                    .withPlaceId("mia")
                    .build()
        );

        when(service.findAll()).thenReturn(favorites);
        mockMvc.perform(get("/favorites"))
                .andExpect(status().isOk())
                .andExpect(view().name("favorite/index"))
                .andExpect(model().attribute("favorites", favorites));
        verify(service).findAll();
    }

    @Test
    public void testAddFavoriteRedirectsToNewFavorite() throws Exception {
        doAnswer(invocation -> {
            Favorite favorite = (Favorite) invocation.getArguments()[0];
            favorite.setId(1L);
            return null;
        }).when(service).save(any(Favorite.class));
        mockMvc.perform(
                post("/favorites")
                    .param("formattedAddress", "Charlotte, NC")
                    .param("placeId", "clt")
                ).andExpect(redirectedUrl("/favorites/1"));
        verify(service).save(any(Favorite.class));
    }

    @Test
    public void testDetailShouldErrorOnNotFound() throws Exception {
        when(service.findById(1L)).thenThrow(FavoriteNotFoundException.class);
        mockMvc.perform(get("/favorites/1"))
                .andExpect(view().name("error"))
                .andExpect(model().attribute("ex",
                        Matchers.instanceOf(FavoriteNotFoundException.class)));
        verify(service).findById(1L);
    }

}
