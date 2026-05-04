package com.example.demo.requestBuilder

import com.example.demo.exception.BadRequestException
import com.example.demo.exception.NotFoundException
import com.example.demo.test.TestRepository
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/report")
class ReportController {

    private static final def logger = LoggerFactory.getLogger(ReportController)

    private final TestRepository testRepository
    private final ReportService reportService

    ReportController(TestRepository testRepository, ReportService reportService) {
        this.testRepository = testRepository
        this.reportService = reportService
    }

    /**
     * Отправить отчёт по существующему тесту.
     * POST /report/{id}
     * Опциональный параметр pageId переопределяет значение из конфига.
     *
     * Пример: POST /report/42?pageId=9247724202
     */
    @PostMapping("/{id}")
    def sendReport(@PathVariable("id") String id,
                   @RequestParam(value = "pageId", required = false) String pageId,
                   HttpServletRequest request) {

        if (!id.isInteger())
            throw new BadRequestException("Неверный запрос. Path-параметр id != Integer")

        def testOpt = testRepository.findById(id.toInteger())
        if (!testOpt || !testOpt.isPresent())
            throw new NotFoundException("Тест не найден - ${id}")

        def result = reportService.sendReport(testOpt.get(), pageId)
        logger.info("${request.method} ${request.requestURI}; message: Отчёт отправлен для теста ${id}")
        return ResponseEntity.ok(result)
    }

    /**
     * Превью JSON без отправки — удобно для отладки.
     * POST /report/preview/{id}
     */
    @PostMapping("/preview/{id}")
    def previewReport(@PathVariable("id") String id,
                      @RequestParam(value = "pageId", required = false) String pageId,
                      HttpServletRequest request) {

        if (!id.isInteger())
            throw new BadRequestException("Неверный запрос. Path-параметр id != Integer")

        def testOpt = testRepository.findById(id.toInteger())
        if (!testOpt || !testOpt.isPresent())
            throw new NotFoundException("Тест не найден - ${id}")

        def dto = reportService.buildRequest(testOpt.get(), pageId)
        logger.info("${request.method} ${request.requestURI}; message: Preview для теста ${id}")
        return ResponseEntity.ok(dto)
    }
}
