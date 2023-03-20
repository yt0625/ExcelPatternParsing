package parsing;

import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import kr.co.shineware.nlp.komoran.model.Token;
import word.ExcelParser;
import kr.co.shineware.util.common.model.Pair;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WordClassifier {
	public static void action() throws IOException {

		Komoran komoran = new Komoran(DEFAULT_MODEL.FULL);
		ArrayList<String> document = ExcelParser.ExcelParse();
		LongCheckValues values = new LongCheckValues();
		String data = "";
		String title[] = { "법령종류", "정리", "대표어", "정비대상용어", "한자", "실제개정내용" };
		String[] checkException = { "{용}", "{뜻}" };
		String 법령종류 = "";
		String 정리 = "";
		String 대표어 = "";
		String 정비대상용어 = "";
		String 한자 = "";
		String 실제개정내용 = "";
		String explain = "→";
		String explainConcat = "";
		StringBuffer buf = new StringBuffer();
		Map<Integer, String> map = new HashMap<Integer, String>();
		int x = 0;
		for (int i = 0; i < document.size(); i++) {
			data = document.get(i);

			KomoranResult analyzeResultList = komoran.analyze(data);

			List<Token> tokenList = analyzeResultList.getTokenList();

			System.out.println("\nData-" + title[x] + ":" + data);
			boolean found = true;

			for (String word : checkException) {
				if (data.contains(word)) {
					System.out.println(data);
					found = false;
					break;
				}
			}

			for (Token token : tokenList) {
				
				
				boolean exists = false;
				for (int r = 0; r < values.arrayDo.size(); ++r) {
					String value = values.arrayDo.get(i);
					if (data.contains(value)) {
						exists = true;
						data.replace(value, "[*" + value + "*]");
					}
				}
				if (exists) {
					System.out.println("exist");
				} else {

				}
				
				
				data = title[0] != null ? 법령종류 = data : "";
				data = title[1] != null ? 정리 = data : "";
				data = title[2] != null ? 대표어 = data : "";
				data = title[3] != null ? 정비대상용어 = data : "";
				data = title[4] != null ? 한자 = data : "";
				data = title[5] != null ? 실제개정내용 = data : "";



				if (token.getPos().contains("NN") && found == true) {
					System.out.println("■" + 정비대상용어 + "▶[*앞특수*]" + 실제개정내용 + "[*조사1*]");
				} else if (found == false) {
					map.put(i, "■" + 정비대상용어 + "▶" + data);
					System.out.println("■" + 정비대상용어 + "▶" + data);
				} else if (token.getPos().contains("JK")) {
					System.out.println(token.getMorph() + "-조사");
				} else if (token.getPos().contains("VV")) {
					System.out.println(token.getMorph() + "-동사");
				} else {
					System.out.format("%s/%s", token.getMorph(), token.getPos());
				}

				if (법령종류.contains("설명")) {
					실제개정내용 = 실제개정내용.replace("(", "(" + explain);
					System.out.println("■" + 정비대상용어 + "▶" + 실제개정내용);
				}

			}

			x++;

			if (x == 6) {
				System.out.println("\n======\n");
				x = 0;
			}
		}
		Set<Integer> keySet = map.keySet();
		for (Integer key : keySet) {
			System.out.println(key + " : " + map.get(key));
		}
	}

	public static void main(String[] args) throws IOException {
		action();
	}
}