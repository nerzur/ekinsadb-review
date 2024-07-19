package cu.havanaclub.ekinsadbreview.repository;

import cu.havanaclub.ekinsadbreview.entity.BufferLlenado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BufferLlenadoRepository extends JpaRepository<BufferLlenado, Long> {
}
