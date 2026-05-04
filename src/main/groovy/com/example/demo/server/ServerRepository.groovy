package com.example.demo.server

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ServerRepository extends CrudRepository<Server, String> {
    Iterable<Server> findByFree(Boolean free)
}
