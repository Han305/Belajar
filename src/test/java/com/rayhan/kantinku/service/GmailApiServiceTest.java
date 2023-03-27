package com.rayhan.kantinku.service;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class GmailApiServiceTest {
    @Autowired private GmailApiService gmailApiService;

    @Test
    public void testKirimEmail() {
        String from = "aplikasi-kantinku@yopmail.com";
        String to = "testkirimgmail@yopmail.com";
        String subject = "Percobaan Kirim Email dengan Gmail API";
        String content = "<h1>Halo User</h1> <a>Selamat siang</a>, <i>Test user</i>";

        gmailApiService.kirimEmail(from, to, subject, content);
    }
}
