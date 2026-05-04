//package com.example.demo.converters
//
//import com.example.demo.test.Test
//import com.example.demo.test.Test.TestType
//import jakarta.persistence.AttributeConverter
//import jakarta.persistence.Converter
//
//@Converter(autoApply = true)
//class TestTypeConverter implements AttributeConverter<Test.TestType, String> {
//    @Override
//    String convertToDatabaseColumn(TestType type) {
//        if (type == null) return null
//        return type.name()
//    }
//
//    @Override
//    TestType convertToEntityAttribute(String value) {
//        if (value == null) return null
//        return TestType.valueOf(value)
//    }
//}
