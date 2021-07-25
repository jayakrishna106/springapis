package com.mytest.webapi.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;

@Component
@RequiredArgsConstructor
@Log4j2
public class CommandExecutor {
    final String COMMAND_PING_TEST = "cmd.exe /c ping 127.0.0.1";

    public String executeWithPing(){
        try {
            String result = pingTest(COMMAND_PING_TEST);
            return result;
        }
        catch (IOException | InterruptedException e){
            log.error("ping test failed");
        }
        return "ping test failed";
    }

    private String pingTest(String cmd) throws IOException, InterruptedException{
        Process process;
        process = Runtime.getRuntime().exec(String.format(cmd));

        String result = com.google.common.io.CharStreams.toString(new InputStreamReader(process.getInputStream(), "cp866"));
        process.waitFor();
        return result;
    }
}
