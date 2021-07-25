import grpc
from concurrent import futures
import datastorage_pb2_grpc as pb2_grpc
import datastorage_pb2 as pb2

from django.utils.crypto import get_random_string

# Classe que implementa o servidor
class DataStorageServicer(pb2_grpc.DataStorageServicer):

    def __init__(self):
        self.hashtable = {}

    # Metodo para insercao de um numero na hashtable
    def put(self, request, context):
        key_generated = get_random_string(10) #Gera uma chave aleatoria independente do valor a ser inserido
        self.hashtable[key_generated] = request.num 

        return pb2.Key(k = key_generated) # Retorna a chave para o cliente que fez a chamada

    def get(self, request, context):
        data = self.hashtable[request.k] # Recupera dado a partir da chave

        return pb2.Data(num = data)

def serve():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    pb2_grpc.add_DataStorageServicer_to_server(DataStorageServicer(), server) 
    server.add_insecure_port('[::]:50051')
    server.start() # Nao bloqueante
    server.wait_for_termination() # Bloquear ate o servidor terminar

if __name__ == '__main__':
    serve()
