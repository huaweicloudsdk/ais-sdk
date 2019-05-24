package com.huawei.ais.demo.vct;

import com.huawei.ais.demo.HttpJsonDataUtils;
import com.huawei.ais.demo.ServiceAccessBuilder;
import com.huawei.ais.demo.obs.ObsFileHandle;
import com.huawei.ais.demo.obs.SimpleObsClient;
import com.huawei.ais.demo.vct.model.*;
import com.huawei.ais.sdk.AisAccess;
import org.apache.http.HttpResponse;

import java.io.IOException;
import java.util.List;

/**
 * 视频标签API AK/SK方式使用示例
 */
public class AkskDemo {
    private static final String SUBMIT_JOB_URI = "/v1.0/video/tagging";
    private static final String GET_JOB_RESULT_URI_TEMPLATE = "/v1.0/video/tagging?job_id=%s";
    private static final String GET_JOB_LIST_URI = "/v1.0/video/tagging/jobs";

    private static final String JSON_ROOT = "result";
    private static final long QUERY_JOB_RESULT_INTERVAL = 2000L;
    private static final Integer RETRY_MAX_TIMES = 3; // 查询任务失败的最大重试次数

    public static void main(String[] args) {
        // 1. 配置好访问视频标签服务的基本信息,生成对应的一个客户端连接对象
        AisAccess aisAkskClient = ServiceAccessBuilder.builder()
                .ak("######")                       // your ak
                .sk("######")                       // your sk
                .region("cn-north-1")               // 视频标签服务目前支持华北-北京一(cn-north-1)
                .connectionTimeout(5000)            // 连接目标url超时限制
                .connectionRequestTimeout(1000)     // 连接池获取可用连接超时限制
                .socketTimeout(20000)               // 获取服务器响应数据超时限制
                .build();
        SimpleObsClient simpleObsClient = new SimpleObsClient(aisAkskClient);

        try {

            //
            // 2.构建访问视频标签服务需要的参数
            //
            //obs桶名, 根据需要自定即可
            String bucketName = "vct-sdk-test";

            //如果目标桶已存在，则不需要执行创建桶的动作
            simpleObsClient.createBucket(bucketName);

            //如果视频文件在本地，上传到OBS中，返回OBS文件句柄
            ObsFileHandle obsFileHandle = simpleObsClient.uploadFile(bucketName, "data/demo.mp4");

            //如果视频已在OBS中，直接获取其OBS文件句柄
            //ObsFileHandle obsFileHandle = simpleObsClient.locateFile(bucketName, "demo.mp4");

            //生成OBS文件临时授权下载链接
            String sharedDownloadUrl = obsFileHandle.generateSharedDownloadUrl();

            //
            // 3.调用视频标签服务
            //
            callVCTService(sharedDownloadUrl, aisAkskClient);
            //
            // 4. 可选动作，任务结束，将文件从obs中删除
            //
            // obsFileHandle.delete();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //
            // 5.使用完毕，关闭服务的客户端连接
            //
            aisAkskClient.close();
            simpleObsClient.close();
        }
    }

    private static void callVCTService(String videoUrl, AisAccess service) throws IOException, InterruptedException {
        JobMetaInfo jobMetaInfo = new JobMetaInfo();
        // 设置必选参数
        jobMetaInfo.setUrl(videoUrl);
        // 设置可选参数
        jobMetaInfo.setFrameInterval(5);
        jobMetaInfo.setThreshold(0);
        jobMetaInfo.setLanguage(Language.CHINESE);

        HttpResponse response = service.post(SUBMIT_JOB_URI, HttpJsonDataUtils.ObjectToHttpEntity(jobMetaInfo));

        if (!HttpJsonDataUtils.isOKResponded(response)) {
            System.out.println("Submit the job failed!");
            System.out.println("Request body:" + HttpJsonDataUtils.ObjectToJsonString(jobMetaInfo));
            System.out.println(HttpJsonDataUtils.responseToString(response));
            return;
        }

        // 获取到提交成功的任务ID, 准备进行结果的查询
        JobResult submitResult = HttpJsonDataUtils.getResponseObject(response, JobResult.class, JSON_ROOT);
        String jobId = submitResult.getId();
        System.out.println("\nSubmit job successfully, job_id=" + jobId);

        // 初始化查询jobId失败次数
        Integer retryTimes = 0;

        // 构建进行查询的请求链接，并进行轮询查询，由于是异步任务，必须多次进行轮询
        // 直到结果状态为任务已处理结束
        String url = String.format(GET_JOB_RESULT_URI_TEMPLATE, jobId);
        while (true) {
            HttpResponse getResponse = service.get(url);
            if (!HttpJsonDataUtils.isOKResponded(getResponse)) {
                System.out.println("Get " + url);
                System.out.println(HttpJsonDataUtils.responseToString(getResponse));
                if(retryTimes < RETRY_MAX_TIMES){
                    retryTimes++;
                    System.out.println(String.format("Jobs process result failed! The number of retries is %s!", retryTimes));
                    Thread.sleep(QUERY_JOB_RESULT_INTERVAL);
                    continue;
                }else{
                    break;
                }

            }
            JobResult jobResult
                    = HttpJsonDataUtils.getResponseObject(getResponse, JobResult.class, JSON_ROOT);
            JobStatus jobStatus = jobResult.getStatus();

            // 根据任务状态觉得继续轮询或者打印结果
            if (jobStatus == JobStatus.CREATED || jobStatus == JobStatus.RUNNING) {
                //如果任务还未处理完，等待一段时间，继续进行轮询
                System.out.println("Job " + jobResult.getStatus() + ", waiting...");
                Thread.sleep(QUERY_JOB_RESULT_INTERVAL);
            } else if (jobStatus == JobStatus.FAILED) {
                // 如果处理失败，直接退出
                System.out.println("\nJob failed!");
                break;
            } else if (jobStatus == JobStatus.FINISH) {
                // 任务处理成功，打印结果
                System.out.println("\nJob finished!");
                processJobFinishedResult(jobResult);
                break;
            } else {
                System.out.println("Should not be here!");
            }
        }
    }

    private static void processJobFinishedResult(JobResult jobResult) {
        System.out.println("\nVideo Tags:");
        List<Tag> tags = jobResult.getTags();
        //对标签进行根据出现次数排序
        tags.sort((tag1, tag2) -> {
            return tag2.getCount() - tag1.getCount();
        });

        tags.forEach(tag -> {
            System.out.println(String.format("tag:%s, count:%d, confidence:%f",
                    tag.getTag(), tag.getCount(), tag.getConfidence()));
        });
    }

}
