[TOC]

# Git

## 安装

CentOS 安装

    yum install git

## 配置git

配置秘钥，免密登录

    cd ~/.ssh





查看当前目录仓库状态

	git status
	
克隆仓库


	git clone git@xxxx

	http 指定账号密码

git clone http://username:password@gitlab.*****.com
	
克隆分支     git clone -b <分支名>  clone地址	
	
提交本地仓库

	git add .
	git commit -m ""
	git push origin master

更新仓库代码

    git pull origin master
    
    
    
    
    
- 分支操作
- 暂存操作
- 回退操作
- 标签操作
- 常规操作
	- git创建项目仓库
	- 忽略已加入到版本库中的文件
	- 取消忽略文件
	- 拉取、上传免密码
	
分支操作

git branch 创建分支
git branch -b 创建并切换到新建的分支上
git checkout 切换分支
git branch 查看分支列表
git branch -v 查看所有分支的最后一次操作
git branch -vv 查看当前分支
git brabch -b 分支名 origin/分支名 创建远程分支到本地
git branch --merged 查看别的分支和当前分支合并过的分支
git branch --no-merged 查看未与当前分支合并的分支
git branch -d 分支名 删除本地分支
git branch -D 分支名 强行删除分支
git branch origin :分支名 删除远处仓库分支
git merge 分支名 合并分支到当前分支上
暂存操作

git stash 暂存当前修改
git stash apply 恢复最近的一次暂存
git stash pop 恢复暂存并删除暂存记录
git stash list 查看暂存列表
git stash drop 暂存名(例：stash@{0}) 移除某次暂存
git stash clear 清除暂存

##### 回退操作

git reset --hard HEAD^ 回退到上一个版本
git reset --hard ahdhs1(commit_id) 回退到某个版本
git checkout -- file撤销修改的文件(如果文件加入到了暂存区，则回退到暂存区的，如果文件加入到了版本库，则还原至加入版本库之后的状态)
git reset HEAD file 撤回暂存区的文件修改到工作区

###### 撤销未push的commit

1. 通过`git log`命令找到commitId
2. 撤销commit操作`git reset commitId`

##### 标签操作

git tag 标签名 添加标签(默认对当前版本)
git tag 标签名 commit_id 对某一提交记录打标签
git tag -a 标签名 -m '描述' 创建新标签并增加备注
git tag 列出所有标签列表
git show 标签名 查看标签信息
git tag -d 标签名 删除本地标签
git push origin 标签名 推送标签到远程仓库
git push origin --tags 推送所有标签到远程仓库
git push origin :refs/tags/标签名 从远程仓库中删除标签
常规操作

git push origin test 推送本地分支到远程仓库
git rm -r --cached 文件/文件夹名字 取消文件被版本控制
git reflog 获取执行过的命令
git log --graph 查看分支合并图
git merge --no-ff -m '合并描述' 分支名 不使用Fast forward方式合并，采用这种方式合并可以看到合并记录
git check-ignore -v 文件名 查看忽略规则
git add -f 文件名 强制将文件提交
git创建项目仓库
git init 初始化
git remote add origin url 关联远程仓库
git pull
git fetch 获取远程仓库中所有的分支到本地
忽略已加入到版本库中的文件
git update-index --assume-unchanged file 忽略单个文件
git rm -r --cached 文件/文件夹名字 (. 忽略全部文件)
取消忽略文件
git update-index --no-assume-unchanged file
拉取、上传免密码
git config --global credential.helper store

###### Fork项目同步原项目

将Fork的项目下载到本地

git clone xxx

增加原项目作为远程项目

git remote add upstream xxx

拉取原项目，在本地完成合并

git fetch upstream

git merge upstream/master

提交合并结果

git push

注：反向创建pull request也可以解决。对于改动较大，冲突很多的项目，拉取两份代码，通过对比工具解决

###### 创建分支

新建分支并推送到远程

git checkout -b branchName

git push origin branchName

## Gitlab

找回密码
http://www.cnblogs.com/heyongboke/p/9968506.html

## Github

[CI/CD](https://mp.weixin.qq.com/s/CxblbkhfP82CzRQ_0ttlFw)