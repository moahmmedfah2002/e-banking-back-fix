package net.hamza.banque.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ResponseTransaction {
    private boolean status;
    private String message;
}
