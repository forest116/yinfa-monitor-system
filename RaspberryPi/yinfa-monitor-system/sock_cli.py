import RPi.GPIO as GPIO
import socket
import datetime
import time
import json
import smbus
import cv2
from time import ctime

#初始化GPIO
GPIO.setmode(GPIO.BCM)    #设置BCM编码
GPIO.setwarnings(False)
GPIO.setup(5,GPIO.OUT)    #5号引脚（蜂鸣器）为输出
GPIO.output(5,GPIO.HIGH)
GPIO.setup(17,GPIO.IN)    #17号引脚（干簧管）
#GPIO.setup(18,GPIO.IN)    #18号引脚（MQ-5传感器）为输入
GPIO.setup(23,GPIO.IN)    #23号引脚（MQ-2传感器）为输入

#设置TCP参数
HOST = '114.116.196.117'
PORT = 1452               #端口号1452 用于pi到服务器JSON数据的传输
sock = socket.socket(socket.AF_INET,socket.SOCK_STREAM)  #（IPv4,TCP流）
sock.connect((HOST,PORT))

#跌倒数据文件夹
filename = '/home/pi/Desktop/yinfa-monitor-system/fall.txt'

bus = smbus.SMBus(1)         #创建一个smbus实例
# 烟雾模拟数据
def readSmog():
    #发送一个控制字节到设备 表示要读取AIN0通道的数据
    bus.write_byte(0x48,0x40)   
    bus.read_byte(0x48)         # 空读一次，消费掉无效数据
    return bus.read_byte(0x48)  # 返回某通道输入的模拟值A/D转换后的数字值

while True:
#    gas = GPIO.input(18)
    smog = readSmog()/255*100
    invade =GPIO.input(17)
    time_stamp = datetime.datetime.now()
    #时间戳
    ts = time.time()
    nowTime = time_stamp.strftime('%Y.%m.%d  %H:%M:%S')
    fo = open(filename,'r')
    fall = fo.read(1)
    state = -1
    fo.close()
   
    if smog > 30 or invade == 0 or fall == '0':
        GPIO.output(5,GPIO.LOW)
        state = 0
        cap = cv2.VideoCapture("http://192.168.31.27:8080/?action=snapshot")
        success, img = cap.read()
        if success:
            cv2.imwrite('/home/pi/Desktop/yinfa-monitor-system/unusual-images/'+nowTime+'.jpg',img, [int(cv2.IMWRITE_PNG_COMPRESSION), 9])
            fo = open(filename,'w')
            fall = fo.write('1')
            fo.close()
            print('保存异常图片成功')
        else:
            print('保存异常图片失败')
    else:
        state = 1
        GPIO.output(5,GPIO.HIGH)
    
    info = {'timestamp':ts,'time':nowTime,'smog':"%.2f"%smog,'invade':invade,'fall':fall,'state':state}
    infoJson = json.dumps(info)
    sock.send(infoJson.encode())
    print(sock.recv(1024).decode())
    time.sleep(1)
GPIO.cleanup()
sock.close()

    
