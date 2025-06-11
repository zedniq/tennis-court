package controller;

import com.IQproject.court.controller.CourtController;
import com.IQproject.court.model.Court;
import com.IQproject.court.service.CourtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CourtControllerTest {

    @Mock
    private CourtService courtService;

    @InjectMocks
    private CourtController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllReturnsListOfCourts() {
        List<Court> mockCourts = List.of(
                new Court("Court A", 1L),
                new Court("Court B", 2L)
        );
        when(courtService.getAllCourts()).thenReturn(mockCourts);

        List<Court> result = controller.getAll();

        assertEquals(2, result.size());
        verify(courtService).getAllCourts();
    }

    @Test
    void getByIdReturnsCourtIfExists() {
        long expectedId = 1L;

        Court court = new Court("Center", 10L);
        court.setId(expectedId);
        when(courtService.getCourt(expectedId)).thenReturn(court);

        ResponseEntity<Court> response = controller.getById(expectedId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(court, response.getBody());
    }

    @Test
    void getByIdReturnsNotFoundIfCourtDoesNotExist() {
        long nonValidId = 999L;

        when(courtService.getCourt(nonValidId)).thenReturn(null);

        ResponseEntity<Court> response = controller.getById(nonValidId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void createReturnsCreatedCourt() {
        Court input = new Court("New Court", 1L);
        Court saved = new Court("New Court", 1L);

        when(courtService.createCourt(input)).thenReturn(saved);

        ResponseEntity<Court> response = controller.create(input);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(saved, response.getBody());
    }

    @Test
    void updateReturnsUpdatedCourtIfExists() {
        long Id = 10L;

        Court updated = new Court("Updated Court", 2L);
        updated.setId(Id);

        when(courtService.getCourt(Id)).thenReturn(updated);
        when(courtService.updateCourt(eq(Id), any(Court.class))).thenReturn(updated);

        ResponseEntity<Court> response = controller.update(Id, updated);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updated, response.getBody());
    }

    @Test
    void updateReturnsNotFoundIfCourtDoesNotExist() {
        long nonValidId = 999L;

        when(courtService.getCourt(nonValidId)).thenReturn(null);

        ResponseEntity<Court> response = controller.update(nonValidId, new Court());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteReturnsNoContentIfCourtExists() {
        long id = 15L;

        Court court = new Court("To delete", 1L);
        court.setId(id);

        when(courtService.getCourt(id)).thenReturn(court);
        doNothing().when(courtService).deleteCourt(id);

        ResponseEntity<Void> response = controller.delete(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(courtService).deleteCourt(id);
    }

    @Test
    void deleteReturnsNotFoundIfCourtDoesNotExist() {
        long notExistingId = 26L;

        when(courtService.getCourt(notExistingId)).thenReturn(null);

        ResponseEntity<Void> response = controller.delete(notExistingId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}