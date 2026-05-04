package com.example.demo.scenario

import com.example.demo.group.Group
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@JsonPropertyOrder(["name", "stand", "domain", "duration", "draft", "groups"])
@Table(name="scenario", schema="load_test_helper")
class Scenario {
    @Id
    @Column(name="name", nullable=false)
    String name

    @Column(name="stand")
    String stand

    @Column(name="domain", nullable=false)
    String domain

    @Column(name="duration", nullable=false)
    String duration

    @Column(name="draft", nullable=false)
    Boolean draft

    @OneToMany(mappedBy = "scenario", cascade = CascadeType.ALL, orphanRemoval = true)
    @Column(name="groups")
    List<Group> groups = []

    Scenario() {}

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    Scenario(@JsonProperty("name") String name,
            @JsonProperty("stand") String stnad,
            @JsonProperty("domain") String domain,
            @JsonProperty("duration")  Integer duration,
            @JsonProperty("draft") Boolean draft,
            @JsonProperty("groups") List<Group> groups) {
        this.name = name
        this.stand = stand
        this.domain = domain
        this.duration = duration
        this.draft = draft
        this.groups = groups
        this.groups.forEach {it.scenario = this }
    }
}
