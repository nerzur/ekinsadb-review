package cu.havanaclub.ekinsadbreview.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "EK_Pesajeslinea")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EkPesajesLinea {

    @Id
    @Basic
    @Column(name = "Uuid", nullable = false, length = 36)
    private String uuid;

    @Basic
    @Column(name = "Numero_Lote", nullable = false, length = 20)
    private String numeroLote;

    @Basic
    @Column(name = "Fecha", nullable = false)
    private Timestamp fecha;

    @Basic
    @Column(name = "Tag", nullable = false, length = 16)
    private String tag;

    @Basic
    @Column(name = "Peso", nullable = false)
    private int peso;

    @Basic
    @Column(name = "Idzona", nullable = false)
    private int idZona;

    @Basic
    @Column(name = "Folio", nullable = true)
    private String folio;
}
