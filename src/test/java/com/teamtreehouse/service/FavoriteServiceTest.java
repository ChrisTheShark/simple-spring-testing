package com.teamtreehouse.service;

import com.teamtreehouse.dao.FavoriteDao;
import com.teamtreehouse.domain.Favorite;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

/**
 * Testing for {@link FavoriteService}.
 */
@RunWith(MockitoJUnitRunner.class)
public class FavoriteServiceTest {

    @InjectMocks
    private FavoriteService favoriteService = new FavoriteServiceImpl();

    @Mock
    private FavoriteDao mockFavoriteDao;

    @Test
    public void testFindAll() throws Exception {
        List<Favorite> favorites = Arrays.asList(
                new Favorite(),
                new Favorite()
        );
        when(mockFavoriteDao.findAll()).thenReturn(favorites);
        assertEquals("Find all should return 2 favorites",
                2, favoriteService.findAll().size());
        verify(mockFavoriteDao).findAll();
    }

    @Test
    public void testFindById() throws Exception {
        when(mockFavoriteDao.findOne(1l)).thenReturn(new Favorite());
        assertThat(favoriteService.findById(1l), instanceOf(Favorite.class));
        verify(mockFavoriteDao).findOne(1l);
    }

    @Test(expected = FavoriteNotFoundException.class)
    public void testFindByIdNonExistantId() throws Exception {
        when(mockFavoriteDao.findOne(1l)).thenReturn(null);
        favoriteService.findById(1l);
    }

}
