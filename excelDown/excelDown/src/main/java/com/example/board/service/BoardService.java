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
	
	public int getCount() throws Exception {
		int count =  boardMapper.getCount();
		
		return count;
	}
	
	public List<BoardDto> getBoardList() throws Exception {
		List<BoardDto> list = boardMapper.getBoardList();
		
		return list;
	}
}
