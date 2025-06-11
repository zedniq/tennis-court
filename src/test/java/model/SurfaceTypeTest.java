package model;

import com.IQproject.court.model.SurfaceType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class SurfaceTypeTest {
    @Test
    void testDefaultConstructorAndSettersGetters() {
        SurfaceType surfaceType = new SurfaceType();

        Long id = 1L;
        String name = "Clay";
        BigDecimal price = new BigDecimal(159);

        assertNull(surfaceType.getId());
        assertNull(surfaceType.getName());
        assertNull(surfaceType.getPricePerMinute());
        assertFalse(surfaceType.isDeleted()); // default value


        surfaceType.setId(id);
        surfaceType.setName(name);
        surfaceType.setPricePerMinute(price);
        surfaceType.setDeleted(true);

        assertEquals(id, surfaceType.getId());
        assertEquals(name, surfaceType.getName());
        assertEquals(price, surfaceType.getPricePerMinute());
        assertTrue(surfaceType.isDeleted());
    }

    @Test
    void testParameterizedConstructor() {
        String name = "Grass";
        BigDecimal price = new BigDecimal("2.00");

        SurfaceType surfaceType = new SurfaceType(name, price);

        assertNull(surfaceType.getId()); // ID is not set yet
        assertEquals(name, surfaceType.getName());
        assertEquals(price, surfaceType.getPricePerMinute());
        assertFalse(surfaceType.isDeleted()); // default value
    }
}