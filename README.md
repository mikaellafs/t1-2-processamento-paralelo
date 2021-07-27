# Trabalho T1.2 - Uso de Middleware RPC/RMI

<br>

## **Servidor**

<br>

**Instalar e atualizar dependências**

```
$ python3 -m pip install --upgrade pip
$ python3 -m pip install grpcio
$ python3 -m pip install grpcio-tools
$ python3 -m pip install Django
$ python3 -m pip install --upgrade protobuf
```

<br>

**Execução**

<br>

Gerar *stubs*

```
$ python3 -m grpc_tools.protoc --proto_path=. ./datastorage.proto --python_out=. --grpc_python_out=.
```

Executar server

```
$ python3 server.py
```

<br>

## **Cliente**

<br>

**Instalar e atualizar dependências**

<br>

A ferramenta de gerência de projeto de software Maven foi escolhida para geração do cliente e seus *stubs*. O download da ferramenta pode ser feito através do link:

[https://maven.apache.org/download.cgi](https://maven.apache.org/download.cgi)

<br>

A instalação da ferramenta pode ser feita de acordo com as instruções em:

[https://maven.apache.org/install.html](https://maven.apache.org/install.html)

<br>

**Execução**

<br>

Abra um terminal na raiz do projeto e execute:

```
$ cd grpc-cliente-java
$ mvn package
```

Agora executamos o(s) cliente(s):

```
$ cd target
$ java -jar grpc-cliente-java-1.0-SNAPSHOT-jar-with-dependencies.jar <num_clientes> <n_nums>
```

Substituindo <num_clientes> pelo número de threads a serem executadas e <n_nums> pela quantidade de números a serem inseridos na hash do servidor.


## **Experimentos**

Por questões técnicas, não conseguimos automatizar a execução no próprio código Java, então criamos o programa *execDados.sh* em shellscript para automatizar o processo.

Esse script executa o programa Cliente dez vezes para cada i = {1, 2, 4, 8} clientes que inserem a quantidade de números desejada.

Na pasta /grpc-cliente-java/, após o comando `$ mvn package` , executar:

```
$ bash execDados.sh <n_nums>
```
Substituindo <n_nums> pela quantidade de números a serem inseridos na tabela hash do servidor.
