package com.example.community.controller;

import com.example.community.dto.FileDTO;
import org.springframework.stereotype.Controller;

@Controller
public class FileController {
    public FileDTO upload(){
        return new FileDTO();
    }
}
