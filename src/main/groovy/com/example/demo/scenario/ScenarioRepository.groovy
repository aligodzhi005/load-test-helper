package com.example.demo.scenario

import com.example.demo.group.Group
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface ScenarioRepository extends CrudRepository<Scenario, String> {
    Iterable<Scenario> findByDraft(Boolean free)

    @Query('SELECT g FROM Scenario s JOIN s.groups g WHERE s.name = :scenarioName AND g.id = :groupId')
    Optional<Group> findGroupByScenarioNameAndGroupId(@Param('scenarioName') String scenarioName, @Param('groupId') Integer groupId)
}
