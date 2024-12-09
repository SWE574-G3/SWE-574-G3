package com.communitter.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageDTO {
    private String base64Image; // Base64-encoded image data
    private String mimeType;   // MIME type (e.g., "image/png")
}
