from __future__ import print_function
import grpc

import datastorage_pb2 as pb2
import datastorage_pb2_grpc as pb2_grpc


def run():
    channel = grpc.insecure_channel('localhost:50051')
    stub = pb2_grpc.DataStorageStub(channel)

    response = stub.put(pb2.Data(num=5))
    print("Chave gerada pelo servidor: ", response.k)

    response2 = stub.get(pb2.Key(k=response.k))
    print("Dado recuperado: ", response2.num)


if __name__ == '__main__':
    run()