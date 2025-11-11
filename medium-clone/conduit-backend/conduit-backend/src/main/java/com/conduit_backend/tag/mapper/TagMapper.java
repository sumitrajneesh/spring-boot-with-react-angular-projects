package com.conduit_backend.tag.mapper;

import com.conduit_backend.tag.dto.TagResponse;
import com.conduit_backend.tag.entity.Tag;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TagMapper {

    public TagResponse toTagResponse(List<Tag> tags) {
        return TagResponse.builder()
                .tags(tags.stream().map(Tag::getName).collect(Collectors.toList()))
                .build();
    }
}
