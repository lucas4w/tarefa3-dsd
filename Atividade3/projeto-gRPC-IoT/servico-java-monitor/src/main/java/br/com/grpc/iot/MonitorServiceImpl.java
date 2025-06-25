package br.com.grpc.iot;

import br.com.grpc.iot.model.DadosSensor;
import br.com.grpc.iot.model.Sensor;
import br.com.grpc.iot.model.Usuario;
import com.google.protobuf.Timestamp;
import io.grpc.stub.StreamObserver;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

// A lógica do nosso serviço. Estendemos a classe base gerada pelo gRPC.
public class MonitorServiceImpl extends MonitorServiceGrpc.MonitorServiceImplBase {

    private final EntityManagerFactory emf;

    public MonitorServiceImpl(EntityManagerFactory emf) {
        this.emf = emf;
        System.out.println("✅ MonitorServiceImpl inicializado com EntityManagerFactory.");
    }

    @Override
    public void registrarUsuario(RegistrarUsuarioRequest request, StreamObserver<RegistrarUsuarioResponse> responseObserver) {
        EntityManager em = emf.createEntityManager();
         em.getTransaction().begin();
        RegistrarUsuarioResponse.Builder responseBuilder = RegistrarUsuarioResponse.newBuilder();

        try {
            // Tenta encontrar um usuário com o mesmo email primeiro
            Usuario existingUser = em.createQuery("SELECT u FROM Usuario u WHERE u.email = :email", Usuario.class)
                                     .setParameter("email", request.getEmail())
                                     .getSingleResult();
            
            responseBuilder.setMensagem("Usuário com email '" + request.getEmail() + "' já existe.")
                           .setSucesso(true)
                           .setUsuarioId(existingUser.getId());
            System.out.println("❌ Usuário com email " + request.getEmail() + " já existe. ID: " + existingUser.getId());

        } catch (NoResultException e) {
            // Se não encontrou, pode criar um novo usuário
            Usuario novoUsuario = new Usuario(request.getEmail(), request.getNome()); // <-- Construtor Usuario(String, String)
            em.persist(novoUsuario);
            em.getTransaction().commit();
            
            responseBuilder.setMensagem("Usuário '" + request.getNome() + "' registrado com sucesso!")
                           .setSucesso(true)
                           .setUsuarioId(novoUsuario.getId());
            System.out.println("✅ Usuário registrado: " + novoUsuario);

        } catch (PersistenceException | IllegalStateException e) {
            System.err.println("Erro de persistência ao registrar usuário: " + e.getMessage());
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            responseBuilder.setMensagem("Erro ao registrar usuário: " + e.getMessage())
                           .setSucesso(false)
                           .setUsuarioId(0);
        } catch (Exception e) {
            System.err.println("Erro inesperado ao registrar usuário: " + e.getMessage());
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            responseBuilder.setMensagem("Erro interno ao registrar usuário.")
                           .setSucesso(false)
                           .setUsuarioId(0);
        } finally {
            em.close();
            responseObserver.onNext(responseBuilder.build());
            responseObserver.onCompleted();
        }
    }

    @Override
    public void registrarSensor(RegistrarSensorRequest request, StreamObserver<RegistrarSensorResponse> responseObserver) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        RegistrarSensorResponse.Builder responseBuilder = RegistrarSensorResponse.newBuilder();

