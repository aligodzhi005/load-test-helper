package com.example.demo.server

import jakarta.validation.constraints.NotBlank

class ServerDTO {
    @NotBlank
    String name
    Boolean free = true
}
