package com.innosistemas.security;
import com.innosistemas.repository.UsuarioRepository;
import com.innosistemas.entity.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Implementación de UserDetailsService para cargar usuarios desde la base de datos.
 * Utilizada por Spring Security para autenticación.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
        /**
         * Repositorio para acceder a los datos de los usuarios.
         */
                private final UsuarioRepository usuarioRepository;

                public UserDetailsServiceImpl(UsuarioRepository usuarioRepository) {
                        this.usuarioRepository = usuarioRepository;
                }

    /**
     * Carga los detalles de un usuario por su correo electrónico.
     * @param correo Correo del usuario
     * @return UserDetails con la información del usuario
     * @throws UsernameNotFoundException si el usuario no existe
     */
    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con el correo: " + correo));

        // Normalizar rol: eliminar espacios y pasar a mayúsculas
        String normalizedRole = usuario.getRol() != null ? usuario.getRol().trim().toUpperCase() : "";
        // Asegurar prefijo ROLE_ requerido por Spring Security al usar hasRole(...)
        String roleAuthority = normalizedRole.startsWith("ROLE_") ? normalizedRole : "ROLE_" + normalizedRole;
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(roleAuthority));

        return new org.springframework.security.core.userdetails.User(
                usuario.getCorreo(),
                usuario.getContrasenia(),
                authorities
        );
    }
}
