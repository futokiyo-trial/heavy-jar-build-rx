package io.yoshizaki2104.heavyjarbuildrx.cli.service;

import io.yoshizaki2104.heavyjarbuildrx.cli.config.ScriptDef;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.function.Function;

public class BuildInvokerService {

    private static final Map<String, Function<ScriptDef,ProcessBuilder>> scriptProcBuilderMap = new HashMap<>();

    private static final int BUFFER_LEN = 1024;

    static {
        scriptProcBuilderMap.put("unix", scriptDef ->
                new ProcessBuilder(scriptDef.getScriptPath(), scriptDef.getMaxHeapSize()));

        scriptProcBuilderMap.put("ps1", scriptDef ->
             new ProcessBuilder("powershell", "-ExecutionPolicy", "RemoteSigned", "-File",
                    scriptDef.getScriptPath(), scriptDef.getMaxHeapSize())
        );
    }

    private final ScriptDef scriptDef;
    private final Path outputPath;

    public BuildInvokerService(ScriptDef scriptDef, Path outputPath){
        this.scriptDef = scriptDef;
        this.outputPath = outputPath;
    }

    public void invokeScript(CountDownLatch countDownLatch, Map<String, Integer> exitValueMap){

        ProcessBuilder pb = scriptProcBuilderMap.get(scriptDef.getRuntimeType())
                .apply(scriptDef);
        pb.redirectErrorStream(true);// 標準エラーを標準出力にマージする
        Process pr ;
        Integer exitValue = null;
        try{
            long startTime = System.currentTimeMillis();
            pr =pb.start();
            System.out.println(scriptDef.getScriptLabel() + " has started."
            + " [maxHeapSize:" + scriptDef.getMaxHeapSize()
            + " scriptPath:" + scriptDef.getScriptPath() + "]");
            printSystemOutToFile(scriptDef, outputPath, pr);
            exitValue = pr.waitFor();
            long elapsedTime = System.currentTimeMillis() - startTime;
            System.out.println(scriptDef.getScriptLabel()
                    + " has ended. exit[" + exitValue
                    + "] -> elapsed " + elapsedTime + "ms." );
        } catch(IOException | InterruptedException e){
            e.printStackTrace();
        } finally{
            exitValueMap.put(scriptDef.getScriptLabel(), exitValue);
            if(countDownLatch!=null){
                countDownLatch.countDown();
                System.out.println("ScriptLabel: " + this.scriptDef.getScriptLabel() +
                        " has completed , Count Down Latch residual : " + countDownLatch.getCount());
            }
        }


    }

    private void printSystemOutToFile(ScriptDef scriptDef, Path outputPath, Process pr) throws IOException {
        File logFile = new File(outputPath.toAbsolutePath() + "/" + scriptDef.getScriptLabel() + ".out");
        try(InputStream is = pr.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            FileOutputStream fos = new FileOutputStream(logFile);
            BufferedOutputStream bos = new BufferedOutputStream(fos)
        ){
            byte[] byteArrayData = new byte[BUFFER_LEN];
            int readByteNum = bis.read(byteArrayData);
            while(readByteNum!=-1){
                bos.write(byteArrayData, 0, readByteNum);
                printSystemOutToSystemOut(byteArrayData, readByteNum);
                readByteNum = bis.read(byteArrayData);
            }
            bos.flush();
        }
    }

    private void printSystemOutToSystemOut(final byte[] byteArrayData, final int readByteNum){
        ByteBuffer tmpBuffer = ByteBuffer.allocate(readByteNum);
        tmpBuffer.put(byteArrayData, 0, readByteNum);
        tmpBuffer.flip();
        byte[] tmpBytes = new byte[readByteNum];
        tmpBuffer.get(tmpBytes, 0, readByteNum);
        System.out.println("\t\t" +
                Thread.currentThread().getName()
                        + " label:" + scriptDef.getScriptLabel()
                        + " " + (new String(tmpBytes, StandardCharsets.UTF_8))

        );
    }
}
