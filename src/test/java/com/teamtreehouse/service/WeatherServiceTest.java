package com.teamtreehouse.service;

import com.teamtreehouse.config.AppConfig;
import com.teamtreehouse.service.dto.geocoding.Location;
import com.teamtreehouse.service.dto.weather.Weather;
import com.teamtreehouse.service.resttemplate.weather.WeatherServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.Instant;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.number.IsCloseTo.closeTo;

/**
 * Testing for {@link WeatherService}.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class WeatherServiceTest {

    private static final double ERROR_TIME = 5000;
    private static final double ERROR_GEO = 0.00000001;

    @Autowired
    private WeatherService service;

    private Location location;
    private Weather weather;

    @Before
    public void setUp() {
        location = new Location(41.9403795, -87.65318049999999);
        weather = service.findByLocation(location);
    }

    @Test
    public void testFindByLocationReturnsCorrectCoordinates() throws Exception {
        assertThat(weather.getLatitude(), closeTo(location.getLatitude(), ERROR_GEO));
        assertThat(weather.getLongitude(), closeTo(location.getLongitude(), ERROR_GEO));
    }

    @Test
    public void testFindByLocationReturnsEightDaysForecast() throws Exception {
        assertThat(weather.getDaily().getData(), hasSize(8));
    }

    @Test
    public void testFindByLocationShouldReturnCurrentConditions() throws Exception {
        Instant now = Instant.now();
        double duration = Duration.between(weather.getCurrently().getTime(), now).toMillis();
        assertThat(duration, closeTo(0, ERROR_TIME));
    }

    @Configuration
    @PropertySource("api.properties")
    public static class TestConfig {

        @Autowired
        private Environment environment;

        @Bean
        public RestTemplate restTemplate() {
            return AppConfig.defaultRestTemplate();
        }

        @Bean
        public WeatherService weatherService() {
            WeatherService service = new WeatherServiceImpl(
                    environment.getProperty("weather.api.name"),
                    environment.getProperty("weather.api.key"),
                    environment.getProperty("weather.api.host")
            );
            return service;
        }
    }

}
