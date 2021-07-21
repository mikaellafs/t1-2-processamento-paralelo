import grpc
from concurrent import futures
import datastorage_pb2_grpc as pb2_grpc
import datastorage_pb2 as pb2

from django.utils.crypto import get_random_string

class DataStorageServicer(pb2_grpc.DataStorageServicer):

    def __init__(self):
        self.hashtable = {}

    def put(self, request, context):
        key_generated = get_random_string(10)
        self.hashtable[key_generated] = request.num

        return pb2.Key(k = key_generated)

    def get(self, request, context):
        data = self.hashtable[request.k]

        return pb2.Data(num = data)

def serve():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    pb2_grpc.add_DataStorageServicer_to_server(DataStorageServicer(), server)
    server.add_insecure_port('[::]:50051')
    server.start()
    server.wait_for_termination()

if __name__ == '__main__':
    serve()
