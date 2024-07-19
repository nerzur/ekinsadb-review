package cu.havanaclub.ekinsadbreview.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Bufferllenado")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BufferLlenado {

    @Id
    @Basic
    @Column(name = "Id")
    Integer id;

    @Basic
    @Column(name = "Tag")
    String tag;

    @Column(name = "Lote")
    String lote;

    @Column(name = "Folio")
    String folio;

    @Column(name = "Procesardespues")
    boolean procesarDespues;
}
