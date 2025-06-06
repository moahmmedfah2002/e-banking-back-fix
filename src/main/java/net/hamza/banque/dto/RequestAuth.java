package net.hamza.banque.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.hamza.banque.model.Role;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestAuth {
    private String username;
    private String password;
    private Role role=Role.CLIENT;
    private String firstName;
    private String lastName;
    private String phone;


}
