import cv2
import time
import socket
import os
import os.path as op

#异常图片的路径
dir = r'/home/pi/Desktop/yinfa-monitor-system/unusual-images'

#设置TCP参数
HOST = '114.116.196.117'
#HOST = '192.168.31.27'
PORT = 1454              #端口号1454 用于pi到服务器图片数据的传输
tcpClient = socket.socket(socket.AF_INET,socket.SOCK_STREAM)  #（IPv4,TCP流）
tcpClient.connect((HOST,PORT))

#cap = cv2.VideoCapture(0)
while True:
    #当前时间戳
    now = time.time()
    #文件夹中最新文件的时间戳
    last = os.path.getmtime(dir)
    if now-last <= 1:
        file_list = os.listdir(dir)
        file_list.sort(key = lambda fn:os.path.getmtime(dir+"/"+fn))
        time.sleep(1)
        cv_image = cv2.imread(dir+'/'+file_list[-1])
        
        #计时
        start = time.perf_counter()
        #压缩图像
        img_encode = cv2.imencode('.jpg',cv_image,[cv2.IMWRITE_JPEG_QUALITY,99])[1]
        #转换成字节流
        bytedata = img_encode.tobytes()
    
        flag_data =  (str(len(bytedata))).encode()+",".encode()+" ".encode()
        tcpClient.send(flag_data)
        print('已发送文件大小')
        name_data = file_list[-1]
        
        data = tcpClient.recv(1024)
        if("ok" == data.decode()):
            tcpClient.send(name_data.encode())
            print('已发送文件名字')
               
        data = tcpClient.recv(1024)
        if("ok" == data.decode()):
            tcpClient.send(bytedata)
            print('已发送文件')
    
        data = tcpClient.recv(1024)
        if("ok" == data.decode()):
            print("延迟："+str(int((time.perf_counter()-start)*1000))+"ms")
            
        #time.sleep(1)
    