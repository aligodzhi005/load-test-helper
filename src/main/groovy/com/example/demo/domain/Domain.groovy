package com.example.demo.domain

import com.example.demo.stand.Stand
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.Column
import jakarta.persistence.GeneratedValue
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.persistence.*
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@Entity
@JsonPropertyOrder(["stand", "name", "url"])
@Table(name="domain", schema="load_test_helper")
class Domain {
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name="id",nullable=false)
    Integer id

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "stand_id")
    Stand stand

    @Column(name = "name", nullable = false)
    String name

    @Column(name = "url", nullable = false)
    String url
}

