package model;

import com.IQproject.court.model.Court;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CourtTest {
    @Test
    void testAllGettersAndSetters() {
        Court court = new Court();

        Long expectedId = 10L;
        String expectedName = "Court A";
        long expectedSurfaceTypeId = 5L;
        boolean expectedDeleted = true;

        court.setId(expectedId);
        court.setName(expectedName);
        court.setSurfaceTypeId(expectedSurfaceTypeId);
        court.setDeleted(expectedDeleted);

        assertEquals(expectedId, court.getId());
        assertEquals(expectedName, court.getName());
        assertEquals(expectedSurfaceTypeId, court.getSurfaceTypeId());
        assertTrue(court.isDeleted());
    }

    @Test
    void testConstructorWithParameters() {
        String name = "Clay Court";
        long surfaceTypeId = 2L;

        Court court = new Court(name, surfaceTypeId);

        assertNull(court.getId()); // ID is not set yet
        assertEquals(name, court.getName());
        assertEquals(surfaceTypeId, court.getSurfaceTypeId());
        assertFalse(court.isDeleted()); // default value
    }
}