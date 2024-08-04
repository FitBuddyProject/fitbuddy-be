package com.fitbuddy.service.config;

import org.springframework.restdocs.headers.HeaderDescriptor;
import org.springframework.restdocs.headers.RequestHeadersSnippet;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;
import org.springframework.restdocs.payload.*;
import org.springframework.restdocs.request.*;
import org.springframework.restdocs.snippet.Snippet;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;

public class RestDocument {
    public static RequestFieldsSnippet simpleRequestFields(Map<String, String> request) {
        List<FieldDescriptor> linkedRequest = new LinkedList<>();
        for(String key : request.keySet()) {
            linkedRequest.add(PayloadDocumentation.subsectionWithPath(key).description(request.get(key)));
        }

        return   relaxedRequestFields(linkedRequest);
    }
    public static ResponseFieldsSnippet simpleResponseFields(Map<String,String> response) {
        List<FieldDescriptor> linkedResponse = new LinkedList<>();
        for(String key : response.keySet()) {
            linkedResponse.add(PayloadDocumentation.subsectionWithPath(key).description(response.get(key)));
        }
        return relaxedResponseFields(linkedResponse);
    }
    public static PathParametersSnippet simplePathParameters(Map<String, String> path) {
        List<ParameterDescriptor> linkedParameter = new LinkedList<>();
        for(String key : path.keySet()) {
            linkedParameter.add(parameterWithName(key).description(path.get(key)));
        }

        return pathParameters(linkedParameter);
    }
    public static RequestHeadersSnippet simpleHeaders(Map<String, String> header) {
        List<HeaderDescriptor> linkedHeader = new LinkedList();

        for( String key : header.keySet()) {
            linkedHeader.add(headerWithName(key).description(header.get(key)));
        }

        return requestHeaders(linkedHeader);
    }
    public static RequestPartsSnippet simpleRqPartFields(Map<String, String> part) {
        List<RequestPartDescriptor> linkedField = new LinkedList<>();
        for (String key : part.keySet()) {
            linkedField.add(partWithName(key).description(part.get(key)));
        }
        return relaxedRequestParts(linkedField);
    }
    public static QueryParametersSnippet simpleQuery (Map<String, String> query) {
        List<ParameterDescriptor> linkedQuery = new LinkedList<>();

        for(String key : query.keySet()) {
            linkedQuery.add(parameterWithName(key).description(query.get(key)));
        }

        return relaxedQueryParameters(linkedQuery);
    }


    public static Builder build(String name) {

        return new Builder(name);

    }

    public static class Builder {
        public Builder(String name) {
            this.name = name;
        }

        private String name;
        private RequestFieldsSnippet rqSnippet = null;
        private ResponseFieldsSnippet rsSnippet = null;
        private PathParametersSnippet pathSnippet = null;
        private RequestPartFieldsSnippet partSnippet = null;
        private RequestHeadersSnippet headersSnippet = null;

        private QueryParametersSnippet querySnippet = null;

        public Builder rqSnippet(RequestFieldsSnippet rqSnippet){
            this.rqSnippet = rqSnippet;
            return this;
        }
        public Builder rsSnippet(ResponseFieldsSnippet rsSnippet){
            this.rsSnippet = rsSnippet;
            return this;
        }
        public Builder pathSnippet(PathParametersSnippet pathSnippet){
            this.pathSnippet = pathSnippet;
            return this;
        }
        public Builder partSnippet(RequestPartFieldsSnippet partSnippet){
            this.partSnippet = partSnippet;
            return this;
        }
        public Builder headersSnippet(RequestHeadersSnippet headersSnippet){
            this.headersSnippet = headersSnippet;
            return this;
        }

        public Builder querySnippet(QueryParametersSnippet query){
            this.querySnippet = query;
            return this;
        }

        private OperationRequestPreprocessor simplePreProcessRequest() {
            return  preprocessRequest(
                    modifyUris() // (1)
                            .scheme("https")
                            .host("docs.api.com")
                            .removePort(),
                    prettyPrint());
        }
        private OperationResponsePreprocessor simplePreProcessResponse( ){
            return preprocessResponse(prettyPrint());
        }

        private Snippet[] simpleSnippets() {
            List<Snippet> snippet = new LinkedList<>();
            if(Objects.nonNull(rqSnippet)) snippet.add(rqSnippet);
            if(Objects.nonNull(rsSnippet)) snippet.add(rsSnippet);
            if(Objects.nonNull(pathSnippet)) snippet.add(pathSnippet);
            if(Objects.nonNull(partSnippet)) snippet.add(partSnippet);
            if(Objects.nonNull(headersSnippet)) snippet.add(headersSnippet);
            if(Objects.nonNull(querySnippet)) snippet.add(querySnippet);

            return snippet.toArray(Snippet[]::new);
        }
        public RestDocumentationResultHandler build() {
            return document(
                    this.name,
                    this.simplePreProcessRequest(),
                    this.simplePreProcessResponse(),
                    this.simpleSnippets()
            );
        }
    }
}
