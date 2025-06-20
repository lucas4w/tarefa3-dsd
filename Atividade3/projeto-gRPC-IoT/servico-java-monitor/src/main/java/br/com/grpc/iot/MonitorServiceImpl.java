package br.com.grpc.iot;

import com.google.protobuf.Timestamp;
import io.grpc.stub.StreamObserver;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

// A lógica do nosso serviço. Estendemos a classe base gerada pelo gRPC.
public class MonitorServiceImpl extends MonitorServiceGrpc.MonitorServiceImplBase {

    // Implementação do nosso método de streaming do cliente.
    @Override
    public StreamObserver<SensorData> enviarDadosSensor(StreamObserver<StatusResposta> responseObserver) {

        // Usamos AtomicInteger para contar as leituras de forma segura em ambientes com múltiplas threads.
        final AtomicInteger leiturasContadas = new AtomicInteger(0);

        // O gRPC nos pede para retornar um StreamObserver. Este objeto define o que fazer
        // quando o cliente nos envia uma mensagem, um erro, ou termina a comunicação.
        return new StreamObserver<SensorData>() {
            // onNext é chamado toda vez que o cliente envia uma nova mensagem (SensorData).
            @Override
            public void onNext(SensorData data) {
                leiturasContadas.incrementAndGet();
                Instant instant = Instant.ofEpochSecond(data.getTimestamp().getSeconds(), data.getTimestamp().getNanos());
                String dataFormatada = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
                                                        .withZone(ZoneId.systemDefault())
                                                        .format(instant);

                System.out.printf(
                    "[DADO RECEBIDO] Sensor ID: %s | Temperatura: %.2f°C | Umidade: %.2f%% | Data: %s%n",
                    data.getSensorId(),
                    data.getTemperatura(),
                    data.getUmidade(),
                    dataFormatada
                );
            }

            // onError é chamado se o cliente envia um erro.
            @Override
            public void onError(Throwable t) {
                System.err.println("Erro no stream do cliente: " + t.getMessage());
            }

            // onCompleted é chamado quando o cliente finaliza o envio de dados.
            @Override
            public void onCompleted() {
                System.out.println("Stream de dados do cliente finalizado.");
                // Preparamos a resposta final para o cliente.
                StatusResposta resposta = StatusResposta.newBuilder()
                        .setMensagem("Dados recebidos com sucesso!")
                        .setTotalLeiturasRecebidas(leiturasContadas.get())
                        .build();

                // Enviamos a resposta e fechamos a comunicação.
                responseObserver.onNext(resposta);
                responseObserver.onCompleted();
            }
        };
    }
}