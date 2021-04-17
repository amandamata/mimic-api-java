package one.mimic.api.mapper;

import javax.annotation.processing.Generated;
import one.mimic.api.dto.WordDTO;
import one.mimic.api.entity.Word;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-04-17T08:50:11-0300",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 14.0.2 (Oracle Corporation)"
)
public class WordMapperImpl implements WordMapper {

    @Override
    public Word toModel(WordDTO wordDTO) {
        if ( wordDTO == null ) {
            return null;
        }

        Word word = new Word();

        word.setId( wordDTO.getId() );
        word.setName( wordDTO.getName() );
        word.setScore( wordDTO.getScore() );
        word.setActive( wordDTO.isActive() );
        word.setCategory( wordDTO.getCategory() );

        return word;
    }

    @Override
    public WordDTO toDTO(Word word) {
        if ( word == null ) {
            return null;
        }

        WordDTO wordDTO = new WordDTO();

        wordDTO.setId( word.getId() );
        wordDTO.setName( word.getName() );
        wordDTO.setScore( word.getScore() );
        wordDTO.setActive( word.isActive() );
        wordDTO.setCategory( word.getCategory() );

        return wordDTO;
    }
}
