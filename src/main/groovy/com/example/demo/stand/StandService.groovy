package com.example.demo.stand

import com.example.demo.domain.Domain
import com.example.demo.domain.DomainDTO
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class StandService {
    def logger = LoggerFactory.getLogger(getClass())
    private final StandRepository standRepository

    StandService(StandRepository standRepository) {
        this.standRepository = standRepository
    }

    //Добавить стенд
    @Transactional()
    def addDomain(StandDTO standDTO) {
        def stand = new Stand(
                name: standDTO.name
        )
        for (domainDTO in standDTO.domains) {
            def existingStand = stand.domains.find {it.name == domainDTO.name}
            if (existingStand) {
                logger.warn(
                        "DomainService: addStand; status: WARN; message: Домен ${domainDTO.name} существует в ${stand.name}")
                continue
            }
        def domain = new Domain(
                stand: stand,
                name: domainDTO.name,
                url: domainDTO.url
        )
        stand.domains.add(domain)
        }
        return standRepository.save(stand)
    }

    //Добавить / Редактировать домен
    @Transactional()
    def addDomain(DomainDTO domainDTO, Stand stand) {
        def domain = standRepository.findStandByStandNameAndDomainName(stand.name, domainDTO.name) ?: null
        if (domain) {
            domain = domain.get()
            domain.url = domainDTO.url
            return standRepository.save(stand)
        }
        domain = new Domain(
                stand: stand,
                name: domainDTO.name,
                url: domainDTO.url
        )
        stand.domains.add(domain)
        return standRepository.save(stand)
    }
}
