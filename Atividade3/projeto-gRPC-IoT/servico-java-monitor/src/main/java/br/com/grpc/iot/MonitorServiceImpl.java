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
import javax.swing.text.DefaultStyledDocument;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import java.util.List;
import java.util.Collections; // Para retornar listas vazias de forma segura

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

    @Override 
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
                           .setSucesso(false)
                           .setTotalLeiturasRecebidas(0);
        } finally {
            em.close();
            responseObserver.onNext(responseBuilder.build());
            responseObserver.onCompleted();
        }
    }

    @Override
    public void getUser(UserData request, StreamObserver<UserResponse> responseObserver) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin(); // Inicia transação para garantir contexto consistente
        UserResponse.Builder responseBuilder = UserResponse.newBuilder();

        try {
            Usuario usuario = em.createQuery("SELECT u FROM Usuario u WHERE u.email = :email", Usuario.class)
                                .setParameter("email", request.getEmail()) // Usa o email da requisição
                                .getSingleResult();

            // Se chegou até aqui, o usuário foi encontrado
            em.getTransaction().commit();
            responseBuilder.setSucesso(true)
                           .setUsuarioId(usuario.getId());
            System.out.println("✅ Usuário com email '" + request.getEmail() + "' encontrado. ID: " + usuario.getId());

        } catch (NoResultException e) {
            // Usuário não encontrado
            em.getTransaction().rollback();
            responseBuilder.setSucesso(false);
            System.out.println("❌ Usuário com email '" + request.getEmail() + "' não encontrado.");
        } catch (PersistenceException | IllegalStateException e) {
            System.err.println("Erro de persistência ao consultar usuário: " + e.getMessage());
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback(); // Garante que a transação seja revertida em caso de erro
            }
            responseBuilder.setSucesso(false);
        } catch (Exception e) {
            System.err.println("Erro inesperado ao consultar usuário: " + e.getMessage());
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            responseBuilder.setSucesso(false);
        } finally {
            em.close();
            responseObserver.onNext(responseBuilder.build()); // Envia a resposta final
            responseObserver.onCompleted(); // Completa a chamada RPC
        }
    }

    @Override
    public void listarSensores(ListarSensoresRequest request, StreamObserver<SensoresResponse> responseObserver) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin(); // Inicia transação
        SensoresResponse.Builder responseBuilder = SensoresResponse.newBuilder();

        try {
            Usuario usuario = em.find(Usuario.class, request.getUsuarioId());

            if (usuario == null) {
                // Usuário não encontrado
                em.getTransaction().rollback();
                responseBuilder.setSucesso(false)
                               .setMensagem("Usuário com ID " + request.getUsuarioId() + " não encontrado.");
                System.out.println("❌ Usuário com ID " + request.getUsuarioId() + " não encontrado para listar sensores.");
            } else {
                // Carrega os sensores associados a este usuário
                List<Sensor> sensoresDoUsuario = usuario.getSensores();

                for (Sensor sensor : sensoresDoUsuario) {
                    SensorInfo sensorInfo = SensorInfo.newBuilder()
                                                      .setSensorId(sensor.getSensorId())
                                                      .setNome(sensor.getNome())
                                                      .setDescricao(sensor.getDescricao() != null ? sensor.getDescricao() : "")
                                                      .build();
                    responseBuilder.addSensores(sensorInfo); // Adiciona cada SensorInfo à lista
                }

                em.getTransaction().commit(); // Confirma a transação
                responseBuilder.setSucesso(true)
                               .setMensagem("Sensores do usuário " + usuario.getNome() + " listados com sucesso. Total: " + sensoresDoUsuario.size());
                System.out.println("✅ Sensores listados para o usuário " + usuario.getEmail() + ". Total: " + sensoresDoUsuario.size());
            }

        } catch (PersistenceException | IllegalStateException e) {
            System.err.println("Erro de persistência ao listar sensores: " + e.getMessage());
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            responseBuilder.setSucesso(false)
                           .setMensagem("Erro ao listar sensores: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro inesperado ao listar sensores: " + e.getMessage());
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            responseBuilder.setSucesso(false)
                           .setMensagem("Erro interno ao listar sensores.");
        } finally {
            em.close();
            responseObserver.onNext(responseBuilder.build()); // Envia a resposta final
            responseObserver.onCompleted(); // Completa a chamada RPC
        }
    }
    @Override
    public void getDados(DadosRequest request, StreamObserver<DadosResponse> responseObserver) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin(); // Inicia transação
        DadosResponse.Builder responseBuilder = DadosResponse.newBuilder();

        try {
            // CORREÇÃO: Buscar a entidade Sensor pelo seu ID (que é uma String)
            Sensor sensor = em.find(Sensor.class, request.getSensorId());

            if (sensor == null) {
                em.getTransaction().rollback();
                responseBuilder.setSucesso(false) // Corrigido
                               .setMensagem("Erro: Sensor com ID '" + request.getSensorId() + "' não encontrado."); // Corrigido
                System.out.println("❌ Sensor com ID '" + request.getSensorId() + "' não encontrado para buscar dados.");
                return; // Sai do método
            }

            // Buscar o último DadosSensor para este sensor, ordenado por timestamp
            DadosSensor ultimoDado = em.createQuery(
                "SELECT d FROM DadosSensor d WHERE d.sensor = :sensor ORDER BY d.timestamp DESC", DadosSensor.class)
                .setParameter("sensor", sensor)
                .setMaxResults(1)
                .getSingleResult(); // Lança NoResultException se não houver dados para o sensor

            em.getTransaction().commit();
            
            // Converter java.time.Instant para google.protobuf.Timestamp
            Timestamp protoTimestamp = Timestamp.newBuilder()
                .setSeconds(ultimoDado.getTimestamp().getEpochSecond())
                .setNanos(ultimoDado.getTimestamp().getNano())
                .build();

            responseBuilder.setSucesso(true) // Corrigido
                           .setMensagem("Último dado encontrado para o sensor '" + request.getSensorId() + "'.") // Corrigido
                           .setSensorIdEncontrado(ultimoDado.getSensor().getSensorId())
                           .setTemperaturaEncontrada(ultimoDado.getTemperatura())
                           .setUmidadeEncontrada(ultimoDado.getUmidade())
                           .setTimestampEncontrado(protoTimestamp);
            System.out.println("✅ Último dado encontrado para o sensor '" + request.getSensorId() + "'.");

        } catch (NoResultException e) {
            // Nenhum dado encontrado para o sensor
            em.getTransaction().rollback();
            responseBuilder.setSucesso(false) // Corrigido
                           .setMensagem("Nenhum dado de leitura encontrado para o sensor '" + request.getSensorId() + "'."); // Corrigido
            System.out.println("❌ Nenhum dado de leitura encontrado para o sensor '" + request.getSensorId() + "'.");
        } catch (PersistenceException | IllegalStateException e) {
            System.err.println("Erro de persistência ao buscar dados do sensor: " + e.getMessage());
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            responseBuilder.setSucesso(false) // Corrigido
                           .setMensagem("Erro ao buscar dados do sensor: " + e.getMessage()); // Corrigido
        } catch (Exception e) {
            System.err.println("Erro inesperado ao buscar dados do sensor: " + e.getMessage());
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            responseBuilder.setSucesso(false) // Corrigido
                           .setMensagem("Erro interno ao buscar dados do sensor."); // Corrigido
        } finally {
            em.close();
            responseObserver.onNext(responseBuilder.build());
            responseObserver.onCompleted();
        }
    }
}
