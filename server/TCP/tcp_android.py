from socket import *
import time

filename = '/home/hk/rtInfo.txt'

HOST = '192.168.0.197'
PORT = 1453
BUFSIZE = 4096
ADDR = (HOST, PORT)
 
tcpServer = socket(AF_INET, SOCK_STREAM)
tcpServer.bind(ADDR)
tcpServer.listen(10)


while 1:
	print('waiting for connection...')
	tcpClient, addr = tcpServer.accept()
	print(addr)
	
	while 1:
		file_object = open(filename,'r+')
		buf = file_object.read()
		print(buf)
		tcpClient.send(buf.encode())
		file_object.close()
		data = tcpClient.recv(BUFSIZE)
		print(data.decode())
		if not data:
			print('---------')
			break;
		time.sleep(1);
	tcpClient.close()
tcpServer.close()
