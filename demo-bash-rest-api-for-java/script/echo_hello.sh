#!/bin/bash

msg="Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,Hello World,"

for i in {1..100}
do
    echo "[+] $(date +'%Y-%m-%d %H:%M:%S') [INFO   ]Welcome \033[32;40;1m$i\033[0m times: $msg"
done