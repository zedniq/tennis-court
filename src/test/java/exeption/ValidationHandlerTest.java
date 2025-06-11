package exeption;

import com.IQproject.court.exception.ValidationHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ValidationHandlerTest {
    private ValidationHandler handler;

    @BeforeEach
    void setUp() {
        handler = new ValidationHandler();
    }

    @Test
    void handleMethodArgumentNotValidException() {
        String errorMessage = "Name is required";
        String fieldName = "randomError";

        TestObject target = new TestObject();
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(target, "test");

        bindingResult.addError(new FieldError("test", fieldName, errorMessage));

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<Map<String, String>> response = handler.handle(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, String> body = response.getBody();
        assertNotNull(body);
        assertEquals(errorMessage, body.get(fieldName));
    }

    @Test
    void handleIllegalArgumentException() {
        String error = "Invalid input";

        IllegalArgumentException ex = new IllegalArgumentException(error);

        ResponseEntity<String> response = handler.handleIllegalArgument(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        String body = response.getBody();
        assertNotNull(body);
        assertEquals(error, body);
    }

    // Dummy class for testing purpose
    static class TestObject {
        private String name;
        private String email;
    }
}