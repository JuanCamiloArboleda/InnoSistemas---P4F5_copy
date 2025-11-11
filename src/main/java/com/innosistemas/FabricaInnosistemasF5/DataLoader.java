package com.innosistemas.FabricaInnosistemasF5;
import com.innosistemas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/* Aquí se prueba la conexion con la base de datos ingresando un usuario*/
@Component
public class DataLoader implements CommandLineRunner {
    @Autowired
    @SuppressWarnings("unused")
    private UsuarioRepository usuarioRepository;

    @Autowired
    @SuppressWarnings("unused")
    private PasswordEncoder passwordEncoder;

    @Autowired
    @SuppressWarnings("unused")
    private DataSource dataSource;

    @Override
    public void run(String... args) throws Exception {
        /*try (Connection c = dataSource.getConnection()) {
            System.out.println("DataSource conectado: " + c.getMetaData().getURL() +
                    " - Usuario: " + c.getMetaData().getUserName());
        }
        String correo = "testest3@uni.edu";
        if (usuarioRepository.findByCorreo(correo).isEmpty()) {
            Usuario u = new Usuario();
            u.setNombres("Estudiante");
            u.setApellidos("Prueba 3");
            u.setCorreo(correo);
            u.setContrasenia(passwordEncoder.encode("Password123")); // contraseña de prueba
            u.setRol("Estudiante");; // Asignar rol de usuario estudiante
            usuarioRepository.save(u);
            System.out.println("Usuario de prueba creado: " + correo + " / Password123");
        }*/
    }
}
