package com.example.demo.stand

import com.example.demo.domain.Domain
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface StandRepository extends CrudRepository<Stand, String>{
    @Query('Select d FROM Stand s JOIN s.domains d WHERE s.name = :standName AND d.name = :domainName')
    Optional<Domain> findStandByStandNameAndDomainName(@Param('standName') String standName, @Param('domainName') String domainName)
}
