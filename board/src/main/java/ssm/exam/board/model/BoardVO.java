package ssm.exam.board.model;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BoardVO {

	private int no;
	private String title;
	private String id;
	private String reporting;
	private String content;
	private String relation_no;
	
}
