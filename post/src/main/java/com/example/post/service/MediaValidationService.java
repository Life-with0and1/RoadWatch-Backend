package com.example.post.service;

import com.example.post.exception.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class MediaValidationService {

    private static final int MAX_FILES = 5;

    private static final int MAX_VIDEOS = 2;

    private static final long MAX_IMAGE_SIZE = 10 * 1024 * 1024; // 10 MB

    private static final long MAX_VIDEO_SIZE = 100 * 1024 * 1024; // 100 MB

    public void validate(List<MultipartFile> mediaFiles) {
        if (mediaFiles == null || mediaFiles.isEmpty()) {
            throw new BadRequestException("At least one media file is required");
        }

        if (mediaFiles.size() > MAX_FILES) {
            throw new BadRequestException("Maximum 5 media files allowed");
        }

        int videoCount = 0;

        for (MultipartFile file : mediaFiles) {
            if (file.isEmpty()) {
                throw new BadRequestException("Empty file is not allowed");
            }
            String contentType = file.getContentType();
            if (contentType == null) {
                throw new BadRequestException("Unknown file type");
            }
            if (contentType.startsWith("image/")) {
                if (file.getSize() > MAX_IMAGE_SIZE) {
                    throw new BadRequestException("Image size cannot exceed 10 MB");
                }
            } else if (contentType.startsWith("video/")) {
                videoCount++;
                if (videoCount > MAX_VIDEOS) {
                    throw new BadRequestException("Maximum 2 videos allowed");
                }

                if (file.getSize() > MAX_VIDEO_SIZE) {
                    throw new BadRequestException("Video size cannot exceed 100 MB");
                }

            } else {
                throw new BadRequestException("Only images and videos are allowed");
            }
        }
    }
}