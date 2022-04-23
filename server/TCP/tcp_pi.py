from socket import *
from time import ctime
import json
import time

rt_filename = '/home/hk/rtInfo.txt'
unusual_filename = '/www/wwwroot/ksjddg/hk/unusual-info.txt'

HOST = '192.168.0.197'
PORT = 1452 
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
		data = tcpClient.recv(BUFSIZE)
		print(data.decode())
		if not data:
			print('---------')
			break;
 
		buf = data.decode()
		file_object = open(rt_filename,'w')
		file_object.write(buf)
		file_object.close()
		print('rtInfo写入成功')
		
		jsonInfo = json.loads(buf)
		state = jsonInfo['state']
		if(state == 0):
		    file_object = open(unusual_filename,'a')
		    file_object.write(buf+',')
		    file_object.close()
		    print('unusualInfo写入成功')
		tcpClient.send(data)
	tcpClient.close()

file_object.close()
tcpServer.close()
