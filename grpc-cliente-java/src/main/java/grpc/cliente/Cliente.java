/**
 *  Se alguns nomes não forem resolvidos, executar "mvn compile" em /grpc-cliente-java/
 *  e garantir que target/generated-sources/protobuf/java e target/generated-sources/protobuf/grpc-java
 *  são vistos como source roots pela IDE (uso de extensão para Maven, por exemplo).
 */

package grpc.cliente;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import datastorage.DataStorageOuterClass.Data;
import datastorage.DataStorageOuterClass.Key;
import datastorage.DataStorageGrpc;

public final class Cliente implements Runnable
{
    private int m;
    private String serverAddress = "localhost:50051";

    // Objeto para a classe gerada pelo compilador de proto3 usado pelo gRPC
    private DataStorageGrpc.DataStorageBlockingStub blockingStub;

    private Cliente( int m )
    {
        this.m = m;

        // Um channel estabelece uma conexão entre cliente e servidor
        ManagedChannel channel = ManagedChannelBuilder.forTarget(serverAddress).usePlaintext().build();
        blockingStub = DataStorageGrpc.newBlockingStub(channel);
    }

    @Override
    public void run()
    {
        List<Key> keys = new ArrayList<Key>(m);
        Random rand = new Random();

        for( int i = 1; i <= m; i++ ) {
            // Um número inteiro aleatório de 0 a 10.000.000 é criado e usado como parâmetro
            // para o método put() no stub, o que executa uma chamada remota ao servidor
            keys.add( blockingStub.put(Data.newBuilder().setNum(rand.nextInt(10_000_000)).build()) );
        }

        for( Key key : keys ) {
            // Para cada chave de cada número gerado anteriormente, executa o método get() no
            // stub, o que executa uma chamada remota ao servidor
            blockingStub.get(key).getNum();
        }
    }

    /**
     * Executa i threads Cliente que inserem m números na tabela hash do servidor usando gRPC.
     * 
     * @param args Array cuja primeira posição é a quantidade de threads Cliente a serem executadas e
     * cuja segunda posição é a quantidade de números a serem inseridos na tabela hash do servidor.
     */
    public static void main( String[] args )
    {
        final int numeroClientes = Integer.parseInt(args[0]);
        final int m = Integer.parseInt(args[1]);
        
        System.out.println("Clientes = " + args[0] + " - Números = " + args[1]);

        ArrayList<Thread> threads = new ArrayList<>();
        Cliente[] clientes = new Cliente[numeroClientes];
        
        // Inicia contagem de tempo
        long tempoInicial = System.nanoTime();

        for( Cliente cliente : clientes ) {
            // Divide os m números igualmente entre as i threads
            cliente = new Cliente(m / numeroClientes);
            Thread thread = new Thread(cliente);
            threads.add(thread);
            thread.start();
        }
        
        // Assegura a finalização de todas as threads
        for( Thread thread : threads ) {
            try {
                thread.join();
            } catch ( InterruptedException e ) {
                e.printStackTrace();
            }
        }
        
        threads.clear();
        
        // Para a contagem de tempo
        long tempoFinal = System.nanoTime();

        // Conversão de nanossegundos para segundos
        double tempo = (double) (tempoFinal - tempoInicial) / 1_000_000_000;

        System.out.println("Tempo Gasto = " + tempo + " s");
    }
}
