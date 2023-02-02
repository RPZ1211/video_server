package com.example.video_server.service.impl;

import com.example.video_server.common.entity.VideoPath;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class VideoCuttingService {


    //切割视频用到的工具地址
    private static String ffmpegPath;

    private static VideoPath videoPath;

    /**
     * 解析函数
     * @return 解析是否成功*/
    public static boolean ConvertVideo(VideoPath newVideoPath){
        getPath(newVideoPath);
        if(!checkFile(videoPath.getInputPath())){
            log.error(videoPath.getInputPath()+" 该文件不存在");
            return false;
        }
        if(process()){
            log.info(videoPath.getInputPath()+" 文件切割成功");
            return true;
        }
        return false;
    }

    /**
     * 处理输入文件、输出文件、视频处理文件的路径*/
    private static void getPath(VideoPath newVideoPath){
        videoPath = newVideoPath;
        try {
            File directory=new File("");
            String currPath= directory.getAbsolutePath();
            ffmpegPath = currPath + "/videoFile/ffmpeg/";//window测试路径
            // ffmpegPath = "/usr/bin/"; //服务器环境路径
        }catch (Exception e){
            log.error("getPath出错:{}",e.getMessage());
        }
    }

    /**
     * 切割函数，对视频文件进行切割*/
    private static boolean process() {
        int type = checkContentType();
        boolean status = false;
        if (type == 0) {
            System.out.println("直接转成M3U8格式");
            status = processM3U8(videoPath.getInputPath());// 直接转成M3U8格式
        } else if (type == 1) {
            String aviFilePath = processAVI(type);
            if (aviFilePath == null)
                return false;// 没有得到avi格式
            status = processM3U8(aviFilePath);// 将avi转成M3U8格式
        }
        return status;
    }

    /**
     * 对ffmpeg无法解析的文件格式(wmv9，rm，rmvb等),
     * 可以先用别的工具（mencoder）转换为avi(ffmpeg能解析的)格式.*/
    private static String processAVI(int type) {
        List<String> commend = new ArrayList<String>();
        commend.add(ffmpegPath + "mencoder");
        commend.add(videoPath.getInputPath());
        commend.add("-oac");
        commend.add("lavc");
        commend.add("-lavcopts");
        commend.add("acodec=mp3:abitrate=64");
        commend.add("-ovc");
        commend.add("xvid");
        commend.add("-xvidencopts");
        commend.add("bitrate=600");
        commend.add("-of");
        commend.add("avi");
        commend.add("-o");
        commend.add(videoPath.getOutPath() + videoPath.getNewFileName() +".avi");
        try {
            ProcessBuilder builder = new ProcessBuilder();
            Process process = builder.command(commend).redirectErrorStream(true).start();
            new PrintStream(process.getInputStream());
            new PrintStream(process.getErrorStream());
            process.waitFor();
            return videoPath.getOutPath() + videoPath.getNewFileName() +".avi";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * ffmpeg能解析的格式：（asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等）*/
    private static boolean processM3U8(String oldfilepath) {

        if (!checkFile(videoPath.getInputPath())) {
            System.out.println(oldfilepath + " is not file");
            return false;
        }

        List<String> command = new ArrayList<String>();
        command.add(ffmpegPath + "ffmpeg");
        command.add("-i");
        command.add(oldfilepath);
        command.add("-hls_time");
        command.add("20");
        command.add("-hls_list_size");
        command.add("0");
        command.add("-c:a");
        command.add("aac");
        command.add("-strict");
        command.add("-2");
        command.add("-f");
        command.add("hls");
        command.add(videoPath.getOutPath()+ videoPath.getNewFileName()+".m3u8");

        try {
            Process videoProcess = new ProcessBuilder(command).redirectErrorStream(true).start();
            new PrintStream(videoProcess.getErrorStream()).start();
            new PrintStream(videoProcess.getInputStream()).start();
            videoProcess.waitFor();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 判断文件类型*/
    private static int checkContentType() {
        String type = videoPath.getInputPath().substring(videoPath.getInputPath().lastIndexOf(".") + 1, videoPath.getInputPath().length())
                .toLowerCase();
        // ffmpeg能解析的格式：（asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等）
        if (type.equals("avi")) {
            return 0;
        } else if (type.equals("mpg")) {
            return 0;
        } else if (type.equals("wmv")) {
            return 0;
        } else if (type.equals("3gp")) {
            return 0;
        } else if (type.equals("mov")) {
            return 0;
        } else if (type.equals("mp4")) {
            return 0;
        } else if (type.equals("asf")) {
            return 0;
        } else if (type.equals("asx")) {
            return 0;
        } else if (type.equals("flv")) {
            return 0;
        }
        // 对ffmpeg无法解析的文件格式(wmv9，rm，rmvb等),
        // 可以先用别的工具（mencoder）转换为avi(ffmpeg能解析的)格式.
        else if (type.equals("wmv9")) {
            return 1;
        } else if (type.equals("rm")) {
            return 1;
        } else if (type.equals("rmvb")) {
            return 1;
        }
        return 9;
    }

    /**
     * 判断文件是否存在*/
    private static boolean checkFile(String path) {
        File file = new File(path);
        if (!file.isFile()) {
            return false;
        }
        return true;
    }


}
