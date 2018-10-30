[TOC]

# Git

## 安装

CentOS 安装

    yum install git

检查

    git --version

## 配置git

这里的账号邮箱并不是git的，只是身份表示。

    git config --global user.name "47.95.252.113"
    git config --global user.email "wspvideo@zintow.com"

配置秘钥，免密登录

    cd ~/.ssh





查看当前目录仓库状态

	git status
	
克隆仓库

	git clone git@xxxx

	http 指定账号密码

	git clone http://Ly641921791:Ly19930830@47.96.16.130/Ly641921791/digitop-cloud.git
	
提交本地仓库

	git add .
	git commit -m ""
	git push origin master


更新仓库代码

    git pull origin master