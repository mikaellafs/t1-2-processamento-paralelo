# t1-2-processamento-paralelo

**Instalar dependências**

```
$ python3.7 -m pip install --upgrade pip
$ pip install grpcio-tools
$ python -m pip install Django

```

**Execução**

Gerar stubs
```
$ python3.7 -m grpc_tools.protoc --proto_path=. ./datastorage.proto --python_out=. --grpc_python_out=.

```
Executar server e teste em python (apenas para testar o servidor)

```
$ python3.7 server.py
$ python3.7 teste_client.py

```
