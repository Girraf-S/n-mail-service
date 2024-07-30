package com.solbeg.nmailservice.controller;

import com.solbeg.nmailservice.model.UserRequest;
import com.solbeg.nmailservice.model.VerifyMailRequest;
import com.solbeg.nmailservice.service.MailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mail")
@RequiredArgsConstructor
public class MailController {

    private final MailSenderService mailSenderService;

    @PostMapping("/verify")
    public void verifyEmail(@RequestBody VerifyMailRequest verifyMailRequest) {
        mailSenderService.verifyEmail(verifyMailRequest.getActivationCode(), verifyMailRequest.getEmail());
    }

    @PostMapping("/activate-link")
    public void sendUserInfoToAdmin(@RequestBody UserRequest userRequest) {
        mailSenderService.sendUserInfoToAdmin(userRequest);
    }
}
