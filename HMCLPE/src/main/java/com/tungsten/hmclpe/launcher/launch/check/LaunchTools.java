package com.tungsten.hmclpe.launcher.launch.check;

/*
 * 启动流程
 * 判断当前版本是否启用特定设置，并获取当前版本设置
 * 检测Java版本（当前选择的Java版本是否支持当前游戏版本）
 * 检查后端是否支持当前游戏版本（1.7.2以下版本发出警告，1.17+版本渲染警告）
 * 处理游戏依赖（检查运行库）
 * 登录（检查是否创建用户，微软账户刷新，外置登录刷新或者重登录，以及检测authlib-injector，nide8auth的更新）
 * 检查Forge动画禁用情况（1.12及以下版本启动forge需要禁用forge动画）
 * 检查游戏完整性（检查游戏文件是否存在/正确）
 * 传入参数，使用相应后端启动游戏
 */

import android.content.Context;

import com.tungsten.hmclpe.launcher.MainActivity;

public class LaunchTools {

    public static void launch(Context context, MainActivity activity) {

    }

}
