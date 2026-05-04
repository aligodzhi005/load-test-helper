package com.example.demo.test

import com.example.demo.profile.ProfileService
import com.example.demo.requestBuilder.ReportService
import com.example.demo.server.ServerService



import org.springframework.transaction.annotation.Isolation
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TestService {
    def logger = LoggerFactory.getLogger(getClass())
    private final TestRepository testRepository
    private final ProfileService profileService
    private final ServerService serverService
    private final ReportService reportService

    TestService(TestRepository testRepository, ProfileService profileService, ServerService serverService, ReportService reportService) {
        this.testRepository = testRepository
        this.profileService = profileService
        this.serverService = serverService
        this.reportService = reportService
    }

    //Создать тест
    @Transactional()
    def createTest(Object test) {
        List<String> profile = []
        List<String> profileArr = profileService.profileCalculation(test["profile"])
        for (List<String> p : profileArr) {
            profile += p.join(":")
        }
        Test newTest = new Test(
                test["stand"] as String,
                test["duration"] as Integer,
                test["server"] as List<String>,
                profile,
                test["testType"] as TestType,
                test["pageId"] as String,
                test["releaseKey"] as String
        )
        newTest = testRepository.save(newTest)
        return newTest
    }

    //Изменить статус теста
    @Transactional(isolation = Isolation.SERIALIZABLE)
    def changeTestStatus(Integer id, String status) {
        try {
            Test test = testRepository.findById(id).get()
            test.status = Status.valueOf(status)
            if (status != "InProgress") {
                serverService.serverBookingCancel(test.server)
            }
            if (status == "Done" && test.isReportCollected == false) {
                reportService.sendReport(test)
                logger.info("TestService: changeTestStatus; testId=${id}; статус Done, отправка отчёта")
            }
            return true
        } catch (Exception ex) {
            logger.error("TestService: changeTestStatus; status Error; message: Ошибка ${ex.getStackTrace()}")
            return false
        }
    }
    //Удалить тест
    @Transactional(isolation = Isolation.SERIALIZABLE)
    def deleteTest(Integer id) {
        try {
            def server = testRepository.findById(id).get().server
            serverService.serverBookingCancel(server)
            testRepository.deleteById(id)
            return true
        } catch (Exception ex) {
            logger.error("TestService: deleteTest; status Error; message: Ошибка ${ex.getStackTrace()}")
            return false
        }
    }

}
