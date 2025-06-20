package br.com.grpc.iot;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import java.io.IOException;

public class MonitorServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        int port = 50051;

        // Cria o servidor na porta especificada, adicionando nosso serviço.
        Server server = ServerBuilder.forPort(port)
                .addService(new MonitorServiceImpl())
                .build();

        // Inicia o servidor.
        server.start();

        System.out.println("✅ Servidor gRPC iniciado na porta " + port);
        System.out.println("Aguardando conexões dos sensores...");

        // Aguarda o encerramento do servidor para manter a aplicação rodando.
        server.awaitTermination();
    }
}