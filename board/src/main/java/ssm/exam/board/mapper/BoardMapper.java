package ssm.exam.board.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import ssm.exam.board.model.BoardVO;

@Repository("ssm.exam.board.mapper.BoardMapper")
public interface BoardMapper {
	
	public List<BoardVO> boardList();
	
	public BoardVO boardDetail(int no);

	public List<Integer> relationPostNo(int no);
	
	public int boardInsert(BoardVO board);

}
