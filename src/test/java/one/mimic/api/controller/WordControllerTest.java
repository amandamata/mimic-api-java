package one.mimic.api.controller;

import one.mimic.api.builder.WordDTOBuilder;
import one.mimic.api.dto.WordDTO;
import one.mimic.api.exception.WordNotFoundException;
import one.mimic.api.service.WordService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.print.attribute.standard.Media;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static one.mimic.api.utils.JsonConvertionUtils.asJsonString;

@ExtendWith(MockitoExtension.class)
public class WordControllerTest {

    private static final String WORD_API_URL_PATH = "/api/v1/words";
    private static final long VALID_WORD_ID = 1L;
    private static final long INVALID_WORD_ID = 1l;

    private MockMvc mockMvc;

    @Mock
    private WordService wordService;

    @InjectMocks
    private WordController wordController;

    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(wordController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView()).build();
    }

    @Test
    void whenPOSTIsCalledThenAWordIsCreated() throws Exception {
        // given
        WordDTO wordDTO =  WordDTOBuilder.builder().build().toWordDTO();

        // when
        when(wordService.createWord(wordDTO)).thenReturn(wordDTO);

        //then
        mockMvc.perform(post(WORD_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(wordDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(wordDTO.getName())))
                .andExpect(jsonPath("$.score", is(wordDTO.getScore())))
                .andExpect(jsonPath("$.category", is(wordDTO.getCategory())));

    }

    @Test
    void whenPOSTIsCalledWithoutRequiredFieldThenAnErrorIsReturned() throws Exception {
        // given
        WordDTO wordDTO =  WordDTOBuilder.builder().build().toWordDTO();
        wordDTO.setName(null);

         //then
        mockMvc.perform(post(WORD_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(wordDTO)))
                .andExpect(status().isBadRequest());

    }

    @Test
    void whenGETIsCalledWithValidNameThenOkStatusIsReturned() throws Exception {
        // given
        WordDTO wordDTO =  WordDTOBuilder.builder().build().toWordDTO();

        // when
        when(wordService.findByName(wordDTO.getName())).thenReturn(wordDTO);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(WORD_API_URL_PATH + "/" + wordDTO.getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(wordDTO.getName())))
                .andExpect(jsonPath("$.score", is(wordDTO.getScore())))
                .andExpect(jsonPath("$.category", is(wordDTO.getCategory())));
    }

    @Test
    void whenGETIsCalledWithWithoutRegisteredNameThenNotFoundStatusIsReturned() throws Exception {
        // given
        WordDTO wordDTO =  WordDTOBuilder.builder().build().toWordDTO();

        // when
        when(wordService.findByName(wordDTO.getName())).thenThrow(WordNotFoundException.class);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(WORD_API_URL_PATH + "/" + wordDTO.getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    void whenGETListWithWordsIsCalledThenOkStatusIsReturned() throws Exception {
        // given
        WordDTO wordDTO =  WordDTOBuilder.builder().build().toWordDTO();

        // when
        when(wordService.listAll()).thenReturn(Collections.singletonList(wordDTO));

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(WORD_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is(wordDTO.getName())))
                .andExpect(jsonPath("$[0].score", is(wordDTO.getScore())))
                .andExpect(jsonPath("$[0].category", is(wordDTO.getCategory())));
    }

    @Test
    void whenGETListWithoutWordsIsCalledThenOkStatusIsReturned() throws Exception {
        // given
        WordDTO wordDTO =  WordDTOBuilder.builder().build().toWordDTO();

        // when
        when(wordService.listAll()).thenReturn(Collections.singletonList(wordDTO));

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(WORD_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenDELETEIsCalledWithValidIdThenNoContentStatusIsReturned() throws Exception {
        // when
        doNothing().when(wordService).deleteById(VALID_WORD_ID);

        // then
        mockMvc.perform(delete(WORD_API_URL_PATH + "/" + VALID_WORD_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(wordService, times(1)).deleteById(VALID_WORD_ID);
    }

    @Test
    void whenDELETEIsCalledWithoutValidIdThenNotFoundStatusIsReturned() throws Exception {
        // when
        doThrow(WordNotFoundException.class).when(wordService).deleteById(INVALID_WORD_ID);

        // then
        mockMvc.perform(delete(WORD_API_URL_PATH + "/" + INVALID_WORD_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
