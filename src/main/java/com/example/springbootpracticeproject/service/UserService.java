package com.example.springbootpracticeproject.service;

import com.example.springbootpracticeproject.dto.UserDto;
import com.example.springbootpracticeproject.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.springbootpracticeproject.repository.UserRepository;

import javax.servlet.http.HttpSession;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void registerUser(UserDto userDto) {

        if(userRepository.existsByUsername(userDto.getUsername()))
            throw new IllegalArgumentException("이미 사용중인 ID입니다");

        if(userRepository.existsByEmail(userDto.getEmail()))
            throw new IllegalArgumentException("이미 등록된 이메일입니다");

        userRepository.save(User
                .builder()
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .password(userDto.getPassword()) //password encoding 아직 안함
                .build());
    }

    public void login(String username, String password, HttpSession httpSession) throws IllegalAccessException {
        if(!userRepository.existsByUsernameAndPassword(username, password)) {
            throw new IllegalAccessException("인증 실패");
        }
        httpSession.setAttribute("user", userRepository.findByUsername(username));
    }
}
