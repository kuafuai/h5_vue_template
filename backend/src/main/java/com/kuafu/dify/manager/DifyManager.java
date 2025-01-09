package com.kuafu.dify.manager;

import com.google.common.collect.Lists;
import com.kuafu.common.config.AppConfig;
import com.kuafu.common.domin.ErrorCode;
import com.kuafu.common.exception.BusinessException;
import com.kuafu.common.file.FileUploadUtils;
import com.kuafu.common.util.StringUtils;
import com.kuafu.common.util.WordImportUtils;
import com.kuafu.dify.config.DifyConfig;
import com.kuafu.dify.entity.DifyFileVO;
import com.kuafu.dify.entity.OutputItem;
import com.kuafu.dify.request.ChatbotRequest;
import com.kuafu.dify.request.DifyRequest;
import com.kuafu.dify.service.DifyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.kuafu.dify.constant.DifyConstant.RESPONSE_MODE_BLOCKING;

@Slf4j
@Service
public class DifyManager {

    @Resource
    private DifyService difyService;

    @Resource
    private DifyConfig difyConfig;

    /**
     * 构建 DifyRequest
     *
     * @return
     */
    public DifyRequest getDifyRequest(ChatbotRequest chatbotRequest) {
        DifyRequest difyRequest = new DifyRequest();
        Map<String, Object> params = new LinkedHashMap<>();

        String query = chatbotRequest.getQuery();
        if (StringUtils.isEmpty(query)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "内容不能为空");
        }

        String prompt = chatbotRequest.getPrompt();
        if (StringUtils.isNotEmpty(prompt)) {
            params.put("prompt", prompt);
        } else {
            params.put("prompt", difyConfig.getPrompt());
        }

        difyRequest.setQuery(query);

        difyRequest.setInputs(params);
        difyRequest.setResponseMode(RESPONSE_MODE_BLOCKING);
        difyRequest.setUserId(chatbotRequest.getUserId());


        return difyRequest;

    }

    /**
     * 调用文件上传
     *
     * @param multipartFile
     * @param difyConfig
     * @return
     */
    public DifyFileVO callUpload(MultipartFile multipartFile, DifyConfig difyConfig) {
        return difyService.callUpload(multipartFile, difyConfig);
    }

    /**
     * 流式调用
     *
     * @param difyRequest
     * @param conversationId
     * @param userId
     * @param sseEmitter
     * @return
     * @throws IOException
     */
    public void callDifyStream(DifyRequest difyRequest, String conversationId, String userId, SseEmitter sseEmitter) {

        difyService.callApiStream(difyRequest, conversationId, userId, sseEmitter);
    }

    /**
     * 返回结果转Output
     *
     * @param answer
     * @param difyConfig
     * @return
     */
    public List<OutputItem> answer2Output(List<OutputItem> outputList, String answer, DifyConfig difyConfig) {
        if (answer == null || answer.isEmpty()) {
            return Lists.newArrayList();
        }

        return Lists.newArrayList();
    }


    /**
     * 判断输入是否为 Markdown 图片格式
     *
     * @param input 输入的字符串
     * @return 如果是图片格式返回 true，否则返回 false
     */
    public static boolean isMarkdownImageFormat(String input) {
        // 定义正则表达式，匹配 Markdown 图片格式
        String regex = "^!\\[[^\\]]*\\]\\([^\\)]+\\)$";

        // 使用正则表达式进行匹配
        return input.matches(regex);
    }

    public String md2Image(String mdUrl) {
        // 定义正则表达式，匹配 URL 部分
        String regex = "\\((.*?)\\)";

        // 创建 Pattern 和 Matcher 对象
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(mdUrl);

        if (matcher.find()) {
            // 去掉括号部分
//            String result = matcher.group().substring(1, matcher.group().length() - 1);
            String result = matcher.group(1);
            System.out.println("提取的 URL: " + result);
            return result;
        } else {
            System.out.println("未匹配到目标 URL");
        }
        return null;
    }

    /**
     * 通过文件URL获取文件路径
     *
     * @param url
     * @return
     */
    public String getFilePathByUrl(String url) {
        // 使用正则表达式提取路径
        String regex = "http://[^/]+(/.+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            String path = matcher.group(1);
            log.info("截取的路径:{} ", path);
            return path;
        } else {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "未匹配到路径");
        }
    }

    /**
     * 根据URL截取文件名
     *
     * @param filePath
     * @return
     */
    public String getFileNameByUrl(String filePath) {

        String regex = "upload/(.*)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(filePath);

        // 如果匹配成功，提取匹配到的部分
        if (matcher.find()) {
            String extractedPath = matcher.group(1);
            log.info("截取的路径: {}", extractedPath);
            return extractedPath;
        } else {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "获取文件名失败");
        }
    }

    /**
     * 根据文件路径获取文件内容
     *
     * @param filePath
     * @return
     */
    public String getFileContentByFilePath(String filePath) {

        String docContent = null;
        String absPath = null;
        File file = null;
        String uploadPath = AppConfig.getUploadPath();
        String fileName = getFileNameByUrl(filePath);
        try {
//            String pathFileName = FileUploadUtils.getPathFileName(uploadPath, fileName);
             absPath= FileUploadUtils.getAbsoluteFile(uploadPath, fileName).getAbsolutePath();
             file  = new File(absPath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!file.exists()) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "文件不存在");
        }

        // 获取文件内容
        if (file.exists()) {
            try (FileInputStream fileInputStream = new FileInputStream(file)) {

                String filename = file.getName();
                String fileSuffix = filename.substring(filename.lastIndexOf(".") + 1);  // 提取文件后缀
                docContent = WordImportUtils.readWord(fileInputStream, fileSuffix);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        log.info("文件内容：{}", docContent);
        return docContent;
    }
}
