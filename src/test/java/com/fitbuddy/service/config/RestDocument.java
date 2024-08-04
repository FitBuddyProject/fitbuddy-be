package com.fitbuddy.service.config;

import org.springframework.restdocs.headers.RequestHeadersSnippet;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;
import org.springframework.restdocs.payload.*;
import org.springframework.restdocs.request.PathParametersSnippet;
import org.springframework.restdocs.snippet.Snippet;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

public class RestDocument {
    public static RequestFieldsSnippet simpleRequestFields(Map<String, String> request) {
        List<FieldDescriptor> linkedRequest = new LinkedList<>();
        for(String key : request.keySet()) {
            linkedRequest.add(PayloadDocumentation.subsectionWithPath(key).description(request.get(key)));
        }

        return   relaxedRequestFields(linkedRequest.toArray(FieldDescriptor[]::new));
    }
    public static ResponseFieldsSnippet simpleResponseFields(Map<String,String> response) {
        List<FieldDescriptor> linkedResponse = new LinkedList<>();
        for(String key : response.keySet()) {
            linkedResponse.add(PayloadDocumentation.subsectionWithPath(key).description(response.get(key)));
        }
        return relaxedResponseFields(linkedResponse.toArray(FieldDescriptor[]::new));
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
