package io.yoshizaki2104.heavyjarbuildrx.cli.app;


import io.yoshizaki2104.heavyjarbuildrx.cli.config.HjbRxConfig;
import io.yoshizaki2104.heavyjarbuildrx.cli.config.ScriptDef;
import io.yoshizaki2104.heavyjarbuildrx.cli.service.BuildInvokerService;
import io.yoshizaki2104.heavyjarbuildrx.cli.utils.JsonUtils;
import io.yoshizaki2104.heavyjarbuildrx.cli.utils.ReadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.concurrent.CountDownLatch;

public class HeavyJarBuildRxApp {

	private static final Logger LOGGER = LoggerFactory.getLogger(HeavyJarBuildRxApp.class);

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		String configFilePath = args[0];
		System.out.println(configFilePath);
		Map<String, Integer> exitValueMap = new LinkedHashMap<>();
		try {
			HjbRxConfig config = convertToConfig(configFilePath);
			List<ScriptDef> scriptDefList = config.getScripts();
			LOGGER.info("ParallelSize : " + config.getParallelSize());
			LOGGER.info("Scripts size : " + scriptDefList.size());
			scriptDefList.forEach(scriptDef -> exitValueMap.put(scriptDef.getScriptLabel(), null));

			Path outputPath = Paths.get(config.getLogDirectory() + System.currentTimeMillis());
			Files.createDirectory(outputPath);
			LOGGER.info("logOutPutDirectory : " + outputPath);

			int parallelSize = 1;
			if(config.getParallelSize()!=null && config.getParallelSize()>1){
				parallelSize = config.getParallelSize();
			}

			CountDownLatch countDownLatch = new CountDownLatch(scriptDefList.size());

			Flux.fromStream(scriptDefList.stream())
					.parallel(parallelSize)
					.runOn(Schedulers.boundedElastic()) //.runOn(Schedulers.parallel())
					.subscribe(scriptDef -> {
						BuildInvokerService service = new BuildInvokerService(scriptDef,outputPath);
						service.invokeScript(countDownLatch, exitValueMap);
					});

			countDownLatch.await();

		} catch (IOException|InterruptedException e) {
			e.printStackTrace();
			System.exit(1);
		} finally {
			LOGGER.info("Gross Elapsed Time : " + (System.currentTimeMillis() - startTime) + "ms." );
			exitValueMap.forEach((label, exitVal) -> LOGGER.info("label:" + label + " -> exit value[" + exitVal + "]."));
			System.exit(0);
		}

	}

	static HjbRxConfig convertToConfig(String path) throws IOException {

		String xmlContent = ReadUtils.readFrom(path);

		return JsonUtils.parse(HjbRxConfig.class, xmlContent);
	}

}
