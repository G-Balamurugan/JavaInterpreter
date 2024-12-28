package com.craftinginterpreters.lox;

import com.craftinginterpreters.lox.model.Scanner;
import com.craftinginterpreters.lox.model.Token;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@SpringBootApplication
public class Lox {
	static boolean hadError = false;

	public static void main(String[] args) throws IOException {
		if (args.length > 1) {
			System.err.println("Usage: jlox [script]");
			System.exit(64);
		} else if (args.length == 1) {
			runFile(args[0]);
		} else {
			runPrompt();
		}
	}

	private static void runFile(String filePath) throws IOException {
		byte[] bytes = Files.readAllBytes(Paths.get(filePath));
		run(new String(bytes, Charset.defaultCharset()));

		if (hadError) {
			System.exit(65);
		}
	}

	private static void runPrompt() throws IOException {
		InputStreamReader inputStreamReader = new InputStreamReader(System.in);
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

		for(;;) {
			System.out.println("> ");
			String line = bufferedReader.readLine();
			if (line == null) {
				break;
			}
			run(line);
			hadError = false;
		}
	}

	private static void run(String sourceInput) {
		Scanner scanner = new Scanner(sourceInput);
		List<Token> tokenList = scanner.scanTokens();

		System.out.println("> Tokens from source file");
		for (Token token : tokenList) {
			System.out.println(token);
		}
	}

	public static void error(int lineNumber, String errorMessage) {
		report(lineNumber, "", errorMessage);
	}

	public static void report(int lineNumber, String where, String message) {
		System.err.println(
				"[line " + lineNumber + "] Error" + where + ": " + message
		);
		hadError = true;
	}
}
