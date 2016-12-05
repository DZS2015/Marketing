package com.dzs.projectframe.control;

import com.dzs.projectframe.base.Bean.Upload;
import com.dzs.projectframe.utils.AsyncTaskUtils;
import com.dzs.projectframe.utils.HttpUtils;

import java.io.InputStream;
import java.util.Map;

/**
 * 控制器
 *
 * @author DZS dzsdevelop@163.com
 * @version V1.0
 * @date 2016/9/12.
 */
public class ProjectControl {
    /**
     * 网络访问获取数据
     *
     * @param taskId             任务ID
     * @param url                URL
     * @param inputStream        证书文件
     * @param params             参数Map
     * @param keys               需要过滤可变键值（用于保存数据时候维持唯一key）
     * @param uploads            需要上传的文件
     * @param httpType           访问方式
     * @param saveCache          是否需要进行缓存
     * @param reflsh             是否需要进行强制刷新
     * @param netReturnListeners 回调
     */
    private void getData(String taskId, String url, InputStream inputStream, Map<String, Object> params, String[] keys, Upload[] uploads, HttpUtils.HttpType httpType, boolean saveCache, boolean reflsh, AsyncTaskUtils.OnNetReturnListener... netReturnListeners) {
        AsyncTaskUtils ansyTaskUtils = new AsyncTaskUtils(taskId, httpType, saveCache, reflsh, netReturnListeners);
        ansyTaskUtils.execute(url, params, keys, uploads);
    }

    protected void getDataByGet(String taskId, String url, Map<String, Object> params, AsyncTaskUtils.OnNetReturnListener... netReturnListeners) {
        getData(taskId, url, null, params, null, null, HttpUtils.HttpType.Get, false, true, netReturnListeners);
    }

    protected void getDataByGet(String taskId, String url, InputStream inputStream, Map<String, Object> params, AsyncTaskUtils.OnNetReturnListener... netReturnListeners) {
        getData(taskId, url, inputStream, params, null, null, HttpUtils.HttpType.Get, false, true, netReturnListeners);
    }

    protected void getDataByGet(String taskId, String url, InputStream inputStream, Map<String, Object> params, String[] keys, boolean saveCache, boolean reflsh, AsyncTaskUtils.OnNetReturnListener... netReturnListeners) {
        getData(taskId, url, inputStream, params, keys, null, HttpUtils.HttpType.Get, saveCache, reflsh, netReturnListeners);
    }

    protected void getDataByJson(String taskId, String url, Map<String, Object> params, AsyncTaskUtils.OnNetReturnListener... netReturnListeners) {
        getData(taskId, url, null, params, null, null, HttpUtils.HttpType.Json, false, true, netReturnListeners);
    }

    protected void getDataByJson(String taskId, String url, InputStream inputStream, Map<String, Object> params, AsyncTaskUtils.OnNetReturnListener... netReturnListeners) {
        getData(taskId, url, inputStream, params, null, null, HttpUtils.HttpType.Json, false, true, netReturnListeners);
    }

    protected void getDataByJson(String taskId, String url, InputStream inputStream, Map<String, Object> params, String[] keys, boolean saveCache, boolean reflsh, AsyncTaskUtils.OnNetReturnListener... netReturnListeners) {
        getData(taskId, url, inputStream, params, keys, null, HttpUtils.HttpType.Json, saveCache, reflsh, netReturnListeners);
    }

    protected void getDataByForm(String taskId, String url, Map<String, Object> params, AsyncTaskUtils.OnNetReturnListener... netReturnListeners) {
        getData(taskId, url, null, params, null, null, HttpUtils.HttpType.Form, false, true, netReturnListeners);
    }

    protected void getDataByForm(String taskId, String url, InputStream inputStream, Map<String, Object> params, AsyncTaskUtils.OnNetReturnListener... netReturnListeners) {
        getData(taskId, url, inputStream, params, null, null, HttpUtils.HttpType.Form, false, true, netReturnListeners);
    }

    protected void getDataByForm(String taskId, String url, InputStream inputStream, Map<String, Object> params, String[] keys, boolean saveCache, boolean reflsh, AsyncTaskUtils.OnNetReturnListener... netReturnListeners) {
        getData(taskId, url, inputStream, params, keys, null, HttpUtils.HttpType.Form, saveCache, reflsh, netReturnListeners);
    }

    protected void Upload(String taskId, String url, Map<String, Object> params, Upload[] uploads, AsyncTaskUtils.OnNetReturnListener... netReturnListeners) {
        getData(taskId, url, null, params, null, uploads, HttpUtils.HttpType.Form, false, true, netReturnListeners);
    }

    protected void Upload(String taskId, String url, InputStream inputStream, Map<String, Object> params, Upload[] uploads, AsyncTaskUtils.OnNetReturnListener... netReturnListeners) {
        getData(taskId, url, inputStream, params, null, uploads, HttpUtils.HttpType.Form, false, true, netReturnListeners);
    }
}
