package one.mimic.api.builder;

import lombok.Builder;
import one.mimic.api.dto.WordDTO;

import java.util.Date;

@Builder
public class WordDTOBuilder {

    @Builder.Default
    private Long id = 1L;

    @Builder.Default
    private String name = "Dog";

    @Builder.Default
    private int score = 10;

    @Builder.Default
    private boolean active = true;

    @Builder.Default
    private String category = "animal";

    public WordDTO toWordDTO() {
        return new WordDTO(id,
                name,
                score,
                active,
                category);
    }
}
