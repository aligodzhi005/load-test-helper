package com.example.demo.test

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

import java.time.LocalDateTime

@Repository
interface TestRepository extends CrudRepository<Test, Integer> {
    Iterable<Test> findAllByEndedAtBefore(LocalDateTime dateAfter)
}
