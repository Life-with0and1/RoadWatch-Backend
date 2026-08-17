package com.example.post.dto;

import com.example.post.model.MediaType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostMediaResponseDTO {

    private String mediaUrl;
    private String publicId;
    private MediaType mediaType;
}