package com.example.demo.stand

import com.example.demo.domain.DomainDTO
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty

class StandDTO {
    @NotBlank
    String name
    @Valid
    @NotEmpty
    List<DomainDTO> domains = []
}
