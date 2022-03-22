package com.example.springbootpracticeproject.controller;

import com.example.springbootpracticeproject.dto.PostRequestDto;
import com.example.springbootpracticeproject.dto.PostResponseDto;
import com.example.springbootpracticeproject.exception.InputValidator;
import com.example.springbootpracticeproject.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/post")
    public PostResponseDto createPost(@RequestBody @Valid PostRequestDto postDto,
                                      BindingResult bindingResult) {
        InputValidator.BadRequestHandler(bindingResult);
        return postService.createPost(postDto);
    }

    @GetMapping("/post/{id}")
    public PostResponseDto getPost(@PathVariable Long id) {
        return postService.getPost(id);
    }

    @GetMapping("/posts")
    public List<PostResponseDto> getAllPost() {
        return postService.getAllPost();
    }

    @DeleteMapping("/post/{id}")
    public void deletePost(@PathVariable Long id) {
        postService.deletePost(id);
    }
}
