package com.zentry.sigea.module_pago.infrastucture.database.entities;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import com.zentry.sigea.module_inscripciones.infrastructure.database.entities.InscripcionEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "pago")
@Getter
@Setter
public class PagoEntity {

    @Id
    @GeneratedValue
    @Column(
        name = "id_pago" , updatable = false , nullable = false , 
        columnDefinition = "UUID DEFAULT gen_random_uuid()"
    )
    private UUID idPago;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "inscripcion_id", referencedColumnName = "id_inscripcion")
    private InscripcionEntity inscripcion;

    @Column(nullable = false)
    private BigDecimal monto;

    @Column(nullable = false, length = 3)
    private String moneda = "PEN";

    @Column(name = "fecha_pago", nullable = false)
    private OffsetDateTime fechaPago;

    @Column(length = 120)
    private String comprobante; // opcional (p.ej. imagen o PDF)

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "metodo_id", referencedColumnName = "id_metodo")
    private MetodoPagoEntity metodoPago;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "estado_id", referencedColumnName = "id_estado")
    private EstadoPagoEntity estadoPago;

    @Column(name = "referencia_ext", length = 120)
    private String referenciaExterna; // ID de pago en Mercado Pago

    // URL de Mercado Libre/Mercado Pago generada al crear el pago
    @Column(name = "url_mercado_pago", length = 512)
    private String urlMercadoPago;

}
