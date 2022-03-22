package com.example.springbootpracticeproject.exception;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.List;

public class InputValidator {

    public static void BadRequestHandler(BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            List<ObjectError> errorList = bindingResult.getAllErrors();
            for(ObjectError error : errorList) {
                //돌면서 에러를 전부 모아서 에러 메세지 리스트로 응답식으로 수정
                System.out.println(error.getDefaultMessage());
                throw new IllegalArgumentException(error.getDefaultMessage());
            }
        }
    }
}
