package com.example.demo.profile

import jakarta.transaction.Transactional
import org.hibernate.Transaction
import org.springframework.stereotype.Service

@Service
class ProfileService {
    private final ProfileRepository profileRepository

    ProfileService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository
    }

    //Добавить профиль
    @Transactional()
    def addProfile(ProfileDTO profileDTO) {
        Profile profile = new Profile(
                name: profileDTO.name,
                throughput: profileDTO.throughput,
                threads: profileDTO.threads,
                rampUp: profileDTO.rampUp
        )
        profileRepository.save(profile)
    }

    //Расчет профиля в зависимости от количества серверов
    def profileCalculation(String profiles, Integer serverCount = 1) {
        try {
            def profilesArr = profiles.split(",")
            profilesArr = profilesArr.collect {it.split(":")}
            for (i in 0..profilesArr.size() - 1) {
                def params = profilesArr[i]
                //Проверка что передан профиль без параметров
                // 0 - name; 1 - throughput; 2 - threads; 3 - rampUp
                if (params.size() == 1) {
                    def profile = profileRepository.findById(params[0]).get()
                    params += (profile.throughput / serverCount).round(5)
                    params += Math.ceil(profile.threads / serverCount).toInteger()
                    params += profile.rampUp
                    profilesArr[i] = params
                } else {
                    params[1] = (params[1].toDouble() / serverCount).round(5)
                    params[2] = Math.ceil(params[2].toDouble() / serverCount).toInteger()
                    //Проверка что передан rampUp
                    params[3] = params[3]
                    profilesArr[i] = params
                }
            }
            return profilesArr
        } catch(ex) {
            return ex
        }
    }
}
