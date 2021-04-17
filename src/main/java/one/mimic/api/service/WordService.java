package one.mimic.api.service;

import lombok.AllArgsConstructor;
import one.mimic.api.dto.WordDTO;
import one.mimic.api.entity.Word;
import one.mimic.api.exception.WordAlreadyRegisteredException;
import one.mimic.api.exception.WordNotFoundException;
import one.mimic.api.mapper.WordMapper;
import one.mimic.api.repository.WordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class WordService {

    private final WordRepository wordRepository;
    private final WordMapper wordMapper = WordMapper.INSTANCE;

    public WordDTO createWord(WordDTO wordDTO) throws WordAlreadyRegisteredException {
        verifyIfIsAlreadyRegistered(wordDTO.getName());
        Word word = wordMapper.toModel(wordDTO);
        Word savedWord = wordRepository.save(word);

        return wordMapper.toDTO(savedWord);
    }

    public WordDTO findByName(String name) throws WordNotFoundException {
        Word foundWord = wordRepository.findByName(name)
                .orElseThrow(() -> new WordNotFoundException(name));
        return wordMapper.toDTO(foundWord);
    }

    public List<WordDTO> listAll() {
        return wordRepository.findAll()
                .stream()
                .map(wordMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void deleteById(Long id) throws WordNotFoundException {
        verifyIfExists(id);
        wordRepository.deleteById(id);
    }

    private void verifyIfIsAlreadyRegistered(String name) throws WordAlreadyRegisteredException {
        Optional<Word> optSavedWord = wordRepository.findByName(name);
        if (optSavedWord.isPresent()) {
            throw new WordAlreadyRegisteredException(name);
        }
    }

    private Word verifyIfExists(Long id) throws WordNotFoundException {
        return wordRepository.findById(id)
                .orElseThrow(() -> new WordNotFoundException(id));
    }
}
