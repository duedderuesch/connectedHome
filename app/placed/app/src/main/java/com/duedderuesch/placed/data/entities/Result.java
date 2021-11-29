package com.duedderuesch.placed.data.entities;

public abstract class Result<T>{
    private Result(){}

    public static final class Success<T> extends Result<T> {

        public T data;

        public Success(T data) {
            this.data = data;
        }

    }

    public static final class Failure<T> extends Result<T> {
        public T statusCode;

        public Failure(T statusCode) {
            this.statusCode = statusCode;
        }
    }

    public static final class Error<T> extends Result<T> {
        public Exception exception;

        public Error(Exception exception) {
            this.exception = exception;
        }
    }
}
