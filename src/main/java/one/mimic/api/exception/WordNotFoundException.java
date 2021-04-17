package one.mimic.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class WordNotFoundException extends Exception {

    public WordNotFoundException(String wordName) {
        super(String.format("Word with name %s not found in the system.", wordName));
    }

    public WordNotFoundException(Long id) {
        super(String.format("Word with id %s not found in the system.", id));
    }
}
