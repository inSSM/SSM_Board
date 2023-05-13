package ssm.exam.board.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;

@Service
public class WordAnalysisService {
	
	Komoran nlp = null;

	public WordAnalysisService() {
		this.nlp = new Komoran(DEFAULT_MODEL.LIGHT);
	}
	
	// 내용안의 단어를 List에 담아옴
	public List<String> doWordNouns(String text) throws Exception {
		String replace_text = text.replace("[^가-힣a-zA-Z0-9", " ");
		
		String trim_text = replace_text.trim();
		
		KomoranResult analyzeResultList = this.nlp.analyze(trim_text);
		
		List<String> rList = analyzeResultList.getNouns();
		
		if(rList == null) {
			rList = new ArrayList<String>();
		}
		
		return rList;
	}
	
	// 내용안의 추출한 단어를 빈도수를 (단어, 빈도수)로 가져옴
	public Map<String, Integer> doWordCount(List<String> pList) throws Exception {
		
		if(pList == null) {
			pList = new ArrayList<String>();
		}
		
		Map<String, Integer> rMap = new HashMap<>();
		
		Set<String> rSet = new HashSet<String>(pList);
		
		Iterator<String> it = rSet.iterator();
		
		while(it.hasNext()) {
			String word = it.next();
			int frequency = Collections.frequency(pList, word);
			
			rMap.put(word, frequency);
		}
		return rMap;
	}

	//내용 안의 빈도수 40% 아래의 단어들만을 추출
	public Set<String> doWordAnalysis(String text) throws Exception {
		List<String> rList = this.doWordNouns(text);
		
		if(rList == null) {
			rList = new ArrayList<String>();
		}
		
		Map<String, Integer> rMap = this.doWordCount(rList);
		
		if(rMap == null) {
			rMap = new HashMap<String, Integer>();
		}
		
		Set<String> rSet = new HashSet<String>(rList);

		int total = rMap.values().stream().mapToInt(Integer::intValue).sum();
		double percent = (total * 40.0) / 100.0;
		
		Iterator<String> it = rMap.keySet().iterator();
		
		while(it.hasNext()) {
			String key = it.next();
			if((int)percent < rMap.get(key)) {
				rSet.remove(key);
			}
		}
		
		return rSet;
	}

}
