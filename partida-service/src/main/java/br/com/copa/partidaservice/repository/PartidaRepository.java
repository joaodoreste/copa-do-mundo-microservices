package br.com.copa.partidaservice.repository;

import br.com.copa.partidaservice.entity.Partida;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartidaRepository extends JpaRepository<Partida, Long> {
}