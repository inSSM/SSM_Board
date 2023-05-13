package ssm.exam.board.service;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ssm.exam.board.mapper.BoardMapper;
import ssm.exam.board.model.BoardVO;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {
	
	private final BoardMapper boardMapper;
	
	public List<BoardVO> getList(){
		return boardMapper.boardList();
	}
	
	public void uploadPost(BoardVO board) {
		
		boardMapper.boardInsert(board);
	}
	
	public BoardVO readPost(int no) {
		return boardMapper.boardDetail(no);
	}
	
	public List<Integer> relationPostNo(int no){
		return boardMapper.relationPostNo(no);
	}
	
}
