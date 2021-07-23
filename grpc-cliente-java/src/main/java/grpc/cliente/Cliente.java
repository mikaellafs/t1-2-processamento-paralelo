/**
 *  Se alguns nomes não forem resolvidos, garantir que target/generated-sources/protobuf/java
 *  e target/generated-sources/protobuf/grpc-java são vistos como source roots pela IDE.
 *  //TODO Verificar se é possível fazer isso pelo pom
 */

package grpc.cliente;

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
    private DataStorageGrpc.DataStorageBlockingStub blockingStub;

    private Cliente( int m )
    {
        this.m = m;
        ManagedChannel channel = ManagedChannelBuilder.forTarget(serverAddress).usePlaintext().build();
        blockingStub = DataStorageGrpc.newBlockingStub(channel);
    }

    @Override
    public void run()
    {
        System.out.println("Processando ...");
        Key[] keys = new Key[m];
        Random rand = new Random();
        for( Key key : keys ) {
            int numero = rand.nextInt(10_000_000);
            Data request = Data.newBuilder().setNum(numero).build();
            key = blockingStub.put(request);
            System.out.print(key);
        }
    }

    public static void main( String[] args )
    {
        final int numeroClientes = 1;
        final int m = 1000;
        Cliente[] clientes = new Cliente[numeroClientes];
        for( Cliente cliente : clientes ) {
            cliente = new Cliente(m);
            Thread thread = new Thread(cliente);
            thread.start();
        }
    }
}
