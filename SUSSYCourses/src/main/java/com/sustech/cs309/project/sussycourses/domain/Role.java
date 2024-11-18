package com.sustech.cs309.project.sussycourses.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Getter
@Setter
@ToString
@Entity
@Table(name = "role")

public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roleId;

    @Column(nullable = false, unique = true)
    private String roleName;

    @OneToMany(mappedBy = "role")
    private List<WebAppUser> webAppUsers;

    @Override
    public String getAuthority() {
        return "ROLE_" + roleName;
    }
}
