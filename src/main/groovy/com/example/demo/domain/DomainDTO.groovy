package com.example.demo.domain

import jakarta.validation.constraints.NotBlank

class DomainDTO {
    @NotBlank
    String name
    @NotBlank
    String url
}
