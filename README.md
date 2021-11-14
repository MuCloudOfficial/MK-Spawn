### EX-AdvancedWarps  
___  
___:fa-exclamation-triangle:当前插件仍在测试阶段___  
__作者: Mark_Chanel__  
BiliBili UID: 356101590  
___  

#### 插件概述  
__一个修复 Essentials 关于地标系统的问题修复与扩充__  
___该插件以 Essentials 作为前置___  

#### 主要功能 
- 修复 Essentials 的 中文地标 问题  
- 对 Essentials 扩充了一些需求地标（金钱，权限，物品）  

#### 命令  
- /exaw info  ---  显示插件主页  
- /exaw convertMainMC   ---   一个实验性功能：将 MainMC 地标转换为 AdvancedWarps 地标（实际上是转换为 Essentials 地标）
- /warps      ---  显示已有地标(与Essentials中命令相同，但对管理员提供了显示需求地标的实现)  

#### 权限（现有的）
- advancedwarps.admin          ---   本插件的管理权限
- exaw.teleportwarp.bypass.*   ---   拥有该权限时可跳过收取地标需求环节直接传送（* 为应用至所有地标，可以更换为地标名以应用至指定地标）

#### Q&A
> Q: __为什么无法使用牌子传送__  
> A: 可能是因为 Essentials 中未配置启动功能告示牌功能，或配置了功能但未配置相应的权限  
>    可以在 plugins/Essentials/config.yml 中找到 enabledSigns 将其下所有子项的注释取消并配置对应的权限即可使用  