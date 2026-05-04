package com.example.demo.components


import com.example.demo.test.TestRepository
import com.example.demo.test.TestService
import org.slf4j.LoggerFactory

import java.time.LocalDateTime
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@EnableScheduling
@Component
class BackgroundTask {
    def logger = LoggerFactory.getLogger(getClass())
    private final TestRepository testRepository
    private final TestService testService

    BackgroundTask(TestRepository testRepository, TestService testService) {
        this.testRepository = testRepository
        this.testService = testService
    }

    @Scheduled(fixedRate = 60000)
    void taskCheckTest() {
        try {
            def tests = testRepository.findAllByEndedAtBefore(LocalDateTime.now())
            if (tests) {
                for (test in tests) {
                    if (testService.changeTestStatus(test.id,"Done")) {
                        logger.info("Scheduling: taskCheckTest; status: OK; message: Тест ${test.id} завершен")
                    } else {
                        logger.error("Scheduling: taskCheckTest; status: ERROR; message: Тест не найден ${test.id}")
                    }
                }
            } else {
                logger.debug("Scheduling: taskCheckTest; status: OK; message: Тест завершенные тесты отсутствуют")
            }
        } catch (ex) {
            logger.error("Scheduling: taskCheckTest; status: ERROR; message: Задача завершена с ошибкой; stackTrace: ${ex.getStackTrace()}")
        }

    }
}
