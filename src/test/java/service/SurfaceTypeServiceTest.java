package service;

import com.IQproject.court.model.SurfaceType;
import com.IQproject.court.repository.SurfaceTypeRepository;
import com.IQproject.court.service.SurfaceTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SurfaceTypeServiceTest {

    private SurfaceTypeRepository repository;
    private SurfaceTypeService service;

    @BeforeEach
    void setUp() {
        repository = mock(SurfaceTypeRepository.class);
        service = new SurfaceTypeService(repository);
    }

    @Test
    void testGetAllSurfaceTypes() {
        SurfaceType clay = new SurfaceType("Clay", BigDecimal.valueOf(1545));
        SurfaceType grass = new SurfaceType("Grass", BigDecimal.valueOf(2234530));

        when(repository.findAll()).thenReturn(Arrays.asList(clay, grass));

        List<SurfaceType> result = service.getAllSurfaceTypes();

        assertEquals(2, result.size());
        assertEquals("Clay", result.get(0).getName());
    }

    @Test
    void testGetSurfaceTypeByIdExists() {
        String surfaceName = "Clay";

        SurfaceType type = new SurfaceType(surfaceName, BigDecimal.valueOf(1.8));
        when(repository.findById(1L)).thenReturn(type);

        SurfaceType result = service.getSurfaceTypeById(1L);

        assertNotNull(result);
        assertEquals(surfaceName, result.getName());
    }

    @Test
    void testGetSurfaceTypeByIdNotFound() {
        long notExistingId = 254L;

        when(repository.findById(notExistingId)).thenReturn(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            service.getSurfaceTypeById(notExistingId);
        });

        assertEquals("SurfaceType does not exist", ex.getMessage());
    }

    @Test
    void testCreateSurfaceType() {
        String name = "Carpet";
        BigDecimal perMin = new BigDecimal(789746);

        SurfaceType newType = new SurfaceType(name, perMin);
        when(repository.save(newType)).thenReturn(newType);

        SurfaceType result = service.createSurfaceType(newType);

        assertNotNull(result);
        assertEquals(name, result.getName());
        assertEquals(perMin, result.getPricePerMinute());
    }

    @Test
    void testDeleteSurfaceType() {
        doNothing().when(repository).softDelete(1L);

        service.deleteSurfaceType(1L);

        verify(repository, times(1)).softDelete(1L);
    }
}