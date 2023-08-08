package cu.havanaclub.ekinsadbreview.entity;

import jakarta.persistence.*;

import java.sql.Date;

@Entity
@Table(name = "EK_Pesajeslinea")
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
    private Date fecha;
    @Basic
    @Column(name = "Tag", nullable = false, length = 16)
    private String tag;
    @Basic
    @Column(name = "Peso", nullable = false)
    private int peso;
    @Basic
    @Column(name = "Idzona", nullable = false)
    private int idZona;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getNumeroLote() {
        return numeroLote;
    }

    public void setNumeroLote(String numeroLote) {
        this.numeroLote = numeroLote;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }

    public int getIdZona() {
        return idZona;
    }

    public void setIdZona(int idZona) {
        this.idZona = idZona;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EkPesajesLinea that = (EkPesajesLinea) o;

        if (peso != that.peso) return false;
        if (idZona != that.idZona) return false;
        if (uuid != null ? !uuid.equals(that.uuid) : that.uuid != null) return false;
        if (numeroLote != null ? !numeroLote.equals(that.numeroLote) : that.numeroLote != null) return false;
        if (fecha != null ? !fecha.equals(that.fecha) : that.fecha != null) return false;
        if (tag != null ? !tag.equals(that.tag) : that.tag != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = uuid != null ? uuid.hashCode() : 0;
        result = 31 * result + (numeroLote != null ? numeroLote.hashCode() : 0);
        result = 31 * result + (fecha != null ? fecha.hashCode() : 0);
        result = 31 * result + (tag != null ? tag.hashCode() : 0);
        result = 31 * result + peso;
        result = 31 * result + idZona;
        return result;
    }
}
