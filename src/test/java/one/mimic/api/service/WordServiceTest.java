package one.mimic.api.service;

import one.mimic.api.builder.WordDTOBuilder;
import one.mimic.api.dto.WordDTO;
import one.mimic.api.entity.Word;
import one.mimic.api.exception.WordAlreadyRegisteredException;
import one.mimic.api.exception.WordNotFoundException;
import one.mimic.api.mapper.WordMapper;
import one.mimic.api.repository.WordRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WordServiceTest {
    private static final long INVALID_WORD_ID = 1L;

    @Mock
    private WordRepository wordRepository;

    private final WordMapper wordMapper = WordMapper.INSTANCE;

    @InjectMocks
    private WordService wordService;

    @Test
    void whenWordInformedThenItShouldBeCreated() throws WordAlreadyRegisteredException {
        // given
        WordDTO expectedWordDTO = WordDTOBuilder.builder().build().toWordDTO();
        Word expectedSaveWord = wordMapper.toModel(expectedWordDTO);

        // when
        //Mockito.when(wordRepository.findByName(wordDTO.getName())).thenReturn(Optional.empty());
        //Mockito.when(wordRepository.save(expectedSaveWord)).thenReturn(expectedSaveWord);
        when(wordRepository.findByName(expectedWordDTO.getName())).thenReturn(Optional.empty());
        when(wordRepository.save(expectedSaveWord)).thenReturn(expectedSaveWord);

        // then
        WordDTO createdWordDTO = wordService.createWord(expectedWordDTO);
        //assertEquals(wordDTO.getId(), createdWordDTO.getId());
        //assertEquals(wordDTO.getName(), createdWordDTO.getName());
        //using hamcrest
        //MatcherAssert.assertThat(createdWordDTO.getId(), Matchers.is(Matchers.equalTo(wordDTO.getId())));
        assertThat(createdWordDTO.getId(), is(equalTo(expectedWordDTO.getId())));
        assertThat(createdWordDTO.getName(), is(equalTo(expectedWordDTO.getName())));
        assertThat(createdWordDTO.getScore(), is(equalTo(expectedWordDTO.getScore())));
        assertThat(createdWordDTO.getCategory(), is(equalTo(expectedWordDTO.getCategory())));

    }

    @Test
    void whenAlreadyRegisteredWordInformedThenAnExceptionShouldBeThrown() {
        // given
        WordDTO expectedWordDTO = WordDTOBuilder.builder().build().toWordDTO();
        Word duplicatedWord = wordMapper.toModel(expectedWordDTO);

        // when
        when(wordRepository.findByName(expectedWordDTO.getName())).thenReturn(Optional.of(duplicatedWord));

        // then
        assertThrows(WordAlreadyRegisteredException.class, () ->  wordService.createWord(expectedWordDTO));
    }

    @Test
    void whenValidWordNameIsGivenThenReturnAWord() throws WordNotFoundException {
        // given
        WordDTO expectedFoundWordDTO = WordDTOBuilder.builder().build().toWordDTO();
        Word expectedFoundWord = wordMapper.toModel(expectedFoundWordDTO);

        // when
        when(wordRepository.findByName(expectedFoundWord.getName())).thenReturn(Optional.of(expectedFoundWord));

        // then
        WordDTO foundWordDTO = wordService.findByName(expectedFoundWordDTO.getName());
        assertThat(foundWordDTO, is(equalTo(expectedFoundWordDTO)));
    }

    @Test
    void whenNotRegisteredWordNameIsGivenThenThrowAnException() {
        // given
        WordDTO expectedFoundWordDTO = WordDTOBuilder.builder().build().toWordDTO();

        // when
        when(wordRepository.findByName(expectedFoundWordDTO.getName())).thenReturn(Optional.empty());

        // then
        assertThrows(WordNotFoundException.class, () ->  wordService.findByName(expectedFoundWordDTO.getName()));
    }

    @Test
    void whenListWordIsCalledThenReturnAListOfWords() {
        // given
        WordDTO expectedFoundWordDTO = WordDTOBuilder.builder().build().toWordDTO();
        Word expectedFoundWord = wordMapper.toModel(expectedFoundWordDTO);

        // when
        when(wordRepository.findAll()).thenReturn(Collections.singletonList(expectedFoundWord));

        // then
        List<WordDTO> foundListWordDTO = wordService.listAll();
        assertThat(foundListWordDTO, is(not(empty())));
        assertThat(foundListWordDTO.get(0), is(equalTo(expectedFoundWordDTO)));
    }

    @Test
    void whenListWordIsCalledThenReturnAEmptyListOfWords() {
        // when
        when(wordRepository.findAll()).thenReturn(Collections.emptyList());

        // then
        List<WordDTO> foundListWordDTO = wordService.listAll();
        assertThat(foundListWordDTO, is(empty()));
    }

    @Test
    void whenExclusionIsCalledWithValidIdThenAWordShouldBeDeleted() throws WordNotFoundException {
        // given
        WordDTO expectedDeletedWordDTO = WordDTOBuilder.builder().build().toWordDTO();
        Word expectedDeletedWord = wordMapper.toModel(expectedDeletedWordDTO);

        // when
        when(wordRepository.findById(expectedDeletedWordDTO.getId())).thenReturn(Optional.of(expectedDeletedWord));
        doNothing().when(wordRepository).deleteById(expectedDeletedWordDTO.getId());

        // then
        wordService.deleteById(expectedDeletedWordDTO.getId());
        verify(wordRepository, times(1)).findById(expectedDeletedWordDTO.getId());
        verify(wordRepository, times(1)).deleteById(expectedDeletedWordDTO.getId());
    }

    @Test
    void whenExclusionIsCalledWithInvalidIdThenExceptionShouldBeThrown() {
        // when
        when(wordRepository.findById(INVALID_WORD_ID)).thenReturn(Optional.empty());

        // then
        assertThrows(WordNotFoundException.class, () -> wordService.deleteById(INVALID_WORD_ID));
    }
}
