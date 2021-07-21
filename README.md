# t1-2-processamento-paralelo

**Instalar e atualizar dependências**

```
$ python3 -m pip install --upgrade pip
$ python3 -m pip install grpcio
$ python3 -m pip install grpcio-tools
$ python3 -m pip install Django
$ python3 -m pip install --upgrade protobuf

```

**Execução**

Gerar stubs (inicialmente apenas python)
```
$ python3 -m grpc_tools.protoc --proto_path=. ./datastorage.proto --python_out=. --grpc_python_out=.

```
Executar server e teste em python (apenas para testar o servidor)

```
$ python3 server.py
$ python3 teste_client.py

```
