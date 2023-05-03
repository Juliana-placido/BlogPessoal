package com.generation.security;

import com.generation.model.Usuario;
import com.generation.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> usuario = usuarioRepository.findByUsuario(username);
        if (usuario.isPresent()) {
            return new UserDetailsImpl(usuario.get());
        }
        throw new UsernameNotFoundException("Usuário não encontrado");
    }



    public static class UserDetailsImpl implements UserDetails {

        private Long id;
        private String username;
        private String password;
        private Collection<? extends GrantedAuthority> authorities;

        public UserDetailsImpl(Usuario usuario) {
            this.id = usuario.getId();
            this.username = usuario.getUsuario();
            this.password = usuario.getSenha();
            this.authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return authorities;
        }

        @Override
        public String getPassword() {
            return password;
        }

        @Override
        public String getUsername() {
            return username;
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }
    }
}
