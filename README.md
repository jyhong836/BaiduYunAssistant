BaiduYunAssistant
=================

百度云助手

GUI client for Baidu Yun, based on https://github.com/houtianze/bypy and Java is used to build it.

NOTE: you need to add the bypy.py (https://github.com/houtianze/bypy) to /usr/bin/bypy first, which means you may need python>2.7 and requests modules.

Author:Junyuan Hong

Copyright 2014 JunyuanHong (https://github.com/jyhong836/BaiduYunAssistant)

LINSENCE:under GPL v3

百度云ubuntu GUI客户端，基于https://github.com/houtianze/bypy 的python命令行工具，需要将bypy.py添加到/usr/bin/bypy，你可以使用：
‘’‘’
$ ln -s <bypy.py> /usr/bin/bypy
‘’‘’
来添加bypy

----

功能说明

----

BaiduYunAssistant脚本，需要修改里面的jre路径，改为java或者是自己的jre路径。
'''''
$sudo chmod a+x BadiduYunAssistant #然后就可以运行BadiduYunAssistant了
'''''
help文件为bypy命令的帮助

BYA.dat保存的了一些用户的设置，上一次关闭时的路径，任务等。如果发现无法启动，则尝试删除BYA.dat之后运行。

同步按键，当前的功能是上传同步文件夹的所有文件和文件夹到/sync/目录下，现在的同步是作为一个任务来进行的，而不是分解为以文件为单位的任务。

文件列表，双击文件夹将打开文件夹。“..”代表父目录。

任务列表，以一个bypy的一个命令为一个任务单位，如downdir或syncup为一个任务。这个任务不能由命令行启动。在任务栏显示的文件只有前5个是真正在运行的，其他的只有在等待前5个运行完成之后会自动运行。

选项->设置：可以设置同步文件夹列表，在主界面按下同步按钮之后会上传给列表的所有内容。


