package com.codingshuttle.project.airBnb.dto;

import com.codingshuttle.project.airBnb.entity.User;
import com.codingshuttle.project.airBnb.entity.enums.Gender;

import lombok.Data;


@Data
public class GuestDTO {
    private Long id;
    private String name;
    private Gender gender;
    private Integer age;
}
