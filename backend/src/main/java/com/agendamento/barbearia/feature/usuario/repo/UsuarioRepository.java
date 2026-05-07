package com.agendamento.barbearia.feature.usuario.repo;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.agendamento.barbearia.feature.usuario.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Mágica do Spring: Ele cria a query de busca pelo e-mail automaticamente
    Optional<Usuario> findByEmail(String email);
}