        try {
            Usuario usuario = em.find(Usuario.class, request.getUsuarioId());

            if (usuario == null) {
                em.getTransaction().rollback();
                responseBuilder.setMensagem("Usuário com ID " + request.getUsuarioId() + " não encontrado.")
                               .setSucesso(false)
                               .setSensorId("");
                System.out.println("❌ Usuário com ID " + request.getUsuarioId() + " não encontrado para registrar sensor.");
                return;
            }

            try {
                Sensor existingSensor = em.createQuery(
                        "SELECT s FROM Sensor s WHERE s.usuario = :usuario AND s.nome = :nome", Sensor.class)
                        .setParameter("usuario", usuario)
                        .setParameter("nome", request.getNome())
                        .getSingleResult();
                
                responseBuilder.setMensagem("Sensor com nome '" + request.getNome() + "' já existe para o usuário.")
                               .setSucesso(true)
                               .setSensorId(existingSensor.getSensorId());
                System.out.println("❌ Sensor com nome '" + request.getNome() + "' já existe para o usuário " + usuario.getEmail() + ". ID: " + existingSensor.getSensorId()); // <-- getEmail() é aqui
            } catch (NoResultException ignored) {
                Sensor novoSensor = new Sensor(request.getNome(), request.getDescricao(), usuario);
                em.persist(novoSensor);
                em.getTransaction().commit();
                
                responseBuilder.setMensagem("Sensor '" + request.getNome() + "' registrado com sucesso!")
                               .setSucesso(true)
                               .setSensorId(novoSensor.getSensorId());
                System.out.println("✅ Sensor registrado: " + novoSensor + " para o usuário: " + usuario.getNome());
            }

        } catch (PersistenceException | IllegalStateException e) {
            System.err.println("Erro de persistência ao registrar sensor: " + e.getMessage());
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            responseBuilder.setMensagem("Erro ao registrar sensor: " + e.getMessage())
                           .setSucesso(false)
                           .setSensorId("");
        } catch (Exception e) {
            System.err.println("Erro inesperado ao registrar sensor: " + e.getMessage());
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            responseBuilder.setMensagem("Erro interno ao registrar sensor.")
                           .setSucesso(false)
                           .setSensorId("");
        } finally {
            em.close();
            responseObserver.onNext(responseBuilder.build());
            responseObserver.onCompleted();
        }
    }

    @Override // <-- Este @Override está na linha 117 no log, indicando o problema de assinatura
    public void enviarDadosSensor(SensorData request, StreamObserver<StatusResposta> responseObserver) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        StatusResposta.Builder responseBuilder = StatusResposta.newBuilder();

        try {
            Sensor sensor = em.find(Sensor.class, request.getSensorId());

            if (sensor == null) {
                em.getTransaction().rollback();
                responseBuilder.setMensagem("Erro: Sensor com ID " + request.getSensorId() + " não encontrado para registrar dados.")
                               .setTotalLeiturasRecebidas(0);
                System.err.println("❌ Erro: Sensor com ID " + request.getSensorId() + " não encontrado para registrar dados.");
                return;
            }

            DadosSensor novoDado = new DadosSensor();
            novoDado.setTemperatura(request.getTemperatura());
            novoDado.setUmidade(request.getUmidade());
            Instant instant = Instant.ofEpochSecond(request.getTimestamp().getSeconds(), request.getTimestamp().getNanos());
            novoDado.setTimestamp(instant);
            novoDado.setSensor(sensor);

            em.persist(novoDado);
            em.getTransaction().commit();

            String dataFormatada = DateTimeFormatter.ISO_INSTANT.withZone(ZoneId.systemDefault()).format(instant);
            System.out.printf(
                "[DADO RECEBIDO E PERSISTIDO] Sensor ID: %s | Temperatura: %.2f°C | Umidade: %.2f%% | Data: %s%n",
                request.getSensorId(),
                request.getTemperatura(),
                request.getUmidade(), 
                dataFormatada
            );
            
            responseBuilder.setMensagem("Dados do sensor recebidos e armazenados com sucesso!")
                           .setTotalLeiturasRecebidas(1);
        } catch (PersistenceException | IllegalStateException e) {
            System.err.println("Erro de persistência ao persistir dados do sensor: " + e.getMessage());
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            responseBuilder.setMensagem("Erro ao armazenar dados do sensor: " + e.getMessage())
                           .setTotalLeiturasRecebidas(0);
        } catch (Exception e) {
            System.err.println("Erro inesperado ao persistir dados do sensor: " + e.getMessage());
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            responseBuilder.setMensagem("Erro interno ao armazenar dados do sensor.")
                           .setSucesso(false) // Adicionado 'setSucesso' para a resposta do StatusResposta
                           .setTotalLeiturasRecebidas(0);
        } finally {
            em.close();
            responseObserver.onNext(responseBuilder.build());
            responseObserver.onCompleted();
        }
    }
}
