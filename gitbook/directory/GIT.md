# 分支管理
> 这里简化了git flow的流程，中间可能产生的合并冲突请手动解决

## 版本号

> 主版本号.次版本号.修订号，版本号递增规则如下：
>
>    + 主版本号：当你做了不兼容的 API 修改
>
>    +  次版本号：当你做了向下兼容的功能性新增
>    + 修订号：当你做了向下兼容的问题修正。

## 分支

- **master**，demo和prod环境
- **develop**，dev和qa环境



## 后端

- #### Release 流程

   > 场景：**合并测试分支与生产分支**
   >
   > 过程：合并develop分支到master分支上，并打tag
   >
   > **确保本地develop、master分支上没有未提交的代码，并且为最新，否则会失败**

   1. 切换到develop分支

      ```shell
      $ git checkout develop
      ```

   2. 产生release分支

      ```shell
      $ mvn jgitflow:release-start
      ```

   3. 合并release分支到develop和master分支上，并push，tag，**合并后会删除release/*分支**，**发布demo环境做验证**

      ```shell
      $ mvn jgitflow:release-finish
      ```

- #### Hotfix 流程

  > 场景：**在生产分支上做紧急修复**
  >
  > 过程：从master分支建立hotfix分支，在hotfix分支上做修改，后将hotfix分支合并develop分支到master分支上，并打tag
  >
  > **确保本地develop、master分支上没有未提交的代码，并且为最新，否则会失败**

  1. 切换到master分支

     ```shell
     $ git checkout master
     ```

  2. 产生hotfix分支

     ```shell
     $ mvn jgitflow:hotfix-start
     ```

  3. 在此分支上做开发push，将jenkins的demo环境分支临时修改为hotfix分支，**发布demo环境做验证**

     ```shell
     $ git push --set-upstream origin hotfix/$VERSION
     ```

  4. 合并hotfix分支到develop和master分支上，并push，tag，**合并后会删除hotfix/*分支**，**最后发布prod环境**

     ```shell
     $ mvn jgitflow:hotfix-finish
     ```

  5. **将jenkins的demo环境的分支修改为master**

     

## 前端

- #### Release 流程

   > **场景：合并测试分支与生产分支**
   >
   > 过程：合并develop分支到master分支上，并打tag
   >
   > **确保本地develop、master分支上没有未提交的代码，并且为最新，否则会失败**

   1. 首次使用

      ```shell
      $ git flow init
      ```

   2. 设置版本号

      ```shell
      $ VERSION=<version>
      ```

   3. 切换到develop分支

      ```shell
      $ git checkout develop
      ```

   4. 产生release分支

      ```shell
      $ git flow release start $VERSION
      ```

   5. 合并release分支到develop和master分支上，合并后会删除release/*分支

      ```shell
      $ git flow release finish $VERSION
      ```

   6. push 到master分支，打tag

      ```shell
      1. $ git push && git push --tags
      ```

   7. push develop分支，**发布demo环境做验证**

      ```shell
      $ git checkout develop &&  git push
      ```

      

- #### Hotfix 流程

  > **场景：在生产分支上做紧急修复**
  >
  > 过程：从master分支建立hotfix分支，在hotfix分支上做修改，后将hotfix分支合并develop分支到master分支上，并打tag
  >
  > **确保本地develop、master分支上没有未提交的代码，并且为最新，否则会失败**

  1. 设置版本号

     ```shell
     $ VERSION=<version>
     ```

  2. 切换到master分支

     ```shell
     $ git checkout master
     ```

  3. 产生hotfix分支

     ```shell
     $ git flow hotfix start $VERSION
     ```

  4. 在此分支上做开发push，将jenkins的demo环境分支临时修改为hotfix分支，**发布demo环境做验证**

     ```shell
     $ git push --set-upstream origin hotfix/$VERSION
     ```

  5. 合并hotfix分支到develop和master分支上

     ```shell
     $ git checkout hotfix/$VERSION
     $ git flow hotfix finish $VERSION
     ```

  6. push develop分支

     ```shell
     $ git checkout develop && git push
     ```

  7. push 到master分支，打tag，**合并后会删除hotfix/*分支**，**最后发布prod环境**

     ```shell
     $ git checkout master && git push && git push --tags && git push origin --delete hotfix/$VERSION
     ```

  8. **将jenkins的demo环境的分支修改为master**

## 合并独立提交

>如果需要将其它分支的commit独立合并到hotfix分支，可使用如下命令
>如果要将多个提交打到hot fix 上，重复该步骤 

~~~shell
$ git cherry-pick <commitID>
~~~
