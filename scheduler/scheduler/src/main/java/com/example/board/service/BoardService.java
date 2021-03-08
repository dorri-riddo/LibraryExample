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

	public void insertScheduler_report() throws Exception {
		boardMapper.insertScheduler_report();
	}
	
	public List<BoardDto> selectScheduler_report() throws Exception {
		return boardMapper.selectScheduler_report();
	}
}
