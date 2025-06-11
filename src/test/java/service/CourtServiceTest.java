package service;

import com.IQproject.court.model.Court;
import com.IQproject.court.model.SurfaceType;
import com.IQproject.court.repository.CourtRepository;
import com.IQproject.court.repository.SurfaceTypeRepository;
import com.IQproject.court.service.CourtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CourtServiceTest {

    private CourtRepository courtRepository;
    private SurfaceTypeRepository surfaceTypeRepository;
    private CourtService courtService;

    @BeforeEach
    void setUp() {
        courtRepository = mock(CourtRepository.class);
        surfaceTypeRepository = mock(SurfaceTypeRepository.class);
        courtService = new CourtService(courtRepository, surfaceTypeRepository);
    }

    @Test
    void testGetAllCourtsReturnsList() {
        when(courtRepository.findAll()).thenReturn(Collections.emptyList());

        List<Court> courts = courtService.getAllCourts();
        assertNotNull(courts);
        assertEquals(0, courts.size());

        verify(courtRepository, times(1)).findAll();
    }

    @Test
    void testGetCourtReturnsCourt() {
        long id = 1L;

        Court mockCourt = new Court();
        when(courtRepository.findById(id)).thenReturn(mockCourt);

        Court result = courtService.getCourt(id);

        assertEquals(mockCourt, result);
        verify(courtRepository).findById(id);
    }

    @Test
    void testCreateCourtWithValidSurfaceType() {
        long surfaceId = 2L;

        Court newCourt = new Court();
        newCourt.setSurfaceTypeId(surfaceId);

        when(surfaceTypeRepository.findById(surfaceId)).thenReturn(new SurfaceType());
        when(courtRepository.save(newCourt)).thenReturn(newCourt);

        Court result = courtService.createCourt(newCourt);

        assertEquals(newCourt, result);
        verify(surfaceTypeRepository).findById(surfaceId);
        verify(courtRepository).save(newCourt);
    }

    @Test
    void testCreateCourtWithInvalidSurfaceTypeThrowsException() {
        long notExistingSurfaceTypeId = 184L;

        Court newCourt = new Court();
        newCourt.setSurfaceTypeId(notExistingSurfaceTypeId);

        when(surfaceTypeRepository.findById(notExistingSurfaceTypeId)).thenReturn(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            courtService.createCourt(newCourt);
        });

        assertTrue(ex.getMessage().contains("SurfaceType with ID " + notExistingSurfaceTypeId + " does not exist."));
    }

    @Test
    void testUpdateCourtSuccess() {
        String originalName = "Best Court";
        String newName = "Court";

        long courtId = 10L;
        long originalSurfaceId = 1L;
        long newSurfaceId = 2L;

        Court existing = new Court();
        existing.setId(courtId);
        existing.setName(originalName);
        existing.setSurfaceTypeId(originalSurfaceId);

        Court updated = new Court();
        updated.setName(newName);
        updated.setSurfaceTypeId(newSurfaceId);

        when(courtRepository.findById(courtId)).thenReturn(existing);
        when(courtRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Court result = courtService.updateCourt(courtId, updated);

        assertNotNull(result);
        assertEquals(newName, result.getName());
        assertEquals(newSurfaceId, result.getSurfaceTypeId());
    }

    @Test
    void testUpdateCourtNotFoundThrowsException() {
        long id = 1L;

        when(courtRepository.findById(id)).thenReturn(null);

        Court updated = new Court();
        updated.setName("New");

        assertThrows(IllegalArgumentException.class, () -> {
            courtService.updateCourt(id, updated);
        });
    }

    @Test
    void testDeleteCourtCallsSoftDelete() {
        long id = 15L;

        courtService.deleteCourt(id);
        verify(courtRepository).softDelete(id);
    }
}