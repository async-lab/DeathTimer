# DeathTimer

A plugin allows you to customize the behavior after player's death timer over.

Support PlaceHolderAPI.

## Commands

+ `/dt`or`/deathtimer`
    + `/dt reload`
    + `/dt revive`

## Configs

### config.yml

```yml
#语言文件（在locals目录下）
lang: "zh_CN"
#死亡后计时
cooldown: 10
#时间单位: HOUR MINUTE SECOND
unit: "SECOND"
```

### locals/zh_CN.yml

```yml
command:
  without_permission: "[DeathTimer] §c你没有权限!"
  non_console: "[DeathTimer] §c控制台无法使用此命令!"
  non_player: "[DeathTimer] §c找不到此玩家!"
  reload:
    reloaded: "[DeathTimer] 插件已重置"
  revive:
    revived: "[DeathTimer] 已复活"
    time_left: "[DeathTimer] §c时候未到..."
  clear:
    cleared: "[DeathTimer] 已清除冷却时间"
actionbar:
  cooldown:
    # elapse: ""
    end: "§a输入 /dt revive 就地复活"
```

### dead.yml

is not a config file  
just use as a data file

DO NOT EDIT IT

## LICENSE

AGPLv3