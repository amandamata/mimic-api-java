package one.mimic.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import one.mimic.api.dto.WordDTO;
import one.mimic.api.exception.WordAlreadyRegisteredException;
import one.mimic.api.exception.WordNotFoundException;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Api("Manages words for a mimic game")
public interface WordControllerDocs {

    @ApiOperation(value = "Word creation operation")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Success word creation"),
            @ApiResponse(code = 400, message = "Missing required fields or wrong field range value.")
    })
    WordDTO createWord(WordDTO wordDTO) throws WordAlreadyRegisteredException;

    @ApiOperation(value = "Returns word found by a given name")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success word found in the system"),
            @ApiResponse(code = 404, message = "Word with given name not found.")
    })
    WordDTO findByName(@PathVariable String name) throws WordNotFoundException;

    @ApiOperation(value = "Returns a list of all words registered in the system")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of all words registered in the system"),
    })
    List<WordDTO> listWords();

    @ApiOperation(value = "Delete a word found by a given valid Id")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Success word deleted in the system"),
            @ApiResponse(code = 404, message = "Word with given id not found.")
    })
    void deleteById(@PathVariable Long id) throws WordNotFoundException;
}
