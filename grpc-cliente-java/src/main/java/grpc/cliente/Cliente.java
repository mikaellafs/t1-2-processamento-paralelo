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
        keys.add( blockingStub.put(Data.newBuilder().setNum(rand.nextInt(10_000_000)).build()) );
        }
        for( Key key : keys ) {            //System.out.println(key.getK() + " " + blockingStub.get(key).getNum());
        blockingStub.get(key).getNum(); // Referente ao get sem fazer print nas linhas
        }
    }

    public static void main( String[] args )
    {
        final int numeroClientes = Integer.parseInt(args[0]);
        final int m = Integer.parseInt(args[1]);       
        
        System.out.println("Clientes = " + args[0] + " - Números = " + args[1]);
        ArrayList<Thread> threads = new ArrayList<>();
        Cliente[] clientes = new Cliente[numeroClientes];
        
        long tempoInicial = System.nanoTime();
        for( Cliente cliente : clientes ) {
            cliente = new Cliente(m/numeroClientes);
            Thread thread = new Thread(cliente);
            threads.add(thread);
            thread.start();
        }
        
        for( Thread thread : threads ) {
            try {
                thread.join();
            } catch ( InterruptedException e ) {
                e.printStackTrace();
            }
        }
        
        threads.clear();
        
        long tempoFinal = System.nanoTime();
        double tempo = (double) (tempoFinal - tempoInicial) / 1_000_000_000;
        System.out.println("Tempo Gasto = " + tempo + " s");
    }
}
