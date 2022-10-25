package ua.nix.onishchenko.mfc.rest.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public final class UserDTO {
    private UUID userId;
    private String name = "";
    private String email = "";
    private String password = "";
}