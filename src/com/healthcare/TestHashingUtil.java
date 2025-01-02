package com.healthcare;

import com.healthcare.utils.HashingUtil;

public class TestHashingUtil {
    public static void main(String[] args) {
        String password = "securePassword123";
        String hashedPassword = HashingUtil.hashPassword(password);

        System.out.println("Password: " + password);
        System.out.println("Hashed Password: " + hashedPassword);
    }
}
