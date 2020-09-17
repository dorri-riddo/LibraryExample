package com.example.board.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.board.dto.BoardDto;

@Repository("com.example.board.mapper.BoardMapper")
public interface BoardMapper {
	public void insertScheduler_report() throws Exception;
	public List<BoardDto> selectScheduler_report() throws Exception;

}
