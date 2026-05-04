package com.example.demo.group

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface GroupRepository extends CrudRepository<Group, Integer> {
}
