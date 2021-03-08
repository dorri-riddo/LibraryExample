package com.example.board.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.example.board.dto.BoardDto;
import com.example.board.mapper.BoardMapper;

@Service("com.example.board.service.BoardService")
public class BoardService {

	@Resource(name="com.example.board.mapper.BoardMapper")
	BoardMapper boardMapper;
	
	public List<BoardDto> selectUserList() throws Exception {
		return boardMapper.selectUserList();
	}
	
	public List<BoardDto> selectSchoolRecord(int user_id) throws Exception {
		return boardMapper.selectSchoolRecord(user_id);
	}
	
	public int selectUserCnt() throws Exception {
		return boardMapper.selectUserCnt();
	}
	
	public List<BoardDto> selectSubjectList() throws Exception {
		return boardMapper.selectSubjectList();
	}
	
	public int selectSubjectCnt() throws Exception {
		return boardMapper.selectSubjectCnt();
	}
}
