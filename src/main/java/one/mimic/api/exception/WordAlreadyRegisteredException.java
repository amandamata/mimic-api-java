package one.mimic.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class WordAlreadyRegisteredException extends Exception{

    public WordAlreadyRegisteredException(String wordName) {
        super(String.format("Word with name %s already registered in the system.", wordName));
    }
}
