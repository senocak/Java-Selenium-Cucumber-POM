package com.senocak.fvs.utility.zulip;

import com.google.gson.Gson;
import com.senocak.fvs.config.ConfigFileReader;
import com.senocak.fvs.utility.Constants;
import com.senocak.fvs.utility.zulip.model.AllStreams;
import com.senocak.fvs.utility.zulip.model.AllUsers;
import com.senocak.fvs.utility.zulip.model.FileResponse;
import com.senocak.fvs.utility.zulip.model.Profile;
import com.senocak.fvs.utility.zulip.model.VoidResponse;
import com.senocak.fvs.utility.zulip.model.Zulip;
import io.cucumber.java.Scenario;
import io.taliox.zulip.ZulipRestExecutor;
import io.taliox.zulip.calls.messages.PostMessage;
import io.taliox.zulip.calls.messages.PostUploadFile;
import io.taliox.zulip.calls.streams.GetAllStreams;
import io.taliox.zulip.calls.users.GetAllUsers;
import io.taliox.zulip.calls.users.GetProfile;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@Slf4j
public class ZulipClient {
    private static final ZulipClient instance = new ZulipClient();
    private static final ConfigFileReader configFileReader = ConfigFileReader.getInstance();
    private static Zulip getZulip;
    private static ZulipRestExecutor executor;
    private static Gson gson;

    /**
     * Singleton constructor to prevent instantiation.
     * @return ZulipClient instance.
     */
    public static ZulipClient getInstance() {
        gson = new Gson();
        getZulip = configFileReader.getZulip();
        executor  = new ZulipRestExecutor(getZulip.getUserName(), getZulip.getPassword(), getZulip.getServerURL());
        return instance;
    }

    /**
     * Get all users from Zulip
     * @return AllUsers
     */
    public AllUsers getAllUser() {
        return gson.fromJson(executor.executeCall(new GetAllUsers()), AllUsers.class);
    }

    /**
     * Send message to Zulip
     * @param email Email of the user
     * @param content Content of the message
     * @return VoidResponse
     */
    public VoidResponse sendMessage(String email, String content) {
        return gson.fromJson(executor.executeCall(new PostMessage(email, content)),
                VoidResponse.class);
    }

    /**
     * Send message to Zulip stream
     * @param toStream Stream name
     * @param topic Topic of the message
     * @param content Content of the message
     * @return VoidResponse
     */
    public VoidResponse sendMessage(String toStream, String topic, String content) {
        return gson.fromJson(executor.executeCall(new PostMessage(toStream, topic, content)),
                VoidResponse.class);
    }

    /**
     * Upload file to Zulip
     * @param filePath Path of the file
     * @return FileResponse
     */
    public FileResponse uploadFile(String filePath) {
        return gson.fromJson(executor.executeCall(new PostUploadFile(filePath)),
                FileResponse.class);
    }

    /**
     * Get profile of the user
     * @return Profile
     */
    public Profile getProfile() {
        return gson.fromJson(executor.executeCall(new GetProfile()),
                Profile.class);
    }

    /**
     * Get all streams from Zulip
     * @return AllStreams
     */
    public AllStreams getAllStreams() {
        return gson.fromJson(executor.executeCall(new GetAllStreams()),
                AllStreams.class);
    }

    /**
     * Create report
     * @param scenario Scenario
     */
    public void createReport(Scenario scenario) {
        /*
        List<PickleStepTestStep> stepDefs;
        try {
            Field f = scenario.getClass().getDeclaredField("delegate");
            f.setAccessible(true);
            TestCaseState tcs = (TestCaseState) f.get(scenario);

            Field f2 = tcs.getClass().getDeclaredField("testCase");
            f2.setAccessible(true);
            TestCase r = (TestCase) f2.get(tcs);

            stepDefs = r.getTestSteps()
                    .stream()
                    .filter(x -> x instanceof PickleStepTestStep)
                    .map(x -> (PickleStepTestStep) x)
                    .collect(Collectors.toList());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        */
        String path = "target/cucumber-reports/" + scenario.getName() + ".zip";
        try {
            Constants.pack(Paths.get("target/cucumber-reports/cucumber-html-reports"),
                    Paths.get(path));
        } catch (IOException e) {
            log.error("Error creating report: {}", ExceptionUtils.getMessage(e));
        }
        String now = new SimpleDateFormat("dd/MM/yyyy:HH:mm:ss").format(Calendar.getInstance().getTime());
        String status = scenario.isFailed() ? ":red_book:" : ":green_book:";
        String content = "* **Time**: " + now + "\n * **Scenario**: " + scenario.getName() + "\n" + "* **Status**: " + status;
        //FileResponse fileResponse = uploadFile(path);
        //content += "\n * **Attachment**: [Log](" + getZulip.getServerURL() + fileResponse.getUri() + ")";
        sendMessage("test", "Castle", content);
        //FileUtils.deleteDirectory(new File(path));
    }
}