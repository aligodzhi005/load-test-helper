package com.example.demo.requestBuilder

import com.example.demo.test.Test
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import com.example.demo.test.TestType
import org.apache.hc.client5.http.impl.classic.HttpClients
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder
import org.apache.hc.core5.ssl.SSLContextBuilder
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory

import java.time.format.DateTimeFormatter

@Service
class ReportService {

    private static final def logger = LoggerFactory.getLogger(ReportService)
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")

    @Value('${report.module.url:http://localhost:8080/api/echo}')
    private String reportUrl

    RestTemplate restTemplate() {
        def sslContext = SSLContextBuilder.create()
                .loadTrustMaterial(null, (chain, authType) -> true) // доверяем всем сертификатам
                .build()

        def connectionManager = PoolingHttpClientConnectionManagerBuilder.create()
                .setSSLSocketFactory(
                        SSLConnectionSocketFactoryBuilder.create()
                                .setSslContext(sslContext)
                                .setHostnameVerifier((hostname, session) -> true) // отключаем проверку hostname
                                .build()
                )
                .build()

        def httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .build()

        return new RestTemplate(new HttpComponentsClientHttpRequestFactory(httpClient))
    }

    private final RestTemplate restTemplate = new RestTemplate()

    private final ObjectMapper objectMapper = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)

    /**
     * Конфигурация датасетов: TestType → список файлов.
     *
     * Каждая запись — Map с ключами:
     *   file  (String)  — имя JSON-файла
     *   title (Boolean) — добавлять ли title (= имя stand) в serviceItem
     *   names (Boolean) — включать ли names (профили) или передавать пустой список
     *
     * Чтобы добавить новый датасет — просто добавьте строку в нужный список.
     * Чтобы добавить новый TestType — добавьте новый ключ в карту.
     */
    private static final Map<TestType, List<Map>> DATASET_CONFIG = [
        (TestType.Maximum)    : [
            [file: "services.json", names: true],
            [file: "ingressEgress.json", names: false]
        ],
        (TestType.Reliability) : [
            [file: "services.json", names: true],
            [file: "comTest.json", names: false],
            [file: "ingressEgress.json", names: false]
        ],
        (TestType.Negative) : [
                [file: "services.json", names: true],
                [file: "compTest.json", names: false],
                [file: "ingressEgress.json", names: false]
        ]
    ]

    // ─────────────────────────────────────────────
    // Публичные методы
    // ─────────────────────────────────────────────

    String sendReport(Test test, String pageId = null) {
        ReportRequestDTO request = buildRequest(test, pageId)
        String json = objectMapper.writeValueAsString(request)
        logger.info("ReportService: sendReport; testId=${test.id}; payload=${json}")
        test.isReportCollected = true
        return post(json)
    }

    ReportRequestDTO buildRequest(Test test, String pageId = null) {
        return new ReportRequestDTO(
                timeFrom: test.createdAt.format(FORMATTER),
                timeTo  : test.endedAt.format(FORMATTER),
                pageId  : pageId ?: test.pageId,
                datasets: buildDatasets(test)
        )
    }

    // ─────────────────────────────────────────────
    // Приватные методы
    // ─────────────────────────────────────────────

    /**
     * Строит список датасетов по конфигурации DATASET_CONFIG.
     * Количество датасетов не ограничено — берётся из конфига для данного TestType.
     */
    private List<ReportDatasetDTO> buildDatasets(Test test) {
        List<Map> configs = DATASET_CONFIG[test.testType]

        if (!configs) {
            logger.warn("ReportService: buildDatasets; нет конфигурации для TestType=${test.testType}")
            return []
        }

        List<String> profileNames = extractProfileNames(test.profile)

        return configs.collect { Map cfg ->
            new ReportDatasetDTO(
                file    : cfg.file,
                services: [new ReportServiceItemDTO(
                    standId: test.stand,
                    names  : cfg.names ? profileNames : []
                )]
            )
        }
    }

    /**
     * Извлекает имена профилей из строк "name:throughput:threads:rampUp".
     */
    private static List<String> extractProfileNames(List<String> rawProfiles) {
        if (!rawProfiles) return []
        return rawProfiles.collect { String p ->
            p.contains(":") ? p.split(":")[0] : p
        }
    }

    private String post(String json) {
        HttpHeaders headers = new HttpHeaders()
        headers.setContentType(MediaType.APPLICATION_JSON)
        HttpEntity<String> entity = new HttpEntity<>(json, headers)
        try {
            def response = restTemplate.postForEntity(reportUrl, entity, String)
            logger.info("ReportService: post; status=${response.statusCode}")
            return response.body
        } catch (ex) {
            logger.error("ReportService: post; Ошибка при отправке запроса: ${ex.message}")
            throw ex
        }
    }
}
