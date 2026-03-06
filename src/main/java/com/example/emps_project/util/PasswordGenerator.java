package com.example.emps_project.util;

import com.example.emps_project.security.PasswordEncoder;

public class PasswordGenerator {
    public static void main(String[] args) {
        System.out.println("admin123 -> " + PasswordEncoder.encode("admin123"));
    }
}
