package one.mimic.api.controller;

import lombok.AllArgsConstructor;
import one.mimic.api.dto.WordDTO;
import one.mimic.api.exception.WordAlreadyRegisteredException;
import one.mimic.api.exception.WordNotFoundException;
import one.mimic.api.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/words")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class WordController implements WordControllerDocs {

    private final WordService wordService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WordDTO createWord(@RequestBody @Valid WordDTO wordDTO) throws WordAlreadyRegisteredException {
        return wordService.createWord(wordDTO);
    }

    @GetMapping("/{name}")
    public WordDTO findByName(@PathVariable String name) throws WordNotFoundException {
        return wordService.findByName(name);
    }

    @GetMapping
    public List<WordDTO> listWords() {
        return wordService.listAll();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) throws WordNotFoundException {
        wordService.deleteById(id);
    }

}
