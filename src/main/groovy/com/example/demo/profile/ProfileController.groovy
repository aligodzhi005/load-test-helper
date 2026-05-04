package com.example.demo.profile

import com.example.demo.exception.BadRequestException
import com.example.demo.exception.NotFoundException
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import jakarta.persistence.*

@Validated
@RestController
@RequestMapping("/profile")
class ProfileController {
    def logger = LoggerFactory.getLogger(getClass())
    private final ProfileRepository profileRepository
    private final ProfileService profileService

    ProfileController(ProfileRepository profileRespository, ProfileService profileService) {
        this.profileRepository = profileRespository
        this.profileService = profileService
    }

    //Список профилей
    @GetMapping
    def getProfile() {
        return profileRepository.findAll()
    }

    //Поиск профиля по name
    @GetMapping("/{name}")
    def getProfileById(@PathVariable ( "name") String name, HttpServletRequest request) {
        def profile = profileRepository.findById(name) ?: false
        if(!profile)
            throw new NotFoundException("Профиль не найден - ${name}", request.method, request.requestURI)
        return profile
    }

    //Расчет профиля в зависимости от количества серверов
    @GetMapping("/profileCalculation")
    def getProfileCalculation(@RequestParam("profileName") String profileName, @RequestParam("serverCount") String cnt, HttpServletRequest request) {
        if(!cnt.isInteger())
            throw new BadRequestException(("Неверный запрос. Get-параметр serverCount != Integer"))
        Integer serverCount = cnt.toInteger()
        def profile = profileService.profileCalculation(profileName, serverCount)
        if (profile instanceof Exception)
            throw new BadRequestException(
                    "Неверный запрос. Get-параметр profileName указан неверно. Паттерн: {name} или {name}:{throughput}:{threads}:{rampUp}")
        logger.info("${request.method} ${request.requestURI}; message: ${profile}")
        return profile
    }
}
