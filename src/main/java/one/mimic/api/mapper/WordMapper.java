package one.mimic.api.mapper;

import one.mimic.api.dto.WordDTO;
import one.mimic.api.entity.Word;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface WordMapper {

    WordMapper INSTANCE = Mappers.getMapper(WordMapper.class);

    Word toModel(WordDTO wordDTO);

    WordDTO toDTO(Word word);
}
