package controller;

import com.IQproject.court.controller.SurfaceTypeController;
import com.IQproject.court.model.SurfaceType;
import com.IQproject.court.service.SurfaceTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SurfaceTypeControllerTest {

    @Mock
    private SurfaceTypeService surfaceTypeService;

    @InjectMocks
    private SurfaceTypeController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllReturnsSurfaceTypeList() {
        List<SurfaceType> surfaceTypes = List.of(
                new SurfaceType("Clay", new BigDecimal(5)),
                new SurfaceType("Grass", new BigDecimal(5))
        );
        when(surfaceTypeService.getAllSurfaceTypes()).thenReturn(surfaceTypes);

        List<SurfaceType> result = controller.getAll();

        assertEquals(2, result.size());
        verify(surfaceTypeService).getAllSurfaceTypes();
    }

    @Test
    void getByIdReturnsSurfaceTypeIfFound() {
        SurfaceType surfaceType = new SurfaceType("Hard", new BigDecimal(5));
        surfaceType.setId(1L);
        when(surfaceTypeService.getSurfaceTypeById(1L)).thenReturn(surfaceType);

        ResponseEntity<SurfaceType> response = controller.getById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(surfaceType, response.getBody());
    }

    @Test
    void getByIdReturnsNotFoundIfMissing() {
        long id = 26L;

        when(surfaceTypeService.getSurfaceTypeById(id)).thenReturn(null);

        ResponseEntity<SurfaceType> response = controller.getById(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }


    @Test
    void createReturnsCreatedSurfaceType() {
        SurfaceType input = new SurfaceType("Carpet", new BigDecimal(50));
        SurfaceType saved = new SurfaceType("Carpet", new BigDecimal(50));

        when(surfaceTypeService.createSurfaceType(input)).thenReturn(saved);

        ResponseEntity<SurfaceType> response = controller.create(input);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(saved, response.getBody());
    }

    @Test
    void deleteReturnsNoContentIfExists() {
        long existingId = 96L;

        SurfaceType existing = new SurfaceType("To Remove", new BigDecimal(0));
        existing.setId(existingId);

        when(surfaceTypeService.getSurfaceTypeById(existingId)).thenReturn(existing);
        doNothing().when(surfaceTypeService).deleteSurfaceType(existingId);

        ResponseEntity<Void> response = controller.delete(existingId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(surfaceTypeService).deleteSurfaceType(existingId);
    }

    @Test
    void deleteReturnsNotFoundIfMissing() {
        long notExistingId = 1025L;

        when(surfaceTypeService.getSurfaceTypeById(notExistingId)).thenReturn(null);

        ResponseEntity<Void> response = controller.delete(notExistingId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
