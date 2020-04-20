package com.advcourse.conferenceassistant.service;

import com.advcourse.conferenceassistant.service.dto.QuestionDto;
import java.util.List;

public interface QuestionService {
    void addQuestion(QuestionDto dto);
    List<QuestionDto> getQuestionsByTopicId(long topicId, String email);
    List<QuestionDto> getTopQuestionsByTopicId(List<QuestionDto> questions);
    QuestionDto like(long questionId, long guestId);
    QuestionDto unlike(long questionId, long guestId);
    List<QuestionDto> getQuestionsByTopicIdForStaff(long topicId);
    void answerThisQuestion(long questionId);
}
