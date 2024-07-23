package com.MovieFlix.demo.auth.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class User implements UserDetails{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @NotBlank(message = "The name feild is blank")
    private String name ;

    @NotBlank(message = "The username feild is blank")
   @Column(unique = true)
    private String username;


    @NotBlank(message = "The email feild is blank")
    @Column(unique = true)
    @Email(message = "please enter correct email")
    private String email;



    @NotBlank(message = "The password feild is blank")
   @Size(min=5 , message = "The password must have atleast 5 characters")
    private String password;

    @OneToOne(mappedBy = "user")
    private RefreshToken refreshToken;

   @Enumerated(EnumType.STRING)
   private  UserRole role;

   private boolean isEnabled = true;

   private boolean isAccountNonExpired = true;

    private boolean isAccountNonLocked = true;

    private boolean isCredentialsNonExpired = true;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}
