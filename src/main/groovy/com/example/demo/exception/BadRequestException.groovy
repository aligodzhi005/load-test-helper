package com.example.demo.exception

class BadRequestException extends RuntimeException{
    BadRequestException(String message) {
        super(message)
    }
}
