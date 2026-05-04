package com.example.demo.profile

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

class ProfileDTO {
    @NotBlank
    String name
    @NotNull
    Double throughput
    @NotNull
    @Min(1)
    Integer threads
    @NotNull
    @Min(1)
    Integer rampUp
}
