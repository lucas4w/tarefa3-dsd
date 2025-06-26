package br.com.grpc.iot.model;

import javax.persistence.*; 
import java.util.List;
import java.io.Serializable;
import java.util.UUID; // Para gerar IDs únicos de sensor

@Entity 
@Table(name = "sensores")
public class Sensor implements Serializable {

    @Id // A chave primária será o sensor_id do Protocol Buffers
    @Column(name = "sensor_id", nullable = false, unique = true)
    private String sensorId;

    @Column(nullable = false)
    private String nome; // Nome amigável do sensor, ex: "Sensor de Temperatura da Cozinha"

    @Column(nullable = true) // Pode ser nulo se não houver descrição
    private String descricao;

    // Um sensor pertence a um usuário.
    // @ManyToOne indica o lado "muitos" do relacionamento.
    // @JoinColumn especifica a coluna na tabela 'sensores' que será a chave estrangeira
    // para a tabela 'usuarios'.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false) // Garante que um sensor sempre tenha um usuário
    private Usuario usuario;

    // Um sensor pode ter muitas leituras de dados.
    // mappedBy indica o nome do campo na classe DadosSensor que mapeia este relacionamento.
    @OneToMany(mappedBy = "sensor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DadosSensor> dados;

    // Construtor padrão (obrigatório para JPA)
    public Sensor() {
        // Gera um ID único para o sensor automaticamente ao ser criado
        this.sensorId = UUID.randomUUID().toString();
    }

    public Sensor(String nome, String descricao, Usuario usuario) {
        this(); // Chama o construtor padrão para gerar o sensorId
        this.nome = nome;
        this.descricao = descricao;
        this.usuario = usuario;
    }

    // Getters e Setters
    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<DadosSensor> getDados() {
        return dados;
    }

    public void setDados(List<DadosSensor> dados) {
        this.dados = dados;
    }

    @Override
    public String toString() {
        return "Sensor{" +
               "sensorId='" + sensorId + '\'' +
               ", nome='" + nome + '\'' +
               ", descricao='" + descricao + '\'' +
               '}';
    }
}
