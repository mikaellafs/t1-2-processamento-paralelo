/**
 *  Se alguns nomes não forem resolvidos, garantir que target/generated-sources/protobuf/java
 *  e target/generated-sources/protobuf/grpc-java são vistos como source roots pela IDE.
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
        List<Key> keys = new ArrayList<Key>(m);
        Random rand = new Random();
        for( int i = 1; i <= m; i++ ) {
            int numero = rand.nextInt(10_000_000);
            Data request = Data.newBuilder().setNum(numero).build();
            keys.add( blockingStub.put(request) );
        }
        for( Key key : keys ) {
            System.out.print(key.getK() + " ");
            Data numero = blockingStub.get(key);
            System.out.println(numero.getNum());
        }
    }

    public static void main( String[] args )
    {
        final int numeroClientes = 1;
        final int m = 100;
        Cliente[] clientes = new Cliente[numeroClientes];
        for( Cliente cliente : clientes ) {
            cliente = new Cliente(m);
            Thread thread = new Thread(cliente);
            thread.start();
        }
    }
}
