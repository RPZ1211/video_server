package com.example.video_server.service.impl;

import com.example.video_server.common.entity.VideoFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VideoCuttingService{

    private static String inputPath = "";
    private static String outputPath = "";
    private static String ffmpegPath = "";

    private static VideoFile videoFile=new VideoFile();


    /**
     * @return 是否解析成功！
     */
    public static boolean ConvertVideo(VideoFile videoFile_new) {
        videoFile=videoFile_new;
        getPath();
        if (!checkfile(inputPath)) {
            System.out.println(inputPath + " is not file");
            return false;
        }
        if (process()) {
            System.out.println("ok");
            return true;
        }
        return true;
    }

    private static void getPath() {
        // 先获取当前项目路径，在获得源文件、目标文件、转换器的路径
        File diretory = new File("");
        try {
            String currPath = diretory.getAbsolutePath();
            inputPath = videoFile.getOldFilePath();
            // 设置存放路径
            File upload=new File(videoFile.getNewFilePath());
            // 如果目录不存在就创建目录
            if(!upload.exists()){
                upload.mkdirs();
            }
            outputPath = videoFile.getNewFilePath();
            ffmpegPath = currPath + "/videoFile/ffmpeg/";//window测试路径
            // ffmpegPath = "/usr/bin/"; //服务器环境路径
        }
        catch (Exception e) {
            System.out.println("getPath出错");
        }
    }

    private static boolean process() {
        int type = checkContentType();
        boolean status = false;
        if (type == 0) {
            System.out.println("直接转成M3U8格式");
            status = processM3U8(inputPath);// 直接转成M3U8格式
        } else if (type == 1) {
            String avifilepath = processAVI(type);
            if (avifilepath == null)
                return false;// 没有得到avi格式
            status = processM3U8(avifilepath);// 将avi转成M3U8格式
        }
        return status;
    }

    private static int checkContentType() {
        String type = inputPath.substring(inputPath.lastIndexOf(".") + 1, inputPath.length())
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

    private static boolean checkfile(String path) {
        File file = new File(path);
        if (!file.isFile()) {
            return false;
        }
        return true;
    }

    // 对ffmpeg无法解析的文件格式(wmv9，rm，rmvb等), 可以先用别的工具（mencoder）转换为avi(ffmpeg能解析的)格式.
    private static String processAVI(int type) {
        List<String> commend = new ArrayList<String>();
        commend.add(ffmpegPath + "mencoder");
        commend.add(inputPath);
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
        commend.add(outputPath + "a.avi");
        try {
            ProcessBuilder builder = new ProcessBuilder();
            Process process = builder.command(commend).redirectErrorStream(true).start();
            new PrintStream(process.getInputStream());
            new PrintStream(process.getErrorStream());
            process.waitFor();
            return outputPath + "a.avi";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ffmpeg能解析的格式：（asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等）
    private static boolean processM3U8(String oldfilepath) {

        if (!checkfile(inputPath)) {
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
        command.add(outputPath+videoFile.getNewFileName());

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
}

class PrintStream extends Thread
{
    java.io.InputStream __is = null;
    public PrintStream(java.io.InputStream is)
    {
        __is = is;
    }

    public void run()
    {
        try
        {
            while(this != null)
            {
                int _ch = __is.read();
                if(_ch != -1)
                    System.out.print((char)_ch);
                else break;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
