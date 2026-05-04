package com.example.demo.components

import com.example.demo.profile.Profile
import com.example.demo.profile.ProfileRepository
import com.example.demo.server.Server
import com.example.demo.server.ServerRepository
import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class DataLoader {
    def logger = LoggerFactory.getLogger(getClass())
    private final ServerRepository serverRepository
    private final ProfileRepository profileRepository

    @Value('${serverListFile:serverList.dat}')
    private String serverListFile

    @Value('${profileListFile:profileList.dat}')
    private String profileListFile

    @Value('${dbName:load_test_helper}')
    private String dbName

    @Value('${userDir}')
    private String userDir

    DataLoader(ServerRepository serverRepository, ProfileRepository profileRepository) {
        this.serverRepository = serverRepository
        this.profileRepository = profileRepository
    }

    @PostConstruct
    private void dataLoaderServer() {
        if(!serverRepository.count()) {
            String[] serverArr = new File("${serverListFile}").text.trim()
            serverRepository.deleteAll()
            for (server in serverArr) {
                serverRepository.save(new Server(server))
            }
            logger.info("DataLoader - serverRepository --> Выполено наполнение БД после инициализации")
        } else {
            logger.info("DataLoader - serverRepository --> Наполнение БД не выполнено. БД переполнено")
        }
    }

    @PostConstruct
    private void dataLoaderProfile() {
        if(!profileRepository.count()) {
            String[] profileArr = new File("${profileListFile}").text.split("\n")
            profileRepository.deleteAll()
            for (profile in profileArr) {
                profile = profile.split(":")
                profileRepository.save(new Profile(profile[0],profile[1].toDouble(),profile[2].toInteger(), profile[3].toInteger()))
            }
            logger.info("DataLoader - profileRepository --> Выполено наполнение БД после инициализации")
        } else {
            logger.info("DataLoader - profileRepository --> Наполнение БД не выполнено. БД переполнено")
        }
    }
}
