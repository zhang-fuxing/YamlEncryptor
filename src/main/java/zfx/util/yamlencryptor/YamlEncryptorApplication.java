package zfx.util.yamlencryptor;

import org.jasypt.encryption.StringEncryptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import zfx.util.yamlencryptor.util.SpringBeanUtills;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class YamlEncryptorApplication {
	
	public static void main(String[] args) throws IOException {
		SpringApplication.run(YamlEncryptorApplication.class, args);
		StringEncryptor encryptor = SpringBeanUtills.getBean(StringEncryptor.class);
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
		File file = new File(arg);
		if (!file.exists()) {
			boolean isFlush = file.createNewFile();
			if (!isFlush) {
				System.out.println("创建文件失败");
				System.exit(-1);
			}
		}
		var bos = new BufferedWriter(new FileWriter(file));
		try (bos) {
			encryMap.forEach((key, value) -> {
				try {
					bos.write("source: " + key + " --> target: ENC(" + value + ")\n");
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			});
		}
		System.out.println("\n密码已加密完成，请到 " + file.getCanonicalPath() + " 文件中查看");
		System.exit(0);
	}
	
}
