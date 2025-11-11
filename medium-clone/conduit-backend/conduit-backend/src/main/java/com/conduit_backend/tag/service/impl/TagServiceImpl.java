package com.conduit_backend.tag.service.impl;

import com.conduit_backend.tag.dto.TagResponse;
import com.conduit_backend.tag.mapper.TagMapper;
import com.conduit_backend.tag.repository.TagRepository;
import com.conduit_backend.tag.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    @Override
    public TagResponse getAllTags() {
        return tagMapper.toTagResponse(tagRepository.findAll());
    }
}
