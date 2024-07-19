package cu.havanaclub.ekinsadbreview.repository;

import cu.havanaclub.ekinsadbreview.entity.BufferVaciado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BufferVaciadoRepository extends JpaRepository<BufferVaciado, Long> {
}
