package br.com.grpc.iot.model;

import javax.persistence.*; // Importe do pacote javax.persistence para Hibernate 5.x
import java.io.Serializable;
import java.time.Instant; // Para mapear o Timestamp do Protocol Buffers

@Entity // Indica que esta classe é uma entidade JPA
@Table(name = "dados_sensor") // Nome da tabela no banco de dados
public class DadosSensor implements Serializable {

    @Id // Chave primária auto-gerada
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private float temperatura;

    @Column(nullable = false)
    private float umidade;

    @Column(nullable = false)
    private Instant timestamp; // Usaremos java.time.Instant para mapear o Timestamp

    // Um dado de sensor pertence a um sensor.
    // @ManyToOne indica o lado "muitos" do relacionamento.
    // @JoinColumn especifica a coluna na tabela 'dados_sensor' que será a chave estrangeira
    // para a tabela 'sensores'.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sensor_id", nullable = false) // garante que um dado sempre pertença a um sensor
    private Sensor sensor;

    public DadosSensor() {}

    public DadosSensor(float temperatura, float umidade, Instant timestamp, Sensor sensor) {
        this.temperatura = temperatura;
        this.umidade = umidade;
        this.timestamp = timestamp;
        this.sensor = sensor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public float getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(float temperatura) {
        this.temperatura = temperatura;
    }

    public float getUmidade() {
        return umidade;
    }

    public void setUmidade(float umidade) {
        this.umidade = umidade;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    @Override
    public String toString() {
        return "DadosSensor{" +
               "id=" + id +
               ", temperatura=" + temperatura +
               ", umidade=" + umidade +
               ", timestamp=" + timestamp +
               '}';
    }
}
