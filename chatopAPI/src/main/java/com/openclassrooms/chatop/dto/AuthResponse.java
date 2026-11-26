package com.openclassrooms.chatop.dto;

import lombok.Builder;

@Builder
public record AuthResponse(String token) {
}
