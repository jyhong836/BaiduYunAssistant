BaiduYunAssistant
=================

百度云助手

GUI client for Baidu Yun, based on https://github.com/houtianze/bypy and Java is used to build it.

NOTE: you need to add the bypy.py (https://github.com/houtianze/bypy) to /usr/bin/bypy first, which means you may need python>2.7 and requests modules.

Author:Junyuan Hong

Copyright 2014 JunyuanHong (https://github.com/jyhong836/BaiduYunAssistant)

LINSENCE:under GPL v3

百度云ubuntu GUI客户端，基于https://github.com/houtianze/bypy 的python命令行工具，需要将bypy.py添加到/usr/bin/bypy，你可以使用：

$ ln -s <bypy.py> /usr/bin/bypy

来添加bypy

----

更新

----

添加了Refresh, Download, Upload, New Dir按钮

添加Help菜单->Help/about

BaiduYunAssistant脚本，需要修改里面的jre路径，改为java或者是自己的jre路径。

$sudo chmod a+x BadiduYunAssistant #然后就可以运行BadiduYunAssistant了

help文件为bypy命令的帮助

每次重新启动都会恢复到上一次退出时的状态。利用ReadObject()和WriteObject()做到这一点。
