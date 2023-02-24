package zfx.util.yamlencryptor;

import org.jasypt.encryption.StringEncryptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.CollectionUtils;
import zfx.util.yamlencryptor.util.SpringBeanUtills;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class YamlEncryptorApplication {
	
	public static void main(String[] args) throws IOException {
		boolean flag = false;
		if (args != null) {
			List<String> list = Arrays.stream(args).filter(item -> item.contains("-p")).toList();
			if (!CollectionUtils.isEmpty(list)) {
				System.setProperty("jasypt.encryptor.password", list.get(0).substring(2));
				flag = true;
			}
		}
		
		SpringApplication.run(YamlEncryptorApplication.class, args);
		System.out.println("加密密钥：" + System.getProperty("jasypt.encryptor.password"));
		StringEncryptor encryptor = SpringBeanUtills.getBean(StringEncryptor.class);
		if (!flag) propGenet(args, encryptor);
		else paramGenet(args, encryptor);
	}
	
	private static void paramGenet(String[] args, StringEncryptor encryptor) throws IOException {
		if (args == null || args.length == 0) {
			System.exit(0);
		}
		
		String _path = null;
		Map<String, String> entryMap = new HashMap<>(args.length);
		for (String param : args) {
			if (param.contains("-p")) continue;
			if (param.contains("-d")) {
				_path = param.substring(2);
				continue;
			}
			entryMap.put(param, "ENC(" + encryptor.encrypt(param) + ")");
		}
		
		if (_path == null) {
			entryMap.forEach((k,v) -> System.out.printf("source: %1$s --> target: %2$s",k,v));
			System.exit(0);
		}
		outToFile(_path, entryMap, " --> target: ", "\n");
		
	}
	
	private static void outToFile(String _path, Map<String, String> entryMap, String x, String x1) throws IOException {
		File file = new File(_path);
		if (!file.exists()) {
			boolean isFlush = file.createNewFile();
			if (!isFlush) {
				System.out.println("创建文件失败");
				System.exit(-1);
			}
		}
		var bos = new BufferedWriter(new FileWriter(file));
		try (bos) {
			entryMap.forEach((key, value) -> {
				try {
					bos.write("source: " + key + x + value + x1);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			});
		}
		System.out.println("\n密码已加密完成，请到 " + file.getCanonicalPath() + " 文件中查看");
		System.exit(0);
	}
	
	private static void propGenet(String[] args, StringEncryptor encryptor) throws IOException {
		if (args == null || args.length < 2) System.exit(0);
		Map<String, String> encryMap = new HashMap<>(args.length - 1);
		for (int i = 0; i < args.length - 1; i++) {
			encryMap.put(args[i], encryptor.encrypt(args[i]));
		}
		
		String arg = args[args.length - 1];
		if ("/".equals(arg)) {
			System.out.println(" \n <-- 字符串加密结果 --> \n");
			encryMap.forEach((key, value) -> System.out.println("source: " + key + " --> target: ENC(" + value + ")"));
			System.exit(0);
		}
		outToFile(arg, encryMap, " --> target: ENC(", ")\n");
	}
	
}
