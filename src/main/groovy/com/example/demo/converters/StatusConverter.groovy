//package com.example.demo.converters
//
//import com.example.demo.test.Test
//import jakarta.persistence.AttributeConverter
//import jakarta.persistence.Converter
//
//@Converter(autoApply=true)
//class StatusConverter implements AttributeConverter<Test.Status, String> {
//
//    @Override
//    String convertToDatabaseColumn(Test.Status status) {
//        if(status == null) return null
//        return status.name()
//    }
//
//    @Override
//    Test.Status convertToEntityAttribute(String value) {
//        if(value == null) return null
//        return Test.Status.valueOf(value)
//    }
//}
