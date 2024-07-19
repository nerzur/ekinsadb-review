package cu.havanaclub.ekinsadbreview.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table (name = "Buffervaciado")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BufferVaciado {

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
