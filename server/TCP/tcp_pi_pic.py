import socket
#import cv2
import numpy as np
from PIL import Image
import time


HOST = '192.168.0.197'
PORT = 1454	#端口号1454 用于pi到服务器图片传输
tcpServer = socket.socket(socket.AF_INET,socket.SOCK_STREAM)
tcpServer.bind((HOST,PORT))
tcpServer.listen(5)

allow = 0
dir = '/www/wwwroot/ksjddg/hk/unusual-images'

while True:
	print("等待连接......")
	client_socket,client_address = tcpServer.accept()
	print("连接成功！")
	try:
		while True:
			data1 = client_socket.recv(1024)
			allow = 0
			if data1:
				client_socket.send(b"ok")  #接收到图片大小信息
				print('接收到图片大小信息')
				flag = data1.decode().split(",")
				total = int(flag[0])
				cnt = 0
				img_bytes = b""
				allow += 1
			else:
				print("已断开！")
				break
			data2 = client_socket.recv(1024)
			if data2:
			    print('接收到图片名')
			    client_socket.send(b"ok")  #接收到图片名
			    image_name = data2.decode() 
			    allow += 1
			else:
				print("已断开！")
				break
			if allow ==  2:
				while cnt < total:
					data = client_socket.recv(256000)
					img_bytes += data
					cnt += len(data)
					print("receive:"+str(cnt)+"/"+flag[0])

				file_object = open(dir+'/'+image_name,'wb')
				file_object.write(img_bytes)
				client_socket.send(b"ok")
			time.sleep(1)


	finally:
		client_socket.close()
		file_object.close()
