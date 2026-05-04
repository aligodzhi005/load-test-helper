package com.example.demo.exception

class NotFoundException  extends RuntimeException{
    NotFoundException(String message) {
        super(message)
    }
}
