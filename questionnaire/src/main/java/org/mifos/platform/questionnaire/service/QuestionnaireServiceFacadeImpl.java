/*
 * Copyright (c) 2005-2010 Grameen Foundation USA
 *  All rights reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 *
 *  See also http://www.apache.org/licenses/LICENSE-2.0.html for an
 *  explanation of the license and how it is applied.
 */

package org.mifos.platform.questionnaire.service;

import org.mifos.framework.exceptions.SystemException;
import org.mifos.platform.questionnaire.domain.QuestionnaireService;
import org.mifos.platform.questionnaire.service.dtos.EventSourceDto;
import org.mifos.platform.questionnaire.service.dtos.QuestionDto;
import org.mifos.platform.questionnaire.service.dtos.QuestionGroupDto;
import org.mifos.platform.questionnaire.service.dtos.QuestionGroupInstanceDto;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class QuestionnaireServiceFacadeImpl implements QuestionnaireServiceFacade {

    @Autowired
    private QuestionnaireService questionnaireService;

    public QuestionnaireServiceFacadeImpl(QuestionnaireService questionnaireService) {
        this.questionnaireService = questionnaireService;
    }

    @SuppressWarnings({"UnusedDeclaration"})
    public QuestionnaireServiceFacadeImpl() {
        this(null);
    }

    @Override
    public void createQuestions(List<QuestionDetail> questionDetails) throws SystemException {
        for (QuestionDetail questionDetail : questionDetails) {
            questionnaireService.defineQuestion(questionDetail);
        }
    }

    @Override
    public boolean isDuplicateQuestion(String title) {
        return questionnaireService.isDuplicateQuestionTitle(title);
    }

    @Override
    public void createQuestionGroup(QuestionGroupDetail questionGroupDetail) throws SystemException {
        questionnaireService.defineQuestionGroup(questionGroupDetail);
    }

    @Override
    public List<QuestionDetail> getAllQuestions() {
        return questionnaireService.getAllQuestions();
    }

    @Override
    public List<QuestionDetail> getAllActiveQuestions() {
        return questionnaireService.getAllActiveQuestions(null);
    }

    @Override
    public List<QuestionDetail> getAllActiveQuestions(List<Integer> excludedQuestions) {
        return questionnaireService.getAllActiveQuestions(excludedQuestions);
    }

    @Override
    public List<QuestionGroupDetail> getAllQuestionGroups() {
        return questionnaireService.getAllQuestionGroups();
    }

    @Override
    public QuestionGroupDetail getQuestionGroupDetail(Integer questionGroupId) throws SystemException {
        return questionnaireService.getQuestionGroup(questionGroupId);
    }

    @Override
    public QuestionDetail getQuestionDetail(Integer questionId) throws SystemException {
        return questionnaireService.getQuestion(questionId);
    }

    @Override
    public List<EventSourceDto> getAllEventSources() {
        return questionnaireService.getAllEventSources();
    }

    @Override
    public void saveResponses(QuestionGroupDetails questionGroupDetails) {
        questionnaireService.saveResponses(questionGroupDetails);
    }

    @Override
    public void validateResponses(List<QuestionGroupDetail> questionGroupDetails) {
        questionnaireService.validateResponses(questionGroupDetails);
    }

    @Override
    public List<QuestionGroupDetail> getQuestionGroups(String event, String source) throws SystemException {
        return questionnaireService.getQuestionGroups(getEventSource(event, source));
    }

    @Override
    public List<QuestionGroupInstanceDetail> getQuestionGroupInstances(Integer entityId, String event, String source) {
        return questionnaireService.getQuestionGroupInstances(entityId, getEventSource(event, source), false, false);
    }

    @Override
    public List<QuestionGroupInstanceDetail> getQuestionGroupInstancesWithUnansweredQuestionGroups(Integer entityId, String event, String source) {
        return questionnaireService.getQuestionGroupInstances(entityId, getEventSource(event, source), true, true);
    }

    @Override
    public QuestionGroupInstanceDetail getQuestionGroupInstance(Integer questionGroupInstanceId) {
        return questionnaireService.getQuestionGroupInstance(questionGroupInstanceId);
    }

    @Override
    public Integer createQuestionGroup(QuestionGroupDto questionGroupDto) throws SystemException {
        return questionnaireService.defineQuestionGroup(questionGroupDto);
    }

    @Override
    public List<String> getAllCountriesForPPI() {
        return questionnaireService.getAllCountriesForPPI();
    }

    @Override
    public void uploadPPIQuestionGroup(String country) {
        questionnaireService.uploadPPIQuestionGroup(country);
    }

    @Override
    public Integer saveQuestionGroupInstance(QuestionGroupInstanceDto questionGroupInstanceDto) {
        return questionnaireService.saveQuestionGroupInstance(questionGroupInstanceDto);
    }

    @Override
    public Integer getSectionQuestionId(String sectionName, Integer questionId, Integer questionGroupId) {
        return questionnaireService.getSectionQuestionId(sectionName, questionId, questionGroupId);
    }

    @Override
    public Integer createQuestion(QuestionDto questionDto) {
        return questionnaireService.defineQuestion(questionDto);
    }

    private EventSourceDto getEventSource(String event, String source) {
        return new EventSourceDto(event, source, String.format("%s.%s", event, source));
    }
}