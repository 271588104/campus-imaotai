package com.oddfar.campus.business.service;

import com.oddfar.campus.business.api.PushPlusApi;
import com.oddfar.campus.business.entity.ILog;
import com.oddfar.campus.business.entity.IUser;
import com.oddfar.campus.common.utils.SpringUtils;
import com.oddfar.campus.framework.manager.AsyncManager;

import java.util.Date;
import java.util.TimerTask;

/**
 * i茅台日志记录
 */
public class IMTLogFactory {


    public static void reservation(IUser iUser, String title, String content) {
        ILog operateLog = new ILog();

        operateLog.setOperTime(new Date());

        if (title.contains("成功")) {
            //失败
            operateLog.setStatus(0);
        } else {
            operateLog.setStatus(1);
        }

        operateLog.setMobile(iUser.getMobile());
        operateLog.setCreateUser(iUser.getCreateUser());
        operateLog.setLogContent(title + " " + content);
        AsyncManager.me().execute(recordOpera(operateLog));

        //日志推送
        PushPlusApi.sendNotice(iUser, title, content);
    }

    /**
     * 操作日志记录
     *
     * @param operateLog 操作日志信息
     * @return 任务task
     */
    public static TimerTask recordOpera(final ILog operateLog) {
        return new TimerTask() {
            @Override
            public void run() {
                SpringUtils.getBean(IMTLogService.class).insertLog(operateLog);
            }
        };
    }


}
