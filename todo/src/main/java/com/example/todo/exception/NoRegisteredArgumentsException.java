package com.example.todo.exception;

public class NoRegisteredArgumentsException
        extends RuntimeException{

    // 기본생성자 + 에러메세지를 받는 생성자
    public NoRegisteredArgumentsException(String message) {
        super(message);
    }
}
