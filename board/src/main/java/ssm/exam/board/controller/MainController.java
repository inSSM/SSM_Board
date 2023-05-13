package ssm.exam.board.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import ssm.exam.board.model.BoardVO;
import ssm.exam.board.service.BoardService;
import ssm.exam.board.service.WordAnalysisService;

@Controller
@RequiredArgsConstructor
public class MainController {
	
	private final BoardService service;
	private final WordAnalysisService wordService;
	
	@GetMapping(value = "/list")
	public String getList(Model model) throws Exception {
		model.addAttribute("list", service.getList());
		return "list";
	}
	
	@GetMapping(value = "/write")
	public String writePage() {
		return "write";
	}
	
	@PostMapping(value = "/upload")
	public String uploadPost(BoardVO post) throws Exception {	
		List<BoardVO> postList = service.getList();
		Iterator<BoardVO> postIt = postList.iterator();
		Map<Integer, Integer> reCount = new HashMap<Integer, Integer>(); // 단어 나온 횟수를 위한 소팅용 Map
		List<Integer> relationList = new ArrayList<Integer>(); // DB에 저장할 연관 게시글 번호 List
		
		// 연관 게시글을 찾는 로직
		while(postIt.hasNext()) {
			int count = 0;
			BoardVO nPost = postIt.next();
			String content = nPost.getContent();
			content = content.replace(System.getProperty("line.separator").toString(), "");

			Set<String> wordList = wordService.doWordAnalysis(post.getContent());
			Iterator<String> wordIt = wordList.iterator();
			
			while(wordIt.hasNext()) {
				String word = wordIt.next();
				if(content.contains(word)) {
					count++;
				}
			}
			
			if(count>=2) {
				reCount.put(nPost.getNo(), count);
			}
		}
		
		// 단어가 많이 나온 순서대로 소팅
		List<Integer> countList = new ArrayList<>(reCount.keySet());
		Collections.sort(countList, (value1, value2) -> reCount.get(value2).compareTo(reCount.get(value1)));
		
		for(Integer key : countList) {
			relationList.add(key);
		}
		System.out.println(reCount);
		
		post.setRelation_no(relationList.toString());
		
		service.uploadPost(post);
		return "redirect:/list";
	}
	
	@GetMapping(value = "/view")
	public String openPost(@RequestParam final int no, Model model) throws Exception{
		BoardVO post = service.readPost(no);
		List<BoardVO> rePost = new ArrayList<BoardVO>();
		
		// 연관 글 존재시 조회
		if(!post.getRelation_no().equals("[]")) {
			String[] relationNo = (post.getRelation_no().replaceAll("[^0-9,]", "")).split(",");
			for(String list : relationNo) {
				rePost.add(service.readPost(Integer.parseInt(list)));
			}		
		}
		
		model.addAttribute("post", post);
		model.addAttribute("rp", rePost);

		return "view";
	}
	

}