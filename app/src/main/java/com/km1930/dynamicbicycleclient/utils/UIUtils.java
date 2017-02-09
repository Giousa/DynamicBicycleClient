package com.km1930.dynamicbicycleclient.utils;


import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;

import com.km1930.dynamicbicycleclient.application.AutoCyclingApplication;


public class UIUtils {
	/**
	 * @return	全局的上下文环境
	 */
	public static Context getContext(){
		return AutoCyclingApplication.getContext();
	}

	/**
	 * @return	全局的hander对象
	 */
	public static Handler getHandler(){
		return AutoCyclingApplication.getHandler();
	}

	/**
	 * @return	返回主线程方法
	 */
	public static Thread getMainThread(){
		return AutoCyclingApplication.getMainThread();
	}

	/**
	 * @return	返回主线程id方法
	 */
	public static int getMainThreadId(){
		return AutoCyclingApplication.getMainThreadId();
	}

	/**
	 * 布局文件转换成view对象方法
	 * @param layoutId	布局文件id
	 * @return	布局文件转换成的view对象
	 */
	public static View inflate(int layoutId){
		return View.inflate(getContext(), layoutId, null);
	}

	/**
	 * @return	返回资源文件夹对象方法
	 */
	public static Resources getResources(){
		return getContext().getResources();
	}

	/**
	 * @param stringId	字符串在xml中对应R文件中的id
	 * @return	string.xml某节点,对应的值
	 */
	public static String getString(int stringId){
		return getResources().getString(stringId);
	}

	/**
	 * @param stringArrayId	字符串数组在xml中对应R文件中的id
	 * @return	节点对应的内容
	 */
	public static String[] getStringArray(int stringArrayId){
		return getResources().getStringArray(stringArrayId);
	}

	//像素密度决定比例关系
	//1:0.75
	//1:1
	//1:1.5
	//1:2
	//1:3
	//dip2px()  dip---->px
	public static int dip2px(int dip){
		//1,获取当前手机的dip和px的转换关系比例值,不同的手机执行此段代码的时候,得到的比例值可能不一致
		float density = getResources().getDisplayMetrics().density;
		//2,将dp转换成px
		return (int)(dip*density+0.5);
	}

	//px2dip
	public static int px2dip(int px){
		//1,获取当前手机的dip和px的转换关系比例值,不同的手机执行此段代码的时候,得到的比例值可能不一致
		float density = getResources().getDisplayMetrics().density;
		//2,将dp转换成px
		return (int)(px/density+0.5);
	}

	//将任务(可能在主线程中,也可能在子线程中)放置在主线程中运行的方法
	/**
	 * @param runnable	将任务保证在主线程中运行的方法
	 */
	public static void runInMainThread(Runnable runnable){
		//获取调用此方法所在的线程
		if(android.os.Process.myTid() == getMainThreadId()){
			//如果上诉runnable就是在主线程中要去执行的任务,则直接运行即可
			runnable.run();
		}else{
			//如果上诉runnable运行在子线程中,将其传递到主线程中去做执行
			getHandler().post(runnable);
		}
	}

	public static Drawable getDrawable(int drawableId) {
		return getResources().getDrawable(drawableId);
	}

	/**
	 * 获取一个颜色选择器的对象	
	 * @param mTabTextColorResId	颜色选择器id
	 * @return
	 */
	public static ColorStateList getColorStateList(int mTabTextColorResId) {
		return getResources().getColorStateList(mTabTextColorResId);
	}

	/**
	 * 执行延迟任务的操作
	 * @param runnableTask 延时任务
	 * @param delayTime	   延时时间
	 */
	public static void postDelayed(Runnable runnableTask, int delayTime) {
		getHandler().postDelayed(runnableTask,delayTime);

	}

	/**
	 * 移除任务操作
	 * @param runnableTask 需要移除的任务对象
	 */
	public static void removeCallBack(Runnable runnableTask) {
		getHandler().removeCallbacks(runnableTask);
	}

}
