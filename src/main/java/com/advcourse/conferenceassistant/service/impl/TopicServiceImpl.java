package com.advcourse.conferenceassistant.service.impl;

import com.advcourse.conferenceassistant.model.Topic;
import com.advcourse.conferenceassistant.repository.TopicRepository;
import com.advcourse.conferenceassistant.service.TopicService;
import com.advcourse.conferenceassistant.service.dto.TopicDto;
import com.advcourse.conferenceassistant.service.dto.mapper.TopicMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class TopicServiceImpl implements TopicService {

    @Autowired
    private TopicRepository topicRepository;

    @Value("${upload.path}")
    private String path;

    @Autowired
    FileServiceImpl fileService;

    @Override
    public TopicDto findById(long id) {

        return TopicMapper.toDto(topicRepository.findById(id).get());

    }

    @Override
    public List<TopicDto> findByConfId(long confId) {
        List<Topic> byConferenceId = topicRepository.findByConferenceId(confId);
        return TopicMapper.toDtos(byConferenceId);
    }

    @Override
    public void deleteById(Long id) {
        topicRepository.deleteById(id);
    }

    @Override
    public TopicDto save(TopicDto dto) {
        Topic topic = topicRepository.saveAndFlush(TopicMapper.fromDto(dto));
        return TopicMapper.toDto(topic);


    }

    @Override
    public TopicDto update(Long topicId, TopicDto dto) {
        TopicDto topic = findById(topicId);
        topic.setDescription(dto.getDescription());
        topic.setEnd(dto.getEnd());
        topic.setSpeaker(dto.getSpeaker());
        topic.setSpeakerdesc(dto.getSpeakerdesc());
        if (dto.getSpeakerimg() != null) {
            /**
             * delete previous img
             * */
            if (topic.getSpeakerimg() != null) {
                if (fileService.deleteFileFromAWS(topic.getSpeakerimg())) {
                    log.info("File {} deleted", topic.getSpeakerimg());
                } else {
                    log.info("Failed to delete the file {}", topic.getSpeakerimg());
                }
            }
            /**
             * set new speaker img
             * */
            topic.setSpeakerimg(dto.getSpeakerimg());
        }
        topic.setStart(dto.getStart());
        topic.setTheme(dto.getTheme());
        topic.setConfId(dto.getConfId());
        return save(topic);
    }

    @Override
    public TopicDto updateStartTime(Long topicId) {
        TopicDto topic = findById(topicId);
        topic.setStart(LocalDateTime.now());
        return save(topic);
    }

    @Override
    public TopicDto updateEndTime(Long topicId) {
        TopicDto topic = findById(topicId);
        topic.setEnd(LocalDateTime.now());
        topic.setEnded(true);
        return save(topic);
    }
    @Override
    public TopicDto findActiveTopicByConfId(long confId) {
        List<Topic> byConferenceId = topicRepository.findByConferenceId(confId);
        List<TopicDto> topics = TopicMapper.toDtos(byConferenceId);
        for (TopicDto t : topics) {
            if (LocalDateTime.now().isAfter(t.getStart()) & LocalDateTime.now().isBefore(t.getEnd())) {
                log.debug("Found active topic: " + t.getId());
                return t;
            }
//            TODO: avoid this null returning if possible
        }
        log.info("No active topic found");
        return null;
    }


}

