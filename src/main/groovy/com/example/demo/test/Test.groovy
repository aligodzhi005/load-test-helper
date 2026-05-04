package com.example.demo.test

import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GenerationType
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import jakarta.persistence.CollectionTable
import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table

import java.time.LocalDateTime

@Entity
@JsonPropertyOrder(["id", "createdAt", "endedAt", "stand", "duration", "server", "profile", "status", "testType", "pageId"])
@Table(name="test", schema="load_test_helper")
class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name="id", nullable=false)
    Integer id

    @JsonFormat(pattern = "dd-MM-yyyy'T'HH:mm:ss")
    @Column(name="createdAt", nullable=false)
    LocalDateTime createdAt

    @JsonFormat(pattern = "dd-MM-yyyy'T'HH:mm:ss")
    @Column(name="endedAt", nullable=false)
    LocalDateTime endedAt

    @Column(name="stand", nullable=false)
    String stand

    @Column(name="duration", nullable=false)
    Integer duration

    @ElementCollection
    @CollectionTable(name="test_server_profile", schema="load_test_helper")
    @Column(name="server")
    List<String> server

    @ElementCollection
    @CollectionTable(name="test_server_profile", schema="load_test_helper")
    @Column(name="profile")
    List<String> profile

    @Column(name="status")
    @Enumerated(EnumType.STRING)
    Status status
    @Column(name="testType")
    @Enumerated(EnumType.STRING)
    TestType testType

    String pageId

    Boolean isReportCollected

    @Column(name="releaseKey")
    String releaseKey

    Test() {}

    Test(String stand, Integer duration, List<String> server, List<String> profile, TestType testType, String pageId, String releaseKey) {
        def now = LocalDateTime.now()
        this.stand = stand
        this.duration = duration
        this.server = server
        this.profile = profile
        this.createdAt = now
        this.endedAt = now.plusSeconds(duration)
        this.status = Status.valueOf("InProgress")
        this.testType = testType
        this.pageId = pageId
        this.isReportCollected = pageId != null ? false : true
        this.releaseKey = releaseKey
    }
}
