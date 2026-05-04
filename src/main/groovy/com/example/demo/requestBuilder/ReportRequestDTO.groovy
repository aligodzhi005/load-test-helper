package com.example.demo.requestBuilder

/**
 * Корневой DTO для POST-запроса в модуль отчётности.
 * Поля timeFrom/timeTo берутся из Test.createdAt / Test.endedAt.
 */
class ReportRequestDTO {
    String timeFrom
    String timeTo
    String pageId
    List<ReportDatasetDTO> datasets = []
}

/**
 * Один элемент массива datasets.
 * Поле file определяется динамически через if-else по TestType.
 */
class ReportDatasetDTO {
    String file
    List<ReportServiceItemDTO> services = []
}

/**
 * Одна запись внутри services.
 * standId  — из Test.stand
 * names    — список имён профилей (часть до ":" из Test.profile)
 * title    — добавляется только для типов, где это нужно (nullable)
 */
class ReportServiceItemDTO {
    String standId
    List<String> names = []
    String title          // null → поле не попадёт в JSON (см. @JsonInclude в сервисе)
}
