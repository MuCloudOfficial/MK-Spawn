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
- /warps      ---  显示已有地标(与Essentials中命令相同，但对管理员提供了显示需求地标的实现)  

#### Q&A
> Q: __为什么无法使用牌子传送__  
> A: 可能是因为 Essentials 中未配置启动功能告示牌功能，或配置了功能但未配置相应的权限  
>    可以在 plugins/Essentials/config.yml 中找到 enabledSigns 将其下所有子项的注释取消并配置对应的权限即可使用